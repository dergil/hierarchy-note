package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.dto.SignupDto;
import com.github.dergil.hierarchynote.model.dto.SignupResponseDto;

public interface JwtNetworkingInterface {
    SignupResponseDto signup(SignupDto signupDto);
    String login(String email, String password);
}
