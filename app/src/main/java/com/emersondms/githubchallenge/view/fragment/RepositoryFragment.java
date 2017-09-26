package com.emersondms.githubchallenge.view.fragment;

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
import com.emersondms.githubchallenge.view.adapter.RepositoryAdapter;
import com.emersondms.githubchallenge.model.Repository;
import com.emersondms.githubchallenge.model.RepositoryList;
import com.emersondms.githubchallenge.api.ApiService;
import com.emersondms.githubchallenge.api.ConsumerType;
import com.emersondms.githubchallenge.api.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryFragment extends Fragment {

    private View view;
    @BindView(R.id.loadRecyclerProgress) ProgressBar progressBar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RepositoryAdapter adapter;
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

        adapter = new RepositoryAdapter(getContext(), getFragmentManager());
        service = new RetrofitClient(ConsumerType.REPOSITORY).getApiService();

        ButterKnife.bind(this, view);
        setupRecyclerView();
        loadFirstPage();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
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
        callApiService().enqueue(new Callback<RepositoryList>() {
            @Override
            public void onResponse(Call<RepositoryList> call, Response<RepositoryList> response) {
                if (response.isSuccessful()) {
                    List<Repository> results = response.body().getRepositories();
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
            public void onFailure(Call<RepositoryList> call, Throwable t) {
            }
        });
    }

    private void loadNextPage() {
        callApiService().enqueue(new Callback<RepositoryList>() {
            @Override
            public void onResponse(Call<RepositoryList> call, Response<RepositoryList> response) {
                if (response.isSuccessful()) {
                    List<Repository> results = response.body().getRepositories();
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
            public void onFailure(Call<RepositoryList> call, Throwable t) {
            }
        });
    }

    private Call<RepositoryList> callApiService() {
        return service.getRepositoriesJson(currentPage);
    }
}
