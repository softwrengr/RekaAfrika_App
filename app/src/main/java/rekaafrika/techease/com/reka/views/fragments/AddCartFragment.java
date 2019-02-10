package rekaafrika.techease.com.reka.views.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import rekaafrika.techease.com.reka.utilities.GeneralUtils;


public class AddCartFragment extends Fragment {
    @BindView(R.id.rv_cart)
    RecyclerView rvCart;
    // @BindView(R.id.sub_total_price)
    public static TextView tvSubTotalPrice;
    // @BindView(R.id.sub_total_items_count)
    public static TextView tvSubTotalItemsCount;
    ArrayList<CartModel> productsModelArrayList;
    ShopCrud shopCrud;
    View view;
    @BindView(R.id.checkout)
    TextView tvOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_cart, container, false);
        tvSubTotalPrice = view.findViewById(R.id.sub_total_price);
        tvSubTotalItemsCount = view.findViewById(R.id.sub_total_items_count);
        getActivity().setTitle("My Bag");
        shopCrud = new ShopCrud(getActivity());
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvCart.setLayoutManager(mLayoutManagerReviews);
        productsModelArrayList = new ArrayList<>();
        showProducts();

        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }


    public void showProducts() {

        Cursor cursor = shopCrud.getProducts();
        while (cursor.moveToNext()) {
            String strProductID = cursor.getString(1);
            String strProductName = cursor.getString(2);
            String strProductImage = cursor.getString(3);
            String strProductPrice = cursor.getString(4);
            String strProductQuantity = cursor.getString(5);

            readAllProducts(strProductID, strProductName, strProductImage, strProductPrice,strProductQuantity);

        }
    }

    private void readAllProducts(String strProductID, String strProductName, String strProductImage, String strProductPrice,String strQuantity) {

        CartModel cartModel = new CartModel();
        cartModel.setId(strProductID);
        cartModel.setItem_name(strProductName);
        cartModel.setItem_image(strProductImage);
        cartModel.setItem_price(strProductPrice);
        cartModel.setItem_quantity(strQuantity);

        Toast.makeText(getActivity(), strProductID, Toast.LENGTH_SHORT).show();

        productsModelArrayList.add(cartModel);
        Set<CartModel> set = new HashSet<>(productsModelArrayList);
        productsModelArrayList.clear();
        productsModelArrayList.addAll(set);

//        Collections.sort(productsModelArrayList, new Comparator<CartModel>(){
//            public int compare(CartModel obj1, CartModel obj2) {
//                return obj1.getItem_name().compareToIgnoreCase(obj2.getItem_name());
//            }
//        });

        CartAdapter adapter = new CartAdapter(getActivity(), productsModelArrayList);
        rvCart.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Login").setMessage("you must be logged in first to continue.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GeneralUtils.connectDrawerFragmentWithBack(getActivity(), new LoginFragment());
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }
}
