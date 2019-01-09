package rekaafrika.techease.com.reka.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import rekaafrika.techease.com.reka.R;


public class GeneralUtils {


    public static Fragment connectFragment(Context activity, Fragment fragment){
        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        return fragment;
    }
}
