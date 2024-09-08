package com.beanbeanjuice.cafeapi.security;

import com.beanbeanjuice.cafeapi.endpoints.cafe.user.CafeUserEntity;
import com.beanbeanjuice.cafeapi.endpoints.cafe.user.CafeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CafeUserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        CafeUserEntity user = userService.findByUsername(username).orElseThrow();

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).toList();

        return UserPrincipal.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
