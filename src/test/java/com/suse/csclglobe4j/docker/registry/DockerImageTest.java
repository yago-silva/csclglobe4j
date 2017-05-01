package com.suse.csclglobe4j.docker.registry;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class DockerImageTest {

    private final static String REPOSITORY_NAME = "repositoryName";
    private final static String TAG = "tag";

    @Test
    public void imagesWithTheSameRepositoryAndTagShouldBeEquals(){
        DockerImage firstRepository = new DockerImage(REPOSITORY_NAME, TAG);
        DockerImage secondRepository = new DockerImage(REPOSITORY_NAME, TAG);

        assertThat(firstRepository, equalTo(secondRepository));
    }

    @Test
    public void imagesWithTheSameRepositoryAndDifferentTagShouldBeDifferent(){
        DockerImage firstRepository = new DockerImage(REPOSITORY_NAME, TAG);
        DockerImage secondRepository = new DockerImage(REPOSITORY_NAME, "other_tag");

        assertThat(firstRepository, not(equalTo(secondRepository)));
    }

    @Test
    public void imagesWithDifferentRepositoryNameAndSameTagShouldBeDiferent(){
        DockerImage firstRepository = new DockerImage(REPOSITORY_NAME, TAG);
        DockerImage secondRepository = new DockerImage("other_repository", TAG);

        assertThat(firstRepository, not(equalTo(secondRepository)));
    }
}