package com.techease.rekaafrika.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.techease.rekaafrika.R;
import com.techease.rekaafrika.utilities.GeneralUtils;
import com.techease.rekaafrika.views.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeneralUtils.connectFragment(this,new HomeFragment());
    }
}
