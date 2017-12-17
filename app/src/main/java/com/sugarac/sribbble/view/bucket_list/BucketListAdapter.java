package com.sugarac.sribbble.view.bucket_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugarac.sribbble.R;
import com.sugarac.sribbble.model.Bucket;
import com.sugarac.sribbble.view.base.BaseViewHolder;

import java.text.MessageFormat;
import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_BUCKET = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Bucket> data;
    private LoadMoreListener loadMoreListener;
    private boolean showLoading;

    public BucketListAdapter(@NonNull List<Bucket> data,
                             @NonNull LoadMoreListener loadMoreListener) {
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bucket, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        } else {
            Bucket bucket = data.get(position);

            // 0 -> 0 shot
            // 1 -> 1 shot
            // 2 -> 2 shots
            String bucketShotCountString = MessageFormat.format(
                    holder.itemView.getContext().getResources().getString(R.string.shot_count),
                    bucket.shots_count);

            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            bucketViewHolder.bucketName.setText(bucket.name);
            bucketViewHolder.bucketShotCount.setText(bucketShotCountString);

            // in this branch, our bucket list only shows up in MainActivity
            // therefore we don't need the checkbox just yet
            // we only need checkbox in ChooseBucketActivity, which we'll add in later branch
            bucketViewHolder.bucketChosen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public int getDataCount() {
        return data.size();
    }

    public void append(@NonNull List<Bucket> moreBuckets) {
        data.addAll(moreBuckets);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }
}
