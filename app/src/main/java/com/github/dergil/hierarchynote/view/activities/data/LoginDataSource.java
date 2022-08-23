package com.github.dergil.hierarchynote.view.activities.data;

import com.github.dergil.hierarchynote.model.dto.SignupDto;
import com.github.dergil.hierarchynote.model.network.JwtAPI;
import com.github.dergil.hierarchynote.view.activities.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private JwtAPI jwtAPI = new JwtAPI();


    public Result<LoggedInUser> login(String username, String password) {

        try {
            jwtAPI.signup(username, password);
            SignupDto signupDto = new SignupDto();
            signupDto.setEmail(username);
            signupDto.setPassword(password);
            String jwt = jwtAPI.login(signupDto);



            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}