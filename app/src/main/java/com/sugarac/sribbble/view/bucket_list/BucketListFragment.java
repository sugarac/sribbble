package com.sugarac.sribbble.view.bucket_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.JsonSyntaxException;
import com.sugarac.sribbble.R;
import com.sugarac.sribbble.dribbble.Dribbble;
import com.sugarac.sribbble.model.Bucket;
import com.sugarac.sribbble.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BucketListFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    BucketListAdapter adapter;

    public static BucketListFragment newInstance() {
        return new BucketListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fab_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new BucketListAdapter(new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(
                        new LoadBucketTask(adapter.getDataCount() / Dribbble.COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Try replacing the root layout of R.layout.fragment_fab_recycler_view with
                // FragmentLayout to see what Snackbar looks like
                Snackbar.make(v, "Fab clicked", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private class LoadBucketTask extends AsyncTask<Void, Void, List<Bucket>> {
        int page;

        public LoadBucketTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            // this method is executed on non-UI thread
            try {
                return Dribbble.getUserBuckets(page);
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            // this method is executed on UI thread!!!!
            if (buckets != null) {
                adapter.append(buckets);
                adapter.setShowLoading(buckets.size() == Dribbble.COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
//    private List<Bucket> fakeData() {
//        List<Bucket> bucketList = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < 20; ++i) {
//            Bucket bucket = new Bucket();
//            bucket.name = "Bucket" + i;
//            bucket.shots_count = random.nextInt(10);
//            bucketList.add(bucket);
//        }
//        return bucketList;
//    }
}
