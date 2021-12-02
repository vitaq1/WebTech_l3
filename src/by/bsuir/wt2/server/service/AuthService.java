package by.bsuir.wt2.server.service;

import by.bsuir.wt2.server.data.AuthType;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final AuthService INSTANCE = new AuthService();

    private final Map<Object, AuthType> users;

    private AuthService() {
        users = new HashMap<>();
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public AuthType getAuthType(Object user) {
        if (!users.containsKey(user)) {
            users.put(user, AuthType.GUEST);
        }

        return users.get(user);
    }

    public void setAuthType(Object user, AuthType type) {
        users.put(user, type);
    }
}