package com.suse.csclglobe4j.docker.registry.client.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListRepositoryImagesResponse {

    private String name;

    private List<String> tags;

    ListRepositoryImagesResponse(){}

    public ListRepositoryImagesResponse(String name, List<String> tags) {
        this.name = name;
        this.tags = new ArrayList<>(tags);
    }

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
}