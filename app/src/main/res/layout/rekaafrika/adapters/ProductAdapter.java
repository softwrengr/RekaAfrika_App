package com.techease.rekaafrika.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techease.rekaafrika.dateModels.AllProductsModel;

import java.util.ArrayList;

public class ProductAdapter  extends BaseAdapter {
    ArrayList<AllProductsModel> allProductsModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;

    public ProductAdapter(Context context, ArrayList<AllProductsModel> allProductsModelArrayList) {
        this.allProductsModelArrayList = allProductsModelArrayList;
        this.context = context;
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);

        }
    }

    @Override
    public int getCount() {
        if (allProductsModelArrayList != null) return allProductsModelArrayList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        final AllProductsModel model = allProductsModelArrayList.get(position);
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AllProductsModel model = allProductsModelArrayList.get(position);

        viewHolder = new MyViewHolder();
        convertView = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
        viewHolder.ivItem = convertView.findViewById(R.id.iv_product);
        viewHolder.tvPrice = convertView.findViewById(R.id.tv_item_price);
        viewHolder.layout_product = convertView.findViewById(R.id.product_layout);

        viewHolder.tvTitle.setText(model.getTitle());
        viewHolder.tvPrice.setText(model.getPrice());
        Picasso.get().load(model.getImage()).into(viewHolder.ivItem);

        viewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, model.getProduct_id(), Toast.LENGTH_SHORT).show();
            }
        });


        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder {
        ImageView ivItem;
        TextView tvTitle,tvPrice;
        FrameLayout layout_product;
    }
}
