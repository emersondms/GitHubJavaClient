package com.emersondms.githubchallenge.api;

import com.emersondms.githubchallenge.model.PullRequest;
import com.emersondms.githubchallenge.model.RepositoryList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("repositories?q=language:Java&sort=stars&per_page=15")
    Call<RepositoryList> getRepositoriesJson(@Query("page") int page);

    @GET("{user}/{repository}/pulls?per_page=15")
    Call<List<PullRequest>> getPullRequestsJson(
        @Path(value = "user", encoded = true) String user,
        @Path(value = "repository", encoded = true) String repository,
        @Query("page") int page
    );
}
