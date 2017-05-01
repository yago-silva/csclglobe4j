package com.suse.csclglobe4j.docker.registry.client.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ListRepositoriesResponseTest {

    @Test
    public void shouldCreateACopyOfOriginalListAtConstructor(){
        List<String> originalListOfRepos = new ArrayList();
        originalListOfRepos.add("my/repo1");
        originalListOfRepos.add("my/repo2");

        ListRepositoriesResponse clientResponse = new ListRepositoriesResponse(originalListOfRepos);

        assertThat(originalListOfRepos, hasSize(2));
        originalListOfRepos.add("new/repo");

        assertThat(originalListOfRepos, hasSize(3));
        assertThat(clientResponse.getRepositories(), hasSize(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnAnImutableListOfRepositories(){
        ListRepositoriesResponse clientResponse =
                new ListRepositoriesResponse(Arrays.asList("my/repo1", "my/repo2"));

        List<String> repositories = clientResponse.getRepositories();

        repositories.add("new/repo");

    }
}
