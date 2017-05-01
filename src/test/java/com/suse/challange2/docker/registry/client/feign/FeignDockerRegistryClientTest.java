package com.suse.challange2.docker.registry.client.feign;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.client.dto.templates.ListRepositoriesResponseTemplate;
import com.suse.challange2.docker.registry.client.dto.templates.ListRepositoryImagesResponseTemplate;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeignDockerRegistryClientTest {

    private final static Response HTTP_ERROR_RESPONSE = Response.create(404, "Not Found", new HashMap<String, Collection<String>>(), new byte[0]);

    @Mock
    private FeignDockerRegistryStub mockedStub;

    @InjectMocks
    private FeignDockerRegistryClient feignDockerRegistryClient;

    @BeforeClass
    public static void initializeTemplate(){
        FixtureFactoryLoader.loadTemplates("com.suse.challange2.docker.registry.client.dto.templates");
    }

    @Test
    public void shouldListRepositories() throws IOException {
        ListRepositoriesResponse stubResponseBody = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        when(mockedStub.listRepositories()).thenReturn(stubResponseBody);

        ListRepositoriesResponse givenResponse = feignDockerRegistryClient.listRepositories();

        assertThat(givenResponse, equalTo(stubResponseBody));
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoRepositoriesOnRegistry() throws IOException {
        ListRepositoriesResponse stubResponseBody = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_ZERO_REPOSITORIES);

        when(mockedStub.listRepositories()).thenReturn(stubResponseBody);

        ListRepositoriesResponse givenResponse = feignDockerRegistryClient.listRepositories();

        assertThat(givenResponse.getRepositories(), hasSize(0));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldWrapFeignHttpErrorsWhenTryListingRepositories() throws IOException {
        when(mockedStub.listRepositories()).thenThrow(FeignException.errorStatus("", HTTP_ERROR_RESPONSE));
        feignDockerRegistryClient.listRepositories();
    }

    @Test
    public void shouldPreserveStatusCodeAndMessageOfErrorThrownByStubWhenTryListingRepositories() throws IOException {
        FeignException feignException = FeignException.errorStatus("", HTTP_ERROR_RESPONSE);
        when(mockedStub.listRepositories()).thenThrow(feignException);

        try {
            feignDockerRegistryClient.listRepositories();
            fail();
        }catch(HttpOperationFailedException ex){
            assertThat(ex.getStatusCode(), equalTo(feignException.status()));
            assertThat(ex.getMessage(), equalTo(feignException.getMessage()));
            assertThat(ex.getCause(), equalTo(feignException));
        }
    }

    @Test(expected = IOException.class)
    public void shouldWrapFeignNetworkErrorsWhenTryListingRepositories() throws IOException {
        when(mockedStub.listRepositories())
                .thenThrow(new RetryableException("Operation timeout", new Date()));

        feignDockerRegistryClient.listRepositories();
    }

    @Test
    public void shouldPreserveMessageAndCauseOfNetworkErrorsThrownByStubWhenTryListingRepositories() throws IOException {
        FeignException feignException = new RetryableException("Operation timeout", new Date());

        when(mockedStub.listRepositories()).thenThrow(feignException);

        try {
            feignDockerRegistryClient.listRepositories();
            Assert.fail();
        }catch(IOException ex){
            assertThat(ex.getMessage(), equalTo(feignException.getMessage()));
            assertThat(ex.getCause(), equalTo(feignException));
        }
    }

    @Test
    public void shouldListImagesByRepository() throws IOException {
        ListRepositoryImagesResponse stubResponseBody = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_MANY_TAGS);

        String repositoryName = stubResponseBody.getName();

        when(mockedStub.listImagesByRepository(repositoryName)).thenReturn(stubResponseBody);

        ListRepositoryImagesResponse givenResponse = feignDockerRegistryClient.listImagesByRepository(repositoryName);

        assertThat(givenResponse, equalTo(stubResponseBody));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldWrapFeignHttpErrorsWhenTryListingImagesByRepository() throws IOException {
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(FeignException.errorStatus("", HTTP_ERROR_RESPONSE));

        feignDockerRegistryClient.listImagesByRepository(repositoryName);
    }

    @Test
    public void shouldPreserveStatusCodeAndInfosOfErrorThrownByStubWhenTryListingImagesByRepository() throws IOException {
        FeignException feignException = FeignException.errorStatus("", HTTP_ERROR_RESPONSE);
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(feignException);

        try {
            feignDockerRegistryClient.listImagesByRepository(repositoryName);
            Assert.fail();
        }catch(HttpOperationFailedException ex){
            assertThat(ex.getStatusCode(), equalTo(feignException.status()));
            assertThat(ex.getMessage(), equalTo(feignException.getMessage()));
            assertThat(ex.getCause(), equalTo(feignException));
        }
    }

    @Test(expected = IOException.class)
    public void shouldWrapFeignNetworkErrorsWhenTryListingImagesByRepository() throws IOException {
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(new RetryableException("Operation timeout", new Date()));

        feignDockerRegistryClient.listImagesByRepository(repositoryName);
    }

    @Test
    public void shouldPreserveInfosOfNetworkErrorsThrownByStubWhenTryListingImagesByRepository() throws IOException {
        FeignException feignException = new RetryableException("Operation timeout", new Date());
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName)).thenThrow(feignException);

        try {
            feignDockerRegistryClient.listImagesByRepository(repositoryName);
            Assert.fail();
        }catch(IOException ex){
            assertThat(ex.getMessage(), equalTo(feignException.getMessage()));
            assertThat(ex.getCause(), equalTo(feignException));
        }
    }
}
