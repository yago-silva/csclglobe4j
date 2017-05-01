package com.suse.csclglobe4j.docker.registry.client.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ListRepositoryImagesResponseTest {

    @Test
    public void shouldCreateACopyOfOriginalListAtConstructor(){
        List<String> originalListOftags = new ArrayList();
        originalListOftags.add("tag1");
        originalListOftags.add("tag2");

        ListRepositoryImagesResponse clientResponse = new ListRepositoryImagesResponse("my/repo", originalListOftags);

        assertThat(originalListOftags, hasSize(2));
        originalListOftags.add("new_tag");

        assertThat(originalListOftags, hasSize(3));
        assertThat(clientResponse.getTags(), hasSize(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnAnImutableListOfTags(){
        ListRepositoryImagesResponse clientResponse =
                new ListRepositoryImagesResponse("my/repo", Arrays.asList("tag1", "tag2"));

        List<String> tags = clientResponse.getTags();

        tags.add("new_tag");
    }
}
