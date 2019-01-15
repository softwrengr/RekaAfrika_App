package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;

public class CategoriesFragment extends Fragment {
    View view;
    ArrayList<AllProductsModel> arrayList = new ArrayList<>();
    public static AllProductsModel model;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_categories, container, false);


//        Bundle bundle = this.getArguments();
//        arrayList = bundle.getStringArrayList("array");
        Log.d("array",String.valueOf(model.arrayList));
        return view;
    }
}
