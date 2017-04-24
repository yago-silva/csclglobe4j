package com.suse.challange2.docker.registry;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.suse.challange2.docker.registry.dto.DockerRegistryServiceResponse;
import com.suse.challange2.docker.registry.dto.tempaltes.DockerRegistryServiceResponseTemplate;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.challange2.docker.registry.service.DockerRegistryService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

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
        FixtureFactoryLoader.loadTemplates("com.suse.challange2.docker.registry.dto.tempaltes");
    }

    @Before
    public void setup(){
        this.dockerRegistryClient = new DockerRegistryClient(dockerRegistryService);
    }

    @Test
    public void shouldListRepositories() throws IOException {
        DockerRegistryServiceResponse serviceResponse = Fixture.from(DockerRegistryServiceResponse.class)
                        .gimme(DockerRegistryServiceResponseTemplate.WITH_MANY_REPOSITORIES);

        when(dockerRegistryService.listRepositories()).thenReturn(serviceResponse);

        List<String> givenRepositories = dockerRegistryClient.listRepositories();

        assertThat(givenRepositories, hasSize(serviceResponse.getRepositories().size()));
        assertTrue(givenRepositories.containsAll(serviceResponse.getRepositories()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void repositoryListShouldBeImmutable() throws IOException{
        DockerRegistryServiceResponse serviceResponse = Fixture.from(DockerRegistryServiceResponse.class)
                .gimme(DockerRegistryServiceResponseTemplate.WITH_MANY_REPOSITORIES);

        when(dockerRegistryService.listRepositories()).thenReturn(serviceResponse);

        List<String> repositories = dockerRegistryClient.listRepositories();

        repositories.add("my/newRepository");
    }

    @Test
    public void shouldReturnAnEmptyListWhenThereIsNoImagesOnRegistry() throws IOException{
        DockerRegistryServiceResponse serviceResponse = Fixture.from(DockerRegistryServiceResponse.class)
                .gimme(DockerRegistryServiceResponseTemplate.WITH_ZERO_REPOSITORIES);

        when(dockerRegistryService.listRepositories()).thenReturn(serviceResponse);

        List<String> repositories = dockerRegistryClient.listRepositories();
        assertThat(repositories, hasSize(0));
    }

    @Test(expected = HttpOperationFailedException.class)
    public void shouldThrowHttpOperationFailedExceptionWhenANetworkProblemOccur()  throws IOException {
        when(dockerRegistryService.listRepositories()).thenThrow(new RuntimeException());
        dockerRegistryClient.listRepositories();
    }
}