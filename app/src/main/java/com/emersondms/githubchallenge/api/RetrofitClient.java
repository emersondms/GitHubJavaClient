package com.emersondms.githubchallenge.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private String BASE_URL;

    public RetrofitClient(ConsumerType type) {
        switch (type) {
            case REPOSITORY:
                BASE_URL = "https://api.github.com/search/";
                break;
            case PULL_REQUEST:
                BASE_URL = "https://api.github.com/repos/";
                break;
        }
    }

    public ApiService getApiService() {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);
    }
}