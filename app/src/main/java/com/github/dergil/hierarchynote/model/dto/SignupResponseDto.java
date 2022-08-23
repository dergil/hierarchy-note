package com.github.dergil.hierarchynote.model.dto;

import java.util.List;

public class SignupResponseDto {
    private String id;
    private String email;
    private List<String> roles;
    private boolean unverified;
    private boolean blocked;
    private boolean admin;
    private boolean goodUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isUnverified() {
        return unverified;
    }

    public void setUnverified(boolean unverified) {
        this.unverified = unverified;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isGoodUser() {
        return goodUser;
    }

    public void setGoodUser(boolean goodUser) {
        this.goodUser = goodUser;
    }
}
