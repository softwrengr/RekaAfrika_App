package rekaafrika.techease.com.reka.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import rekaafrika.techease.com.reka.R;


public class GeneralUtils {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static Fragment connectFragment(Context activity, Fragment fragment){
        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        return fragment;
    }
    public static Fragment connectFragmentWithBack(Context activity, Fragment fragment){
        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("true").commit();
        return fragment;
    }

    public static Fragment connectDrawerFragment(Context activity, Fragment fragment){
        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container,fragment).commit();
        return fragment;
    }

    public static Fragment connectDrawerFragmentWithBack(Context activity, Fragment fragment){
        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container,fragment).addToBackStack("").commit();
        return fragment;
    }


    public static SharedPreferences.Editor putStringValueInEditor(Context context, String key, String value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putBooleanValueInEditor(Context context, String key, boolean value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
        return editor;
    }



    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("afrika", 0);
    }


    public static String getID(Context context) {
        return getSharedPreferences(context).getString("product_id", "");
    }

    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString("userID", "");
    }

    public static String getSlug(Context context) {
        return getSharedPreferences(context).getString("slug", "");
    }

    public static String getPosition(Context context) {
        return getSharedPreferences(context).getString("position", "0");
    }

    public static String getSubTotalPrice(Context context) {
        return getSharedPreferences(context).getString("subtotal", "0");
    }

    public static String getOrderID(Context context) {
        return getSharedPreferences(context).getString("order_id", "0");
    }

    public static String getQuantity(Context context) {
        return getSharedPreferences(context).getString("quantity", "0");
    }

    public static String getPaymentType(Context context) {
        return getSharedPreferences(context).getString("payment_type", "0");
    }

    public static String getCurrency(Context context) {
        return getSharedPreferences(context).getString("currency", "zar");
    }

    public static String getConvertedCurrency(Context context) {
        return getSharedPreferences(context).getString("converted_currency", "1");
    }

    public static String getItemPrice(Context context) {
        return getSharedPreferences(context).getString("item_price", "zar");
    }

    public static boolean isLogin(Context context){
        return getSharedPreferences(context).getBoolean("isLogin",false);
    }
}
