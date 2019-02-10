package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class ShippingAddressFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    @BindView(R.id.et_shipping_first_name)
    EditText etFirstName;
    @BindView(R.id.et_shipping_last_name)
    EditText etLastName;
    @BindView(R.id.et_shipping_address_one)
    EditText etAddressOne;
    @BindView(R.id.et_shipping_address_two)
    EditText etAddressTwo;
    @BindView(R.id.et_shipping_city)
    EditText etCity;
    @BindView(R.id.et_shipping_postcode)
    EditText etPostCode;
    @BindView(R.id.et_shipping_company)
    EditText etCompany;
    @BindView(R.id.et_shipping_country)
    EditText etCountry;
    @BindView(R.id.et_shipping_state)
    EditText etState;
    @BindView(R.id.btn_shipping_address)
    Button btnShippingAddress;

    private String strFirstName, strLastName, strAddressOne, strAddressTwo, strCity, strPostCode, strCompany, strCountry, strState;
    Boolean valid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shipping_address, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        btnShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    apiCallShippingAddress();
                }
            }
        });

    }

    private void apiCallShippingAddress() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SHIPPING_ADDRESS
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                if (response.contains("true")) {
                    Toast.makeText(getActivity(), "Billing Address Updated", Toast.LENGTH_SHORT).show();
                    GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new LoginFragment());
                } else {
                    Toast.makeText(getActivity(), "you got some error please try again", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", GeneralUtils.getUserID(getActivity()));
                params.put("first_name", strFirstName);
                params.put("last_name", strLastName);
                params.put("company", strCompany);
                params.put("address_1", strAddressOne);
                params.put("address_2", strAddressTwo);
                params.put("city", strCity);
                params.put("state", strState);
                params.put("postcode", strPostCode);
                params.put("country", strCountry);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }


    private boolean validate() {
        valid = true;
        strFirstName = etFirstName.getText().toString().trim();
        strLastName = etLastName.getText().toString().trim();
        strAddressOne = etAddressOne.getText().toString().trim();
        strAddressTwo = etAddressTwo.getText().toString().trim();
        strCity = etCity.getText().toString().trim();
        strPostCode = etPostCode.getText().toString().trim();
        strCompany = etCompany.getText().toString().trim();
        strCountry = etCountry.getText().toString().trim();
        strState = etState.getText().toString().trim();

        if (strFirstName.isEmpty()) {
            etFirstName.setError("please enter your first name");
            valid = false;
        } else {
            etFirstName.setError(null);
        }

        if (strLastName.isEmpty()) {
            etLastName.setError("please enter your last name");
            valid = false;
        } else {
            etLastName.setError(null);
        }

        if (strAddressOne.isEmpty()) {
            etAddressOne.setError("please enter your address");
            valid = false;
        } else {
            etAddressOne.setError(null);
        }

        if (strAddressTwo.isEmpty()) {
            etAddressTwo.setError("please enter your address");
            valid = false;
        } else {
            etAddressTwo.setError(null);
        }

        if (strCity.isEmpty()) {
            etCity.setError("please enter your city name");
            valid = false;
        } else {
            etCity.setError(null);
        }

        if (strPostCode.isEmpty()) {
            etPostCode.setError("please enter postal code");
            valid = false;
        } else {
            etPostCode.setError(null);
        }

        if (strCompany.isEmpty()) {
            etCompany.setError("please enter company name");
            valid = false;
        } else {
            etCompany.setError(null);
        }

        if (strCountry.isEmpty()) {
            etCountry.setError("please enter country name");
            valid = false;
        } else {
            etCountry.setError(null);
        }

        if (strState.isEmpty()) {
            etState.setError("please enter state name");
            valid = false;
        } else {
            etState.setError(null);
        }
        return valid;
    }
}
