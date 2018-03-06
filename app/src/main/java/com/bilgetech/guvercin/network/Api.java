package com.bilgetech.guvercin.network;

import com.bilgetech.guvercin.model.Message;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("/messages")
    Call<Object> sendMessages(@Body ArrayList<Message> messages);
}
