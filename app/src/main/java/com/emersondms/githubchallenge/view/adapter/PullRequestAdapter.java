package com.emersondms.githubchallenge.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emersondms.githubchallenge.R;
import com.emersondms.githubchallenge.model.PullRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PullRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String user, repository;
    private List<PullRequest> results = null;
    private static final int NEW_ITEM = 0;
    private static final int LOADING = 1;
    private PullRequest pullRequest;
    private boolean isLoadingAdded = false;

    public PullRequestAdapter(Context context, String user, String repository) {
        this.context = context;
        this.user = user;
        this.repository = repository;
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
        viewHolder = new PullRequestHolder(
            inflater.inflate(R.layout.single_pullreq_row, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        pullRequest = results.get(position);
        int avatarIconSize = 100;

        switch (getItemViewType(position)) {
            case NEW_ITEM:
                PullRequestHolder pullReqHolder = (PullRequestHolder) holder;
                pullReqHolder.title.setText(pullRequest.getTitle());
                pullReqHolder.date.setText(pullRequest.getDate());
                pullReqHolder.body.setText(pullRequest.getBody());
                pullReqHolder.author.setText(pullRequest.getUser().getUserName());

                new Picasso.Builder(context)
                    .build()
                    .load(pullRequest.getUser().getAvatar())
                    .resize(avatarIconSize, avatarIconSize)
                    .into(pullReqHolder.avatar);

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

    private void add(PullRequest pullRequest) {
        results.add(pullRequest);
        notifyItemInserted(results.size() - 1);
    }

    public void addAll(List<PullRequest> pullRequests) {
        for (PullRequest pullRequest : pullRequests) {
            add(pullRequest);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PullRequest());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = results.size() - 1;
        PullRequest pullRequest = getItem(position);

        if (pullRequest != null) {
            results.remove(position);
            notifyItemRemoved(position);
        }
    }

    private PullRequest getItem(int position) {
        return results.get(position);
    }

    class PullRequestHolder
        extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvTitle) TextView title;
        @BindView(R.id.tvDate) TextView date;
        @BindView(R.id.tvBody) TextView body;
        @BindView(R.id.ivAuthorAvatar) ImageView avatar;
        @BindView(R.id.tvAuthorName) TextView author;

        PullRequestHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(Locale.getDefault(),
                    "https://github.com/%s/%s/pull/%d",
                    user, repository, pullRequest.getNumber()
                ))
            ));
        }
    }
}