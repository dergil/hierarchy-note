package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.dto.SignupDto;

public interface JwtNetworkingInterface {
    void signup(String email, String password);
    String login(SignupDto signupDto);
}
