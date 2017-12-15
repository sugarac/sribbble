package com.sugarac.sribbble.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sugarac.sribbble.R;
import com.sugarac.sribbble.view.shot_list.ShotListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) { //for not adding fragment twice
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance())
                    .commit();
        }
    }
}
