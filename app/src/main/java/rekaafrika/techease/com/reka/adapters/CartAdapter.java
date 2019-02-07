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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.AddCartFragment;
import rekaafrika.techease.com.reka.views.fragments.LoginFragment;
import rekaafrika.techease.com.reka.views.fragments.ProductDetailsFragment;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    List<CartModel> allProductsModelArrayList;
    Context context;
    int singleQuantity;
    float  productPrice, totalPrice;

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
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int position) {
        final CartModel model = allProductsModelArrayList.get(position);

        viewHolder.tvTitle.setText(model.getItem_name());
        viewHolder.tvPrice.setText("$"+model.getItem_price());
        Picasso.get().load(model.getItem_image()).into(viewHolder.ivItem);
        AddCartFragment.tvSubTotalItemsCount.setText(String.valueOf(position+1)+" Products");

        viewHolder.ivAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productPrice = Float.parseFloat(model.getItem_price());
                singleQuantity = Integer.parseInt(viewHolder.tvQuantity.getText().toString());
                if(singleQuantity>=0){
                    singleQuantity++;
                    viewHolder.tvQuantity.setText(String.valueOf(singleQuantity));
                    totalPrice = productPrice * singleQuantity;
                    viewHolder.tvPrice.setText(String.valueOf(totalPrice));
                    Float inrementedValue = Float.parseFloat( viewHolder.tvPrice.getText().toString())-Float.parseFloat(model.getItem_price());
                    GeneralUtils.putStringValueInEditor(context,"value",String.valueOf(inrementedValue));
                    sumPrices(allProductsModelArrayList.size(),inrementedValue);
                }
                else if(singleQuantity<0){
                    singleQuantity=0;
                    singleQuantity++;
                    viewHolder.tvQuantity.setText(String.valueOf(singleQuantity));
                    totalPrice = productPrice * singleQuantity;
                    viewHolder.tvPrice.setText(String.valueOf(totalPrice));
                    Float decrementedValue = Float.parseFloat( viewHolder.tvPrice.getText().toString());
                    Toast.makeText(context, String.valueOf(decrementedValue), Toast.LENGTH_SHORT).show();
                    sumPrices(allProductsModelArrayList.size(),decrementedValue);
                }

            }
        });

        viewHolder.ivRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleQuantity--;
                if(singleQuantity==1){
                    viewHolder.tvQuantity.setText("1");
                    viewHolder.tvPrice.setText(String.valueOf(productPrice));
                }
                else if(singleQuantity>=1) {
                    viewHolder.tvQuantity.setText(String.valueOf(singleQuantity));
                    totalPrice = productPrice * singleQuantity;
                    viewHolder.tvPrice.setText(String.valueOf(totalPrice));
                }

            }
        });

        sumPrices(allProductsModelArrayList.size(),totalPrice);

    }

    @Override
    public int getItemCount() {
        return allProductsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;
        TextView tvTitle, tvPrice,tvQuantity;
        RelativeLayout layout_product;
        ImageView ivAddQuantity,ivRemoveQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            ivItem = itemView.findViewById(R.id.iv_item_view);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            layout_product = itemView.findViewById(R.id.item_layout);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            ivAddQuantity = itemView.findViewById(R.id.iv_add_quantity);
            ivRemoveQuantity = itemView.findViewById(R.id.iv_remove_quantity);

        }
    }

    private void sumPrices(int size,float total){
        float totalPrice=0;
        try{
            for (int i = 0; i<size; i++)
            {
                float subTotal =Float.parseFloat(allProductsModelArrayList.get(i).getItem_price());
                totalPrice += subTotal;
            }

            AddCartFragment.tvSubTotalPrice.setText(String.valueOf(totalPrice+Float.parseFloat(GeneralUtils.getValue(context))));
        }catch(NumberFormatException ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
