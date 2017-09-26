package com.emersondms.githubchallenge.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emersondms.githubchallenge.R;
import com.emersondms.githubchallenge.view.fragment.PullRequestFragment;
import com.emersondms.githubchallenge.model.Repository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private FragmentManager manager;
    private List<Repository> results = null;
    private static final int NEW_ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public RepositoryAdapter(Context context, FragmentManager manager) {
        this.context = context;
        this.manager = manager;
        results = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case NEW_ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                viewHolder = new LoadingHolder(
                    inflater.inflate(R.layout.item_progress, parent, false));
                break;
        }

        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new RepositoryHolder(
            inflater.inflate(R.layout.single_repo_row, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Repository repository = results.get(position);
        int avatarIconSize = 150;

        switch (getItemViewType(position)) {
            case NEW_ITEM:
                RepositoryHolder repoHolder = (RepositoryHolder) holder;
                repoHolder.name.setText(repository.getName());
                repoHolder.description.setText(repository.getDescription());
                repoHolder.forks.setText(String.valueOf(repository.getForks()));
                repoHolder.stars.setText(String.valueOf(repository.getStars()));
                repoHolder.userName.setText(repository.getUser().getUserName());

                new Picasso.Builder(context)
                    .build()
                    .load(repository.getUser().getAvatar())
                    .resize(avatarIconSize, avatarIconSize)
                    .into(repoHolder.avatar);

                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == results.size() - 1 && isLoadingAdded) ? LOADING : NEW_ITEM;
    }

    private void add(Repository repository) {
        results.add(repository);
        notifyItemInserted(results.size() - 1);
    }

    public void addAll(List<Repository> repositories) {
        for (Repository repository : repositories) {
            add(repository);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Repository());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = results.size() - 1;
        Repository repository = getItem(position);

        if (repository != null) {
            results.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Repository getItem(int position) {
        return results.get(position);
    }

    class RepositoryHolder
        extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvName) TextView name;
        @BindView(R.id.tvDescription) TextView description;
        @BindView(R.id.tvForks) TextView forks;
        @BindView(R.id.tvStars) TextView stars;
        @BindView(R.id.ivAvatar) ImageView avatar;
        @BindView(R.id.tvUserName) TextView userName;

        RepositoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("user", String.valueOf(userName.getText()));
            bundle.putString("repository", String.valueOf(name.getText()));
            PullRequestFragment fragment = new PullRequestFragment();
            fragment.setArguments(bundle);

            manager
                .beginTransaction()
                .replace(R.id.recyclerContainer, fragment)
                .addToBackStack(null)
                .commit();
        }
    }
}