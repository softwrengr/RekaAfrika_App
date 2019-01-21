package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CategoryItemAdapter;
import rekaafrika.techease.com.reka.adapters.ProductAdapter;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.dateModels.CategoryItemModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;


public class CategoryItemsFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    GridView gvCategoryItems;
    ArrayList<CategoryItemModel> categoryItemModelArrayList;
    CategoryItemAdapter categoryItemAdapter;
    View view;
    int categoryPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_items, container, false);
        getActivity().setTitle("Sub categories");
        gvCategoryItems = view.findViewById(R.id.gv_category_item);
        initUI();
        return view;
    }

    private void initUI() {

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            categoryPosition = bundle.getInt("no");
        }

        categoryItemModelArrayList = new ArrayList<>();
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getCategoryItem();
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
                    JSONObject object = jsonArray.getJSONObject(categoryPosition);
                    JSONArray array = object.getJSONArray("sub_categories");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject itemObject = array.getJSONObject(i);

                        String strProductID = itemObject.getString("id");
                        String strTitle = itemObject.getString("name");
                        String strSlug = itemObject.getString("slug");
                        String strImage = itemObject.getString("image");


                        CategoryItemModel model = new CategoryItemModel();
                        model.setProduct_id(strProductID);
                        model.setName(strTitle);
                        model.setSlug(strSlug);
                        model.setImage(strImage);

                        categoryItemModelArrayList.add(model);
                        categoryItemAdapter = new CategoryItemAdapter(getActivity(), categoryItemModelArrayList);
                        gvCategoryItems.setAdapter(categoryItemAdapter);
                        categoryItemAdapter.notifyDataSetChanged();
                    }


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


            //            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("category","Local Non-Fiction");
//                return params;
//            }

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
