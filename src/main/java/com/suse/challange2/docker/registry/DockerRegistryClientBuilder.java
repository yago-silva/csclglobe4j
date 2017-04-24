package com.suse.challange2.docker.registry;

import com.squareup.okhttp.OkHttpClient;
import com.suse.challange2.docker.registry.service.DockerRegistryService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.concurrent.TimeUnit;

public class DockerRegistryClientBuilder {

    private String baseUrl;

    public NonRequiredParametersStep withBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
        return new NonRequiredParametersStep();
    }

    public class NonRequiredParametersStep {

        private long timeout = 5000l;

        private NonRequiredParametersStep(){}

        public NonRequiredParametersStep withTimeout(long timeout){
            this.timeout = timeout;
            return this;
        }

        public DockerRegistryClient build(){
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(timeout, TimeUnit.MILLISECONDS);

            RestAdapter retrofit = new RestAdapter.Builder()
                    .setEndpoint(baseUrl)
                    .setClient(new OkClient(okHttpClient))
                    .build();

            DockerRegistryService dockerRegistryService = retrofit.create(DockerRegistryService.class);

            return new DockerRegistryClient(dockerRegistryService);
        }
    }
}