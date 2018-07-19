package com.elepy.admin.concepts;

import com.elepy.admin.models.Token;
import com.elepy.admin.models.User;
import com.elepy.admin.services.UserService;
import spark.Request;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class TokenHandler {

    private Set<Token> tokens;
    private final UserService userService;

    public TokenHandler(UserService userService) {
        this.tokens = new TreeSet<>();
        this.userService = userService;
    }

    public Optional<Token> createToken(Request request) {

        final Optional<String[]> credentials = basicCredentials(request);

        if (!credentials.isPresent()) {
            return Optional.empty();
        }

        final String username = credentials.get()[0];
        final String password = credentials.get()[1];

        return createToken(username, password);
    }

    public Optional<Token> createToken(String username, String password) {
        final Optional<User> login = userService.login(username, password);

        if (!login.isPresent()) {
            return Optional.empty();
        }

        final Token token = new Token().setId(UUID.randomUUID().toString()).setCreationTime(System.currentTimeMillis()).setDuration(1000 * 60 * 60 * 24 * 7).setUser(login.get());

        tokens.add(token);
        return Optional.of(token);
    }

    public void flushTokens() {
        final long currentTime = System.currentTimeMillis();
        tokens = tokens.stream().filter(token -> {

            final long maxTime = token.getCreationTime() + token.getDuration();

            return currentTime < maxTime;
        }).collect(Collectors.toSet());


    }

    private Optional<String[]> basicCredentials(Request request) {
        String header = request.headers("Authorization");

        if (header == null || !(header = header.trim()).startsWith("Basic")) {
            return Optional.empty();
        }

        header = header.replaceAll("Basic", "").trim();
        final String[] strings = readBasicUsernamePassword(header);

        if (strings.length != 2) {
            return Optional.empty();
        }
        return Optional.of(strings);

    }

    private String[] readBasicUsernamePassword(String base64) {
        String credentials = new String(Base64.getDecoder().decode(base64),
                Charset.forName("UTF-8"));
        return credentials.split(":", 2);
    }

}