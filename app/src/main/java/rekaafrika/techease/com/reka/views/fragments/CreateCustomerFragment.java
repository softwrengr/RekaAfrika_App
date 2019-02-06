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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;


public class CreateCustomerFragment extends Fragment {
    View view;
    AlertDialog alertDialog;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_create_user)
    Button btnCreateUser;
    private String strFirstName,strLastName,strEmail,strUsername,strPassword;
    private boolean valid = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_customer, container, false);
        initUI();
        return view;
    }

    private void initUI(){
        ButterKnife.bind(this,view);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    apiCallSignup();
                }
            }
        });
    }
    private void apiCallSignup(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREATE_CUSTOMER
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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
                params.put("email",strEmail);
                params.put("first_name",strFirstName);
                params.put("last_name",strLastName);
                params.put("username",strUsername);
                params.put("password",strPassword);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private boolean validate(){
        valid = true;
        strFirstName = etFirstName.getText().toString();
        strLastName = etLastName.getText().toString();
        strUsername = etUsername.getText().toString();
        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();

        if(strFirstName.isEmpty()){
            etFirstName.setError("please enter your first name");
            valid = false;
        }
        else {
            etFirstName.setError(null);
        }

        if(strLastName.isEmpty()){
            etLastName.setError("please enter your last name");
            valid = false;
        }
        else {
            etLastName.setError(null);
        }

        if(strEmail.isEmpty()){
            etEmail.setError("please enter your email");
            valid = false;
        }
        else {
            etEmail.setError(null);
        }

        if(strUsername.isEmpty()){
            etUsername.setError("please enter your username");
            valid = false;
        }
        else {
            etUsername.setError(null);
        }

        if(strPassword.isEmpty()){
            etPassword.setError("please enter your password");
            valid = false;
        }
        else {
            etPassword.setError(null);
        }
        return valid;
    }
}

