package com.sugarac.sribbble.view.shot_list;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.sugarac.sribbble.R;
import com.sugarac.sribbble.model.Shot;
import com.sugarac.sribbble.utils.ModelUtils;
import com.sugarac.sribbble.view.shot_detail.ShotActivity;
import com.sugarac.sribbble.view.shot_detail.ShotFragment;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter {
    private List<Shot> data;

    public ShotListAdapter(@NonNull List<Shot> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shot, parent, false);
        return new ShotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Shot shot = data.get(position); //final

        ShotViewHolder shotViewHolder = (ShotViewHolder) holder;
        shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));
        shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        shotViewHolder.commentCount.setText(String.valueOf(shot.comments_count));
        shotViewHolder.image.setImageResource(R.drawable.shot_placeholder);

        shotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //display the shot after clicking
                Context context = holder.itemView.getContext(); //final
                Intent intent = new Intent(context, ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT,
                        ModelUtils.toString(shot, new TypeToken<Shot>(){}));
                intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
