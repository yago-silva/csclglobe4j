package com.suse.challange2.docker.registry;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.suse.challange2.docker.registry.exceptions.NetworkException;
import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.client.dto.templates.ListRepositoriesResponseTemplate;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.challange2.docker.registry.client.DockerRegistryClient;
import com.suse.challange2.docker.registry.client.dto.templates.ListRepositoryImagesResponseTemplate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DockerRegistryTest {

    @Mock
    private DockerRegistryClient dockerRegistryClient;

    private DockerRegistry dockerRegistry;

    @BeforeClass
    public static void initializeTemplate(){
        FixtureFactoryLoader.loadTemplates("com.suse.challange2.docker.registry.client.dto.templates");
    }

    @Before
    public void setup(){
        this.dockerRegistry = new DockerRegistry(dockerRegistryClient);
    }

    @Test
    public void shouldListRepositories() throws IOException {
        ListRepositoriesResponse clientResponse = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        when(dockerRegistryClient.listRepositories()).thenReturn(clientResponse);

        List<String> givenRepositories = dockerRegistry.listRepositories();

        assertThat(givenRepositories, hasSize(clientResponse.getRepositories().size()));
        assertTrue(givenRepositories.containsAll(clientResponse.getRepositories()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void repositoryListShouldBeImmutable() throws IOException{
        ListRepositoriesResponse clientResponse = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        when(dockerRegistryClient.listRepositories()).thenReturn(clientResponse);

        List<String> repositories = dockerRegistry.listRepositories();

        repositories.add("my/newRepository");
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoImagesOnRegistry() throws IOException{
        ListRepositoriesResponse clientResponse = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_ZERO_REPOSITORIES);

        when(dockerRegistryClient.listRepositories()).thenReturn(clientResponse);

        List<String> repositories = dockerRegistry.listRepositories();
        assertThat(repositories, hasSize(0));
    }

    @Test(expected = NetworkException.class)
    public void shouldThrowExceptionWhenANetworkProblemOccurListingRepositories()  throws IOException {
        when(dockerRegistryClient.listRepositories()).thenThrow(new IOException());
        dockerRegistry.listRepositories();
    }

    @Test
    public void shouldRespectErrorStatusCodeGivenFromClientWhenListingRepository() throws Exception {
        final int internalServerError = 500;
        Response response = new Response("", internalServerError, "", new ArrayList<>(), null);
        RetrofitError cause = RetrofitError.httpError("", response, null, null);

        when(dockerRegistryClient.listRepositories())
                .thenThrow(new HttpOperationFailedException(internalServerError, "Internal Server Error", cause));

        try {
            dockerRegistry.listRepositories();
            fail();
        }catch(HttpOperationFailedException ex){
            assertThat(ex.getStatusCode(), equalTo(internalServerError));
        }
    }

    @Test
    public void shouldListImagesByRepositoryName() throws Exception {
        ListRepositoryImagesResponse resposeBody = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_MANY_TAGS);

        String repositoryName = resposeBody.getName();


        when(dockerRegistryClient.listImagesByRepository(repositoryName)).thenReturn(resposeBody);
        List<DockerImage> givenImages = dockerRegistry.listImagesByRepository(repositoryName);

        assertThat(givenImages, hasSize(resposeBody.getTags().size()));
        List<String> givenImagesNames = givenImages.stream().map(img -> img.getTag()).collect(Collectors.toList());
        assertTrue(givenImagesNames.containsAll(resposeBody.getTags()));
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoImagesForRepository() throws Exception {
        ListRepositoryImagesResponse expectedResponse = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_ZERO_TAGS);

        String repositoryName = expectedResponse.getName();

        when(dockerRegistryClient.listImagesByRepository(repositoryName)).thenReturn(expectedResponse);
        List<DockerImage> givenImages = dockerRegistry.listImagesByRepository(repositoryName);

        assertThat(givenImages, hasSize(0));
    }

    @Test(expected = NetworkException.class)
    public void shouldThrowExceptionWhenANetworkProblemOccurListingImagesByRepository() throws Exception {
        String repositoryName = UUID.randomUUID().toString();

        when(dockerRegistryClient.listImagesByRepository(repositoryName)).thenThrow(new IOException());
        dockerRegistry.listImagesByRepository(repositoryName);
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldThrowExceptionWhenClientReturnAnErrorStatusCodeListingImagesByRepository() throws Exception {
        String repositoryName = UUID.randomUUID().toString();

        when(dockerRegistryClient.listImagesByRepository(repositoryName))
                .thenThrow(new HttpOperationFailedException(404, "Internal Server Error", null));

        dockerRegistry.listImagesByRepository(repositoryName);
    }

    @Test
    public void shouldGetAllAvailableImages() throws Exception {
        ListRepositoriesResponse listReposResponseBody = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        List<String> repositories = listReposResponseBody.getRepositories();

        Map<String, List<ListRepositoryImagesResponse>> imagesByRepositoryResponseBody = repositories.stream()
                .map(ListRepositoryImagesResponseTemplate::newTemplateByRepositoryName)
                .collect(Collectors.groupingBy(ListRepositoryImagesResponse::getName));

        when(dockerRegistryClient.listRepositories()).thenReturn(listReposResponseBody);

        repositories.forEach(repositoryName -> {
            try {
                when(dockerRegistryClient.listImagesByRepository(repositoryName))
                        .thenReturn(imagesByRepositoryResponseBody.get(repositoryName).get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<DockerImage> givenImages = dockerRegistry.listAllAvailableImages();
        assertThat(givenImages, hasSize(9));

        listReposResponseBody.getRepositories().forEach(repository -> {
            List<DockerImage> expectedImagesForRepository = imagesByRepositoryResponseBody.get(repository).stream()
                    .map(respBody -> respBody.getTags()).flatMap(List::stream)
                    .map(tag -> new DockerImage(repository, tag)).collect(Collectors.toList());

            assertTrue(givenImages.containsAll(expectedImagesForRepository));
        });
    }
}