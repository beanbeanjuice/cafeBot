package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import com.beanbeanjuice.cafeapi.endpoints.cafe.user.CafeUserEntity;
import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.security.*;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JWTIssuer jwtIssuer;
    private final JWTDecoder jwtDecoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public ResponseEntity<?> attemptLogin(final String email, final String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        JWTIssuer.Request jwtRequest = JWTIssuer.Request.builder()
                .userId(principal.getUserId())
                .username(principal.getUsername())
                .roles(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        String accessToken = jwtIssuer.issueToken(jwtRequest);
        String refreshToken = jwtIssuer.issueRefreshToken(jwtRequest);

        ResponseEntity<?> refreshTokenUpdateResponse = this.updateRefreshToken(refreshToken, principal);

        if (refreshTokenUpdateResponse.getStatusCode() != HttpStatus.CREATED) return refreshTokenUpdateResponse;

        return ResponseEntity.status(HttpStatus.OK).body(
                Response
                        .message("Successfully logged in.")
                        .body("user",
                                LoginResponse.builder()
                                        .userID(principal.getUserId())
                                        .username(principal.getUsername())
                                        .accessToken(accessToken)
                                        .refreshToken(refreshToken)
                                        .build()
                        )
        );
    }

    private ResponseEntity<?> updateRefreshToken(final String refreshToken, final UserPrincipal userPrincipal) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            CafeUserEntity user = session.get(CafeUserEntity.class, userPrincipal.getUserId());

            RefreshTokenEntity oldRefreshTokenEntity = session.createQuery(
                            "SELECT token FROM RefreshTokenEntity token WHERE token.user = :user",
                            RefreshTokenEntity.class
                    )
                    .setParameter("user", user)
                    .getSingleResultOrNull();

            session.beginTransaction();

            if (oldRefreshTokenEntity != null) {
                oldRefreshTokenEntity.setRefreshToken(refreshToken);
                oldRefreshTokenEntity.setExpirationDate(Timestamp.from(jwtDecoder.decode(refreshToken).getExpiresAtAsInstant()));
                oldRefreshTokenEntity.setUser(user);
                session.merge(oldRefreshTokenEntity);
            } else {
                RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
                newRefreshTokenEntity.setRefreshToken(refreshToken);
                newRefreshTokenEntity.setUser(user);
                newRefreshTokenEntity.setExpirationDate(Timestamp.from(jwtDecoder.decode(refreshToken).getExpiresAtAsInstant()));
                session.persist(newRefreshTokenEntity);
            }

            session.getTransaction().commit();
            return ResponseEntity.status(HttpStatus.CREATED).body(Response.of("Refresh token updated."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("Cannot update refresh token: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> refreshAccessToken(final String refreshToken) {
        // Decode and validate the refresh token
        RefreshTokenEntity refreshTokenEntity;
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            refreshTokenEntity = session.createQuery(
                    "SELECT refreshToken FROM RefreshTokenEntity refreshToken WHERE refreshToken.refreshToken = :refreshToken AND refreshToken.expirationDate > :currentTimeStamp",
                    RefreshTokenEntity.class)
                    .setParameter("refreshToken", refreshToken)
                    .setParameter("currentTimeStamp", new Timestamp(System.currentTimeMillis()))
                    .getSingleResultOrNull();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("Unable to refresh access token: %s", e.getMessage())));
        }

        if (refreshTokenEntity == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.of("Invalid refresh token."));

        // Load user details based on username or userId
        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenEntity.getUser().getUsername());

        // Create authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate new access token
        JWTIssuer.Request jwtRequest = JWTIssuer.Request.builder()
                .userId(refreshTokenEntity.getUser().getId())
                .username(refreshTokenEntity.getUser().getUsername())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        String accessToken = jwtIssuer.issueToken(jwtRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
            Response
                    .message("Successfully refreshed access token.")
                    .body("user",
                            LoginResponse.builder()
                                    .userID(refreshTokenEntity.getUser().getId())
                                    .username(userDetails.getUsername())
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)  // Optionally return a new refresh token if desired
                                    .build()
                    )
        );
    }

    @Transactional
    public ResponseEntity<?> revokeRefreshToken(final String refreshToken) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            RefreshTokenEntity refreshTokenEntity = session.createQuery(
                    "SELECT refreshToken FROM RefreshTokenEntity refreshToken WHERE refreshToken.refreshToken = :refreshToken",
                    RefreshTokenEntity.class
            )
                    .setParameter("refreshToken", refreshToken)
                    .getSingleResultOrNull();

            Map<String, Object> successResponse = Response.of("Refresh token has been invalidated.");
            if (refreshTokenEntity == null) return ResponseEntity.status(HttpStatus.OK).body(successResponse);

            session.getTransaction().begin();
            session.remove(refreshTokenEntity);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("Cannot revoke refresh token: %s", e.getMessage())));
        }
    }

}
