package com.emersondms.githubchallenge.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.emersondms.githubchallenge.R;
import com.emersondms.githubchallenge.view.activity.ToolbarTitleSetter;
import com.emersondms.githubchallenge.view.adapter.PullRequestAdapter;
import com.emersondms.githubchallenge.model.PullRequest;
import com.emersondms.githubchallenge.api.ApiService;
import com.emersondms.githubchallenge.api.ConsumerType;
import com.emersondms.githubchallenge.api.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PullRequestFragment extends Fragment {

    private String user, repository;
    private View view;
    @BindView(R.id.loadRecyclerProgress) ProgressBar progressBar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PullRequestAdapter adapter;
    private ApiService service;
    private int currentPage;
    private static final int FIRST_PAGE = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        layoutManager = new LinearLayoutManager(
            getContext(), LinearLayoutManager.VERTICAL, false);

        adapter = new PullRequestAdapter(getContext(), user, repository);
        service = new RetrofitClient(ConsumerType.PULL_REQUEST).getApiService();

        ButterKnife.bind(this, view);
        setupRecyclerView();
        loadFirstPage();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        user = getArguments().getString("user");
        repository = getArguments().getString("repository");
        ((ToolbarTitleSetter) activity).setToolbarTitle(repository);
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new ScrollListener(layoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private void loadFirstPage() {
        currentPage = FIRST_PAGE;
        callApiService().enqueue(new Callback<List<PullRequest>>() {
            @Override
            public void onResponse(Call<List<PullRequest>> call, Response<List<PullRequest>> response) {
                if (response.isSuccessful()) {
                    List<PullRequest> results = response.body();
                    if (!results.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        adapter.addAll(results);
                        adapter.addLoadingFooter();
                        isLoading = false;
                    } else {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PullRequest>> call, Throwable t) {
            }
        });
    }

    private void loadNextPage() {
        callApiService().enqueue(new Callback<List<PullRequest>>() {
            @Override
            public void onResponse(Call<List<PullRequest>> call, Response<List<PullRequest>> response) {
                if (response.isSuccessful()) {
                    List<PullRequest> results = response.body();
                    if (!results.isEmpty()) {
                        adapter.removeLoadingFooter();
                        adapter.addAll(results);
                        adapter.addLoadingFooter();
                        isLoading = false;
                    } else {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PullRequest>> call, Throwable t) {
            }
        });
    }

    private Call<List<PullRequest>> callApiService() {
        return service.getPullRequestsJson(user, repository, currentPage);
    }
}
