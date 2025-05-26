package com.defi.auth.user.service;

public interface LoginFailureService {
    long onFailure(String username);
    void onSuccess(String username);
}
