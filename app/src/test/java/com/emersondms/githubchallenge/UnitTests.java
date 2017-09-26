package com.emersondms.githubchallenge;

import com.emersondms.githubchallenge.api.ConsumerType;
import com.emersondms.githubchallenge.api.RetrofitClient;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class UnitTests {

    private int testPage = 1;

    @Test
    public void test_repositoriesApiReturn() throws Exception {
        assertNotNull(
            new RetrofitClient(ConsumerType.REPOSITORY)
                .getApiService()
                .getRepositoriesJson(testPage)
        );
    }

    @Test
    public void test_pullRequestsApiReturn() throws Exception {
        String testOwner = "square";
        String testRepository = "retrofit";
        assertNotNull(
            new RetrofitClient(ConsumerType.PULL_REQUEST)
                .getApiService()
                .getPullRequestsJson(testOwner, testRepository, testPage)
        );
    }
}