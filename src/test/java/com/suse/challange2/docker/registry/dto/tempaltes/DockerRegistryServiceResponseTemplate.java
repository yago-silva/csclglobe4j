package com.suse.challange2.docker.registry.dto.tempaltes;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.suse.challange2.docker.registry.dto.DockerRegistryServiceResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class DockerRegistryServiceResponseTemplate implements TemplateLoader {

    public static final String WITH_MANY_REPOSITORIES = "WITH_MANY_REPOSITORIES";

    public static final String WITH_ZERO_REPOSITORIES = "WITH_ZERO_REPOSITORIES";

    @Override
    public void load() {

        Fixture.of(DockerRegistryServiceResponse.class).addTemplate(WITH_MANY_REPOSITORIES, new Rule(){{
            add("repositories", Arrays.asList("my/repository1", "my/repository2", "my/repository3"));
        }});

        Fixture.of(DockerRegistryServiceResponse.class).addTemplate(WITH_ZERO_REPOSITORIES, new Rule(){{
            add("repositories", new ArrayList<>());
        }});
    }
}
