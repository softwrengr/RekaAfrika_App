package rekaafrika.techease.com.reka.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.utilities.Config;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;

public class PaypalPaymentActivity extends AppCompatActivity {
    private static PayPalConfiguration config;
    private String paymentID;
    private String paymentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment);

        configPaypal();
        makePaypalPayment();
    }

    private void configPaypal() {
        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId("<YOUR_CLIENT_ID>");

    }

    private void makePaypalPayment() {

        Intent start = new Intent(this, PayPalService.class);
        start.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(start);

        paymentAmount = GeneralUtils.getTotalPrice(this);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "Rekaafrika",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(PaypalPaymentActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    //Getting the payment details
                    String paymentDetails = confirm.toJSONObject().toString(4);
                    Log.i("paymentExample", paymentDetails);

                    JSONObject jsonDetails = new JSONObject(paymentDetails);
                    paymentID = jsonDetails.getJSONObject("response").getString("id");
                    if (paymentID != null) {
                        sendPaymentDetails();
                    } else {
                        Log.i("paymentExample", "ID is null");
                    }
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private void sendPaymentDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PAYMENT
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {

                } else {
                    Toast.makeText(PaypalPaymentActivity.this, "could not complete your request this time", Toast.LENGTH_SHORT).show();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaypalPaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("payment_id", "");
                params.put("payment_title", "paypal");
                params.put("order_id", "14185");
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(PaypalPaymentActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
