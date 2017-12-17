package com.sugarac.sribbble.view.shot_list;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;
import com.sugarac.sribbble.R;
import com.sugarac.sribbble.model.Shot;
import com.sugarac.sribbble.utils.ModelUtils;
import com.sugarac.sribbble.view.shot_detail.ShotActivity;
import com.sugarac.sribbble.view.shot_detail.ShotFragment;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_SHOT = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<Shot> data;
    private LoadMoreListener loadMoreListener;
    private boolean showLoading;

    public ShotListAdapter(@NonNull List<Shot> data, LoadMoreListener loadMoreListener) {
        this.data = data;
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SHOT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shot, parent, false);
            return new ShotViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_loading, parent, false);
            return new RecyclerView.ViewHolder(view) {
            }; //anonymous class, no need to create ViewHolder for animation
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShotViewHolder) {
            final Shot shot = data.get(position); //final

            ShotViewHolder shotViewHolder = (ShotViewHolder) holder;
            shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));
            shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
            shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
            shotViewHolder.commentCount.setText(String.valueOf(shot.comments_count));
            shotViewHolder.image.setImageResource(R.drawable.shot_placeholder);

            // play gif automatically
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(shot.getImageUrl()))
                    .setAutoPlayAnimations(true)
                    .build();
            shotViewHolder.image.setController(controller);

            shotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //display the shot after clicking
                    Context context = holder.itemView.getContext(); //final
                    Intent intent = new Intent(context, ShotActivity.class);
                    intent.putExtra(ShotFragment.KEY_SHOT,
                            ModelUtils.toString(shot, new TypeToken<Shot>() {
                            }));
                    intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
                    context.startActivity(intent);
                }
            });
        } else {
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
//        return data.size() + 1; //show loading animation
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) { //necessary to override, for RV to get viewType
        return position < data.size()
                ? VIEW_TYPE_SHOT
                : VIEW_TYPE_LOADING;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public void append(@NonNull List<Shot> moreShots) { //append more shots after onLoadMore
        data.addAll(moreShots);
        notifyDataSetChanged(); //redraw UI, e.g. 21 -> 40
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return data.size();
    }
}
