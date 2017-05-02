package com.suse.csclglobe4j.docker.registry.client.retrofit;

import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.concurrent.TimeUnit;

public class RetrofitDockerRegistryStubFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitDockerRegistryStubFactory.class);

    public static RetrofitDockerRegistryStub get(String baseUrl, long timeout){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(timeout, TimeUnit.MILLISECONDS);

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(okHttpClient))
                .build();

        RetrofitDockerRegistryStub stub = retrofit.create(RetrofitDockerRegistryStub.class);

        LOGGER.debug("A new RetrofitDockerRegistryStub has been created! Timeout was set to %s milliseconds " +
                "and base url was set to %s", timeout, baseUrl);

        return stub;
    }
}