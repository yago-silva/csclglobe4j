package com.suse.challange2.docker.registry.client.retrofit;

import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.concurrent.TimeUnit;

public class RetrofitDockerRegistryStubFactory {

    public static RetrofitDockerRegistryStub get(String baseUrl, long timeout){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(timeout, TimeUnit.MILLISECONDS);

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(okHttpClient))
                .build();

        return retrofit.create(RetrofitDockerRegistryStub.class);
    }
}
