package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CategoryAdapter;
import rekaafrika.techease.com.reka.dateModels.CategoryDataModel;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class PriceFilterFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.layout_dollar)
    LinearLayout layoutDollar;
    @BindView(R.id.layout_rand)
    LinearLayout layoutRand;
    @BindView(R.id.layout_euro)
    LinearLayout layoutEuro;
    @BindView(R.id.layout_pound)
    LinearLayout layoutPound;
    @BindView(R.id.layout_pula)
    LinearLayout layoutPula;
    public static String strCurrency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_price_filter, container, false);
        customActionBar();
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);

        layoutDollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                GeneralUtils.putStringValueInEditor(getActivity(),"currency","usd");
                apiCallCurrency("ZAR_USD");
            }
        });

        layoutRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                alertDialog = AlertUtils.createProgressDialog(getActivity());
//                alertDialog.show();
                GeneralUtils.putStringValueInEditor(getActivity(),"currency","rand");
                //apiCallCurrency("ZAR_ZAR");
                strCurrency = "1";
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(),new ProductsFragment());
            }
        });

        layoutEuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                GeneralUtils.putStringValueInEditor(getActivity(),"currency","euro");
                apiCallCurrency("ZAR_EUR");
            }
        });

        layoutPound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                GeneralUtils.putStringValueInEditor(getActivity(),"currency","pound");
                apiCallCurrency("ZAR_GBP");
            }
        });

        layoutPula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = AlertUtils.createProgressDialog(getActivity());
                alertDialog.show();
                GeneralUtils.putStringValueInEditor(getActivity(),"currency","pula");
                apiCallCurrency("ZAR_BWP");
            }
        });
    }

    private void apiCallCurrency(final String currency) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.CurrencyConverterUrl + currency + "&compact=y&apiKey=" + Config.CurrencyToken
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = jsonObject.getJSONObject(currency);
                    strCurrency = object.getString("val");
                    GeneralUtils.connectDrawerFragmentWithBack(getActivity(),new ProductsFragment());

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

    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Select Currency");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }
}
