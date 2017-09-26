package com.emersondms.githubchallenge.model;

import java.util.List;

public class RepositoryList {

    private List<Repository> items;

    public List<Repository> getRepositories() {
        return items;
    }
    public void setRepositories(List<Repository> items) {
        this.items = items;
    }
}
