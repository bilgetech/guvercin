package com.bilgetech.guvercin.network;

import android.util.Log;

import com.bilgetech.guvercin.model.ErrorResponse;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGenerator {
    private static final String TAG = ApiGenerator.class.getSimpleName();

    private static Retrofit retrofit;


    public static Api create(String baseUrl) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        OkHttpClient httpClient = httpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(httpClient)
                .build();

        return retrofit.create(Api.class);
    }

    public static ErrorResponse parseError(Response<?> response) {
        final Converter<ResponseBody, ErrorResponse> converter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        ErrorResponse error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            Log.e(TAG, "Cannot parse the error body:");
            Log.e(TAG, response.errorBody().toString());
            return new ErrorResponse();
        }

        return error;
    }
}
