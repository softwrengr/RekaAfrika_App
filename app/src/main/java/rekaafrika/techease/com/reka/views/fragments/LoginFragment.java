package rekaafrika.techease.com.reka.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.interfaces.LoginInterface;
import rekaafrika.techease.com.reka.utilities.AlertUtils;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class LoginFragment extends Fragment {
    AlertDialog alertDialog;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_sign_up)
    TextView tvSignUp;
    View view;
    private boolean valid = false;
    private String strEmail;
    public static onOrderInterface mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        customActionBar();
        initUI();
        return view;

    }

    private void initUI() {
        ButterKnife.bind(this, view);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    apiCallLogin();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new CreateCustomerFragment());
            }
        });
    }

    private void apiCallLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_CUSTOMER_BY_EMAIL
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                if (response == null) {
                    Toast.makeText(getActivity(), "you got some error please try again later", Toast.LENGTH_SHORT).show();
                } else if (response.contains("true")) {

                    try {
                        alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject object = jsonObject.getJSONObject("data");
                        String userID = object.getString("id");
                        GeneralUtils.putBooleanValueInEditor(getActivity(), "isLogin", true);
                        GeneralUtils.putStringValueInEditor(getActivity(), "userID", userID);
                        GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new AddCartFragment());

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                } else {
                    Toast.makeText(getActivity(), "incorrect email", Toast.LENGTH_SHORT).show();
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
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", strEmail);
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
        strEmail = etEmail.getText().toString().trim();

        if (strEmail.isEmpty()) {
            etEmail.setError("please enter your email");
            valid = false;
        } else {
            etEmail.setError(null);
        }
        return valid;
    }

    //cutomAction Bar
    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Login");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();

    }

    // on OrderClick Interface
    public interface onOrderInterface {
        void orderPlaceInterface();
    }


}
