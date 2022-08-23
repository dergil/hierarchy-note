package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.SignupDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitUser {

    @POST("/api/core/user/signup")
    Call<Void> signup(@Query("username") String username, @Query("password") String password);

    @POST("/api/core/login")
    Call<Void> login(@Body SignupDto signupDto);
}
