package com.suse.challange2.docker.registry.service.dto;

import java.util.ArrayList;
import java.util.List;

public class ListRepositoryImagesResponse {

    private String name;

    private List<String> tags;

    public ListRepositoryImagesResponse(String name, List<String> tags) {
        this.name = name;
        this.tags = new ArrayList<>(tags);
    }

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }
}