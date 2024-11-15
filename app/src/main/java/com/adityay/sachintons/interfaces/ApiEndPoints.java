package com.adityay.sachintons.interfaces;

import com.adityay.sachintons.models.Century;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiEndPoints {

    @GET("matches")
    Call<List<Century>> getCenturyList();
}
