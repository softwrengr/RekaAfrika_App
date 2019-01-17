package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CategoryItemAdapter;
import rekaafrika.techease.com.reka.dateModels.CategoryItemModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class CategoriesFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.category_bags)
    RelativeLayout layoutBags;
    @BindView(R.id.category_fashion)
    RelativeLayout layoutFashion;
    @BindView(R.id.category_homeware)
    RelativeLayout layoutHomeware;
    @BindView(R.id.tv_category_bags)
    TextView tvBags;
    @BindView(R.id.tv_category_fashion)
    TextView tvFashion;
    @BindView(R.id.tv_category_homeware)
    TextView tvHomeware;
    @BindView(R.id.iv_bag)
    ImageView ivBag;
    @BindView(R.id.iv_fashion)
    ImageView ivFashion;
    @BindView(R.id.iv_homeware)
    ImageView ivHomeware;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        getActivity().setTitle("Categories");
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        bundle = new Bundle();

        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getCategoryItem();

        layoutBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("no", 1);
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new CategoryItemsFragment()).setArguments(bundle);
            }
        });
        layoutFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("no", 2);
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new CategoryItemsFragment()).setArguments(bundle);
            }
        });
        layoutHomeware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("no", 4);
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new CategoryItemsFragment()).setArguments(bundle);
            }
        });
    }

    private void getCategoryItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.CATEGORIES
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONObject objectBag = jsonArray.getJSONObject(1);
                    JSONObject objectFashion = jsonArray.getJSONObject(2);
                    JSONObject objectHomeware = jsonArray.getJSONObject(4);

                    String strBag = objectBag.getString("name");
                    String strFashion = objectFashion.getString("name");
                    String strHomeware = objectHomeware.getString("name");

                    tvBags.setText(strBag);
                    tvFashion.setText(strFashion);
                    tvHomeware.setText(strHomeware);
                    Picasso.get().load(objectBag.getString("image")).into(ivBag);
                    Picasso.get().load(objectHomeware.getString("image")).into(ivFashion);
                    Picasso.get().load(objectHomeware.getString("image")).into(ivHomeware);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
