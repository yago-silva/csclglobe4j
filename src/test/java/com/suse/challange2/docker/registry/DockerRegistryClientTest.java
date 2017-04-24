package com.suse.challange2.docker.registry;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.suse.challange2.docker.registry.service.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.service.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.service.dto.templates.ListRepositoriesResponseTemplate;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.challange2.docker.registry.service.DockerRegistryService;
import com.suse.challange2.docker.registry.service.dto.templates.ListRepositoryImagesResponseTemplate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DockerRegistryClientTest {

    @Mock
    private DockerRegistryService dockerRegistryService;

    private DockerRegistryClient dockerRegistryClient;

    @BeforeClass
    public static void initializeTemplate(){
        FixtureFactoryLoader.loadTemplates("com.suse.challange2.docker.registry.service.dto.templates");
    }

    @Before
    public void setup(){
        this.dockerRegistryClient = new DockerRegistryClient(dockerRegistryService);
    }

    @Test
    public void shouldListRepositories() throws IOException {
        ListRepositoriesResponse serviceResponse = Fixture.from(ListRepositoriesResponse.class)
                        .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        when(dockerRegistryService.listRepositories()).thenReturn(serviceResponse);

        List<String> givenRepositories = dockerRegistryClient.listRepositories();

        assertThat(givenRepositories, hasSize(serviceResponse.getRepositories().size()));
        assertTrue(givenRepositories.containsAll(serviceResponse.getRepositories()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void repositoryListShouldBeImmutable() throws IOException{
        ListRepositoriesResponse serviceResponse = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        when(dockerRegistryService.listRepositories()).thenReturn(serviceResponse);

        List<String> repositories = dockerRegistryClient.listRepositories();

        repositories.add("my/newRepository");
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoImagesOnRegistry() throws IOException{
        ListRepositoriesResponse serviceResponse = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_ZERO_REPOSITORIES);

        when(dockerRegistryService.listRepositories()).thenReturn(serviceResponse);

        List<String> repositories = dockerRegistryClient.listRepositories();
        assertThat(repositories, hasSize(0));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldThrowExceptionWhenANetworkProblemOccurListingRepositories()  throws IOException {
        when(dockerRegistryService.listRepositories()).thenThrow(new RuntimeException());
        dockerRegistryClient.listRepositories();
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldThrowExceptionWhenServiceReturnAnErrorStatusCodeListingRepository() throws Exception {
        //Runtime exception was thrown when we get an error status code
        when(dockerRegistryService.listRepositories()).thenThrow(new RuntimeException());
        dockerRegistryClient.listRepositories();
    }

    @Test
    public void shouldListImagesByRepositoryName() throws Exception {
        ListRepositoryImagesResponse expectedResponse = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_MANY_TAGS);

        String repositoryName = expectedResponse.getName();


        when(dockerRegistryService.listImagesByRepository(repositoryName)).thenReturn(expectedResponse);
        List<String> givenImages = dockerRegistryClient.listImagesByRepository(repositoryName);

        assertThat(givenImages, hasSize(expectedResponse.getTags().size()));
        assertTrue(givenImages.containsAll(expectedResponse.getTags()));
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoImagesForRepository() throws Exception {
        ListRepositoryImagesResponse expectedResponse = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_ZERO_TAGS);

        String repositoryName = expectedResponse.getName();

        when(dockerRegistryService.listImagesByRepository(repositoryName)).thenReturn(expectedResponse);
        List<String> givenImages = dockerRegistryClient.listImagesByRepository(repositoryName);

        assertThat(givenImages, hasSize(0));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldThrowExceptionWhenANetworkProblemOccurListingImagesByRepository() throws Exception {
        String repositoryName = UUID.randomUUID().toString();

        when(dockerRegistryService.listImagesByRepository(repositoryName)).thenThrow(new IOException());
        dockerRegistryClient.listImagesByRepository(repositoryName);
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldThrowExceptionWhenServiceReturnAnErrorStatusCodeListingImagesByRepository() throws Exception {
        String repositoryName = UUID.randomUUID().toString();

        //Runtime exception was thrown when we get an error status code
        when(dockerRegistryService.listImagesByRepository(repositoryName)).thenThrow(new RuntimeException());
        dockerRegistryClient.listImagesByRepository(repositoryName);
    }
}