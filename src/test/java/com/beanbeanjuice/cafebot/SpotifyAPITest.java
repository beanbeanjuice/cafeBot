package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.CafeBot;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SpotifyAPITest {

//    @Test
//    @DisplayName("Spotify API Test")
//    public void spotifyAPITest() throws IOException, ParseException, SpotifyWebApiException {
//        SpotifyApi spotifyApi = new SpotifyApi.Builder()
//                .setClientId(CafeBot.getSpotifyApiClientID())
//                .setClientSecret(CafeBot.getSpotifyApiClientSecret())
//                .build();
//
//        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
//
//        try {
//            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
//            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
//        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException ignored) {}
//
//        Assertions.assertEquals((spotifyApi.getTrack("0XUU1fzICK7484jpGnfGvd").build().execute().getName()), "unspoken words");
//    }

}
