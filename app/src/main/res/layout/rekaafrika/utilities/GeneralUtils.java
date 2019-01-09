package com.techease.rekaafrika.utilities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.techease.rekaafrika.R;

public class GeneralUtils {


    public static Fragment connectFragment(Activity activity,Fragment fragment){
        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        return fragment;
    }
}
