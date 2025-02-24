package org.lab.service;

import org.lab.model.Role;

public class Token {
    private String token;
    private int expires;
    private Role role;

    public Token() {}

    public Token(String token, int expires, Role role) {
        this.token = token;
        this.expires = expires;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
