package rekaafrika.techease.com.reka.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this,NavigationDrawerActivity.class));
    }
}
