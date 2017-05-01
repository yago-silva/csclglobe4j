package com.suse.csclglobe4j.docker.registry.client.retrofit;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.templates.ListRepositoriesResponseTemplate;
import com.suse.csclglobe4j.docker.registry.client.dto.templates.ListRepositoryImagesResponseTemplate;
import com.suse.csclglobe4j.docker.registry.exceptions.HttpOperationFailedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrofitDockerRegistryClientTest {

    private Response httpErrorResponse;

    @Mock
    private RetrofitDockerRegistryStub mockedStub;

    @InjectMocks
    private RetrofitDockerRegistryClient retrofitDockerRegistryClient;

    @BeforeClass
    public static void initializeTemplate(){
        FixtureFactoryLoader.loadTemplates("com.suse.csclglobe4j.docker.registry.client.dto.templates");
    }

    @Before
    public void setup(){
        httpErrorResponse = new Response("fake_url", 404, "Not Found", new ArrayList<>(), null);
    }

    @Test
    public void shouldListRepositories() throws IOException {
        ListRepositoriesResponse stubResponseBody = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        when(mockedStub.listRepositories()).thenReturn(stubResponseBody);

        ListRepositoriesResponse givenResponse = retrofitDockerRegistryClient.listRepositories();

        assertThat(givenResponse, equalTo(stubResponseBody));
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoRepositoriesOnRegistry() throws IOException {
        ListRepositoriesResponse stubResponseBody = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_ZERO_REPOSITORIES);

        when(mockedStub.listRepositories()).thenReturn(stubResponseBody);

        ListRepositoriesResponse givenResponse = retrofitDockerRegistryClient.listRepositories();

        assertThat(givenResponse.getRepositories(), hasSize(0));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldWrapRetrofitHttpErrorsWhenTryListingRepositories() throws IOException {
        when(mockedStub.listRepositories()).thenThrow(RetrofitError.httpError(null, httpErrorResponse, null, null));
        retrofitDockerRegistryClient.listRepositories();
    }

    @Test
    public void shouldPreserveStatusCodeAndMessageOfErrorThrownByStubWhenTryListingRepositories() throws IOException {
        RetrofitError retrofitError = RetrofitError.httpError(null, httpErrorResponse, null, null);
        when(mockedStub.listRepositories()).thenThrow(retrofitError);

        try {
            retrofitDockerRegistryClient.listRepositories();
            fail();
        }catch(HttpOperationFailedException ex){
            assertThat(ex.getStatusCode(), equalTo(httpErrorResponse.getStatus()));
            assertThat(ex.getMessage(), equalTo(httpErrorResponse.getReason()));
            assertThat(ex.getCause(), equalTo(retrofitError));
        }
    }

    @Test(expected = IOException.class)
    public void shouldWrapRetrofitNetworkErrorsWhenTryListingRepositories() throws IOException {
        when(mockedStub.listRepositories())
                .thenThrow(RetrofitError.networkError("fake_url", new IOException("Operation timeout")));

        retrofitDockerRegistryClient.listRepositories();
    }

    @Test
    public void shouldPreserveMessageAndCauseOfNetworkErrorsThrownByStubWhenTryListingRepositories() throws IOException {
        RetrofitError retrofitError = RetrofitError.networkError("fake_url", new IOException("Operation timeout"));

        when(mockedStub.listRepositories()).thenThrow(retrofitError);

        try {
            retrofitDockerRegistryClient.listRepositories();
            fail();
        }catch(IOException ex){
            assertThat(ex.getMessage(), equalTo(retrofitError.getMessage()));
            assertThat(ex.getCause(), equalTo(retrofitError));
        }
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapRetrofitGenericErrorsWhenTryListingRepositories() throws IOException {
        when(mockedStub.listRepositories())
                .thenThrow(RetrofitError.unexpectedError("fake_url", new Exception("Unexpected Error!")));

        retrofitDockerRegistryClient.listRepositories();
    }

    @Test
    public void shouldPreserveMessageAndCauseOfGenericErrorsThrownByStubWhenTryListingRepositories() throws IOException {
        RetrofitError retrofitError = RetrofitError.unexpectedError("fake_url", new Exception("Unexpected Error!"));

        when(mockedStub.listRepositories()).thenThrow(retrofitError);

        try {
            retrofitDockerRegistryClient.listRepositories();
            fail();
        }catch(RuntimeException ex){
            assertThat(ex.getMessage(), equalTo(retrofitError.getMessage()));
            assertThat(ex.getCause(), equalTo(retrofitError));
        }
    }

    @Test
    public void shouldListImagesByRepository() throws IOException {
        ListRepositoryImagesResponse stubResponseBody = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_MANY_TAGS);

        String repositoryName = stubResponseBody.getName();

        when(mockedStub.listImagesByRepository(repositoryName)).thenReturn(stubResponseBody);

        ListRepositoryImagesResponse givenResponse = retrofitDockerRegistryClient.listImagesByRepository(repositoryName);

        assertThat(givenResponse, equalTo(stubResponseBody));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldWrapRetrofitHttpErrorsWhenTryListingImagesByRepository() throws IOException {
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(RetrofitError.httpError(null, httpErrorResponse, null, null));

        retrofitDockerRegistryClient.listImagesByRepository(repositoryName);
    }

    @Test
    public void shouldPreserveStatusCodeAndInfosOfErrorThrownByStubWhenTryListingImagesByRepository() throws IOException {
        RetrofitError retrofitError = RetrofitError.httpError(null, httpErrorResponse, null, null);
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(retrofitError);

        try {
            retrofitDockerRegistryClient.listImagesByRepository(repositoryName);
            fail();
        }catch(HttpOperationFailedException ex){
            assertThat(ex.getStatusCode(), equalTo(httpErrorResponse.getStatus()));
            assertThat(ex.getMessage(), equalTo(httpErrorResponse.getReason()));
            assertThat(ex.getCause(), equalTo(retrofitError));
        }
    }

    @Test(expected = IOException.class)
    public void shouldWrapRetrofitNetworkErrorsWhenTryListingImagesByRepository() throws IOException {
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(RetrofitError.networkError("fake_url", new IOException("Operation timeout")));

        retrofitDockerRegistryClient.listImagesByRepository(repositoryName);
    }

    @Test
    public void shouldPreserveInfosOfNetworkErrorsThrownByStubWhenTryListingImagesByRepository() throws IOException {
        RetrofitError retrofitError = RetrofitError.networkError("fake_url", new IOException("Operation timeout"));
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName)).thenThrow(retrofitError);

        try {
            retrofitDockerRegistryClient.listImagesByRepository(repositoryName);
            fail();
        }catch(IOException ex){
            assertThat(ex.getMessage(), equalTo(retrofitError.getMessage()));
            assertThat(ex.getCause(), equalTo(retrofitError));
        }
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapRetrofitGenericErrorsWhenTryListingImagesByRepository() throws IOException {
        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName))
                .thenThrow(RetrofitError.unexpectedError("fake_url", new Exception("Unexpected Error!")));

        retrofitDockerRegistryClient.listImagesByRepository(repositoryName);
    }

    @Test
    public void shouldPreserveInfosOfGenericErrorsThrownByStubWhenTryListingImagesByRepository() throws IOException {
        RetrofitError retrofitError = RetrofitError.unexpectedError("fake_url", new Exception("Unexpected Error!"));

        String repositoryName = "my/repo1";
        when(mockedStub.listImagesByRepository(repositoryName)).thenThrow(retrofitError);

        try {
            retrofitDockerRegistryClient.listImagesByRepository(repositoryName);
            fail();
        }catch(RuntimeException ex){
            assertThat(ex.getMessage(), equalTo(retrofitError.getMessage()));
            assertThat(ex.getCause(), equalTo(retrofitError));
        }
    }
}