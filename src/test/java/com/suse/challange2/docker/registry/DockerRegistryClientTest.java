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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        ListRepositoryImagesResponse resposeBody = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_MANY_TAGS);

        String repositoryName = resposeBody.getName();


        when(dockerRegistryService.listImagesByRepository(repositoryName)).thenReturn(resposeBody);
        List<DockerImage> givenImages = dockerRegistryClient.listImagesByRepository(repositoryName);

        assertThat(givenImages, hasSize(resposeBody.getTags().size()));
        List<String> givenImagesNames = givenImages.stream().map(img -> img.getTag()).collect(Collectors.toList());
        assertTrue(givenImagesNames.containsAll(resposeBody.getTags()));
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoImagesForRepository() throws Exception {
        ListRepositoryImagesResponse expectedResponse = Fixture.from(ListRepositoryImagesResponse.class)
                .gimme(ListRepositoryImagesResponseTemplate.WITH_ZERO_TAGS);

        String repositoryName = expectedResponse.getName();

        when(dockerRegistryService.listImagesByRepository(repositoryName)).thenReturn(expectedResponse);
        List<DockerImage> givenImages = dockerRegistryClient.listImagesByRepository(repositoryName);

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

    @Test
    public void shouldGetAllAvailableImages() throws Exception {
        ListRepositoriesResponse listReposResponseBody = Fixture.from(ListRepositoriesResponse.class)
                .gimme(ListRepositoriesResponseTemplate.WITH_MANY_REPOSITORIES);

        List<String> repositories = listReposResponseBody.getRepositories();

        Map<String, List<ListRepositoryImagesResponse>> imagesByRepositoryResponseBody = repositories.stream()
                .map(ListRepositoryImagesResponseTemplate::newTemplateByRepositoryName)
                .collect(Collectors.groupingBy(ListRepositoryImagesResponse::getName));

        when(dockerRegistryService.listRepositories()).thenReturn(listReposResponseBody);

        repositories.forEach(repositoryName -> {
            try {
                when(dockerRegistryService.listImagesByRepository(repositoryName))
                        .thenReturn(imagesByRepositoryResponseBody.get(repositoryName).get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<DockerImage> givenImages = dockerRegistryClient.listAllAvailableImages();
        assertThat(givenImages, hasSize(9));

        listReposResponseBody.getRepositories().forEach(repository -> {
            List<DockerImage> expectedImagesForRepository = imagesByRepositoryResponseBody.get(repository).stream()
                    .map(respBody -> respBody.getTags()).flatMap(List::stream)
                    .map(tag -> new DockerImage(repository, tag)).collect(Collectors.toList());

            assertTrue(givenImages.containsAll(expectedImagesForRepository));
        });
    }
}