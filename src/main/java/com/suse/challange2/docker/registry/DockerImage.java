package com.suse.challange2.docker.registry;

public class DockerImage {

    private String repositoryName;

    private String tag;

    public DockerImage(String repositoryName, String tag) {
        this.repositoryName = repositoryName;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DockerImage)) return false;

        DockerImage that = (DockerImage) o;

        if (!repositoryName.equals(that.repositoryName)) return false;
        return tag.equals(that.tag);

    }

    @Override
    public int hashCode() {
        int result = repositoryName.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }

    @Override

    public String toString() {
        return String.join(":", repositoryName, tag);
    }
}
