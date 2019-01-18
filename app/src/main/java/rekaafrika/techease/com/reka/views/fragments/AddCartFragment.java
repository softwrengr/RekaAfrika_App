package rekaafrika.techease.com.reka.views.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.adapters.CartAdapter;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;


public class AddCartFragment extends Fragment {
    @BindView(R.id.gv_cart)
    GridView gvCart;
    ArrayList<CartModel> productsModelArrayList;
    ShopCrud shopCrud;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_cart, container, false);
        getActivity().setTitle("Cart");
        shopCrud = new ShopCrud(getActivity());
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        productsModelArrayList = new ArrayList<>();
        showProducts();
    }


    private void showProducts() {

        Cursor cursor = shopCrud.getProducts();
        while (cursor.moveToNext()) {
            String strProductID = cursor.getString(1);
            String strProductName = cursor.getString(2);
            String strProductImage = cursor.getString(3);
            String strProductPrice = cursor.getString(4);

            readAllProducts(strProductID, strProductName, strProductImage, strProductPrice);

        }
    }

    private void readAllProducts(String strProductID, String strProductName, String strProductImage, String strProductPrice) {

        CartModel cartModel = new CartModel();
        cartModel.setId(strProductID);
        cartModel.setItem_name(strProductName);
        cartModel.setItem_image(strProductImage);
        cartModel.setItem_price(strProductPrice);

        productsModelArrayList.add(cartModel);
        Set<CartModel> set = new HashSet<>(productsModelArrayList);
        productsModelArrayList.clear();
        productsModelArrayList.addAll(set);


        CartAdapter adapter = new CartAdapter(getActivity(), productsModelArrayList);
        gvCart.setAdapter(adapter);
    }


}
