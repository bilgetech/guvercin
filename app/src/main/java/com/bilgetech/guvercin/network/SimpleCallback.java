package com.bilgetech.guvercin.network;

import android.util.Log;
import android.widget.Toast;

import com.bilgetech.guvercin.App;
import com.bilgetech.guvercin.model.ErrorResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SimpleCallback<T> implements Callback<T> {

    private static final String TAG = SimpleCallback.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            ErrorResponse errorResponse = ApiGenerator.parseError(response);
            errorResponse.setStatus(response.code());
            onError(errorResponse);
        }

        afterAllCompleted();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof ConnectivityHelper.NoConnectivityException) {
            onNoConnectivity();
        } else {
            onIOError(call, t);
        }
        afterAllCompleted();
    }

    /**
     * Override this method to implement your error logic.
     */
    public void onError(ErrorResponse errorResponse) {
        Toast.makeText(App.getInstance(), errorResponse.getMessage().toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * Override this method if you want to do something after all success and fail scenarios.
     */
    public void afterAllCompleted() {
    }

    /**
     * Override this method if you want to do something when the user is not connected to the internet.
     */
    public void onNoConnectivity() {
        Toast.makeText(App.getInstance(), "You are not connected to the internet. Please try again.", Toast.LENGTH_LONG).show();
    }

    public void onIOError(Call<T> call, Throwable t) {
        Log.e(TAG, "Request with path: " + call.request().url().encodedPath() + " is failed due to an IOException");
        t.printStackTrace();
    }

    public abstract void onSuccess(T data);
}
