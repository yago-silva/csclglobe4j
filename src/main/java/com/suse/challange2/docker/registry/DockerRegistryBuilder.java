package com.suse.challange2.docker.registry;

import com.suse.challange2.docker.registry.client.DockerRegistryClient;
import com.suse.challange2.docker.registry.client.retrofit.RetrofitDockerRegistryClient;
import com.suse.challange2.docker.registry.client.retrofit.RetrofitDockerRegistryStub;
import com.suse.challange2.docker.registry.client.retrofit.RetrofitDockerRegistryStubFactory;

public class DockerRegistryBuilder {

    private String baseUrl;

    public NonRequiredParametersStep withBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
        return new NonRequiredParametersStep();
    }

    public class NonRequiredParametersStep {

        private long timeout = 5000l;

        private NonRequiredParametersStep(){}

        public NonRequiredParametersStep withTimeoutInMilliseconds(long timeout){
            this.timeout = timeout;
            return this;
        }

        public DockerRegistry build(){
            RetrofitDockerRegistryStub retrofitDockerRegistryStub =
                    RetrofitDockerRegistryStubFactory.get(baseUrl, timeout);
            DockerRegistryClient dockerRegistryClient = new RetrofitDockerRegistryClient(retrofitDockerRegistryStub);

            return new DockerRegistry(dockerRegistryClient);
        }
    }
}