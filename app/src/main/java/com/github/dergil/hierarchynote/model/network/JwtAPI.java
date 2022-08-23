package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.dto.SignupDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

public class JwtAPI implements JwtNetworkingInterface{
    RetrofitUser retrofitUser;
    String BASE_URL = "http://10.0.2.2:8080/";

    public JwtAPI() {initNetworkingStack();}

    @Override
    public void signup(String email, String password) {
        Call<Void> questions = retrofitUser.signup(email, password);
        try {
            Response<Void> execute = questions.execute();
            System.out.println(execute.isSuccessful());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String login(SignupDto signupDto) {
        Call<Void> questions = retrofitUser.login(signupDto);
        try {
            Response<Void> execute = questions.execute();
            System.out.println(execute.isSuccessful());
            if (execute.isSuccessful()) {
                Headers headers = execute.headers();
                String jwt = headers.get("Authorization");
                System.out.println(jwt);
                return jwt;
            }
            else return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initNetworkingStack(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitUser = retrofit.create(RetrofitUser.class);
    }
}
