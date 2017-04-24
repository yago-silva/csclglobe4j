package com.suse.challange2.docker.registry.service.dto.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.suse.challange2.docker.registry.service.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.service.dto.ListRepositoryImagesResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class ListRepositoryImagesResponseTemplate implements TemplateLoader {

    public static final String WITH_MANY_TAGS = "WITH_MANY_TAGS";

    public static final String WITH_ZERO_TAGS = "WITH_ZERO_TAGS";

    @Override
    public void load() {

        Fixture.of(ListRepositoryImagesResponse.class).addTemplate(WITH_MANY_TAGS, new Rule(){{
            add("name", name());
            add("tags", Arrays.asList("latest", "v1.1.1", "v2.1.1"));
        }});

        Fixture.of(ListRepositoryImagesResponse.class).addTemplate(WITH_ZERO_TAGS).inherits(WITH_MANY_TAGS, new Rule(){{
            add("tags", new ArrayList<>());
        }});
    }
}
