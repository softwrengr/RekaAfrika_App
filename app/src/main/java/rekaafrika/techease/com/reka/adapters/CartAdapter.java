package rekaafrika.techease.com.reka.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.dateModels.CalculationModel;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.AddCartFragment;
import rekaafrika.techease.com.reka.views.fragments.LoginFragment;
import rekaafrika.techease.com.reka.views.fragments.ProductDetailsFragment;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    List<CartModel> allProductsModelArrayList;
    Context context;
    ShopCrud shopCrud;

    public CartAdapter(Activity context, List<CartModel> allProductsModelArrayList) {
        this.context = context;
        this.allProductsModelArrayList = allProductsModelArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        final CartModel model = allProductsModelArrayList.get(position);
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.size();
        return 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int position) {
        final CartModel model = allProductsModelArrayList.get(position);
        shopCrud = new ShopCrud(context);

        viewHolder.tvTitle.setText(model.getItem_name());
        viewHolder.tvPrice.setText(model.getItem_price());
        viewHolder.tvQuantity.setText(model.getItem_quantity());
        Glide.with(context).load(model.getItem_image()).into(viewHolder.ivItem);
        AddCartFragment.tvSubTotalItemsCount.setText(String.valueOf(position + 1) + " Products");

        switch (GeneralUtils.getCurrency(context)) {
            case "usd":
                viewHolder.ivCartCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.dollar));
                break;
            case "rand":
                viewHolder.ivCartCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.rand));
                break;
            case "euro":
                viewHolder.ivCartCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.euro));
                break;
            case "pound":
                viewHolder.ivCartCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.pound));
                break;
            case "pula":
                viewHolder.ivCartCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.pula));
                break;
        }


        viewHolder.tvDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopCrud.delete(model.getId());
                GeneralUtils.connectDrawerFragmentWithBack(context, new AddCartFragment());
            }
        });

        sumPrices();

    }

    @Override
    public int getItemCount() {
        return allProductsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem, ivCartCurrency;
        TextView tvTitle, tvPrice, tvQuantity, tvDeleteItem;
        RelativeLayout layout_product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            ivItem = itemView.findViewById(R.id.iv_item_view);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            layout_product = itemView.findViewById(R.id.item_layout);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvDeleteItem = itemView.findViewById(R.id.delete_item);
            ivCartCurrency = itemView.findViewById(R.id.iv_cart_currency);

        }
    }

    private void sumPrices() {
        float totalPrice = 0;
        try {
            for (int i = 0; i < allProductsModelArrayList.size(); i++) {
                float subTotal = Float.parseFloat(allProductsModelArrayList.get(i).getItem_price());
                totalPrice += subTotal;
            }
            AddCartFragment.tvSubTotalPrice.setText(String.valueOf(totalPrice));

        } catch (NumberFormatException ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
