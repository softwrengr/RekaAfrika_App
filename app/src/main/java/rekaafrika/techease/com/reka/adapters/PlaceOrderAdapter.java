package rekaafrika.techease.com.reka.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.CustomerOrderModel;
import rekaafrika.techease.com.reka.dateModels.OrderItemModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.views.activities.PaypalPaymentActivity;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.MyViewHolder> {
    List<OrderItemModel> allProductsModelArrayList;
    Context context;

    public PlaceOrderAdapter(Activity context, List<OrderItemModel> allProductsModelArrayList) {
        this.context = context;
        this.allProductsModelArrayList = allProductsModelArrayList;
    }


    @NonNull
    @Override
    public PlaceOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_placeorder_layout, parent, false);

        return new PlaceOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        final OrderItemModel model = allProductsModelArrayList.get(position);
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.size();
        return 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final PlaceOrderAdapter.MyViewHolder viewHolder, final int position) {
        final OrderItemModel model = allProductsModelArrayList.get(position);

        viewHolder.tvName.setText(model.getItemName());
        viewHolder.tvPrice.setText(model.getItemPrice());



    }

    @Override
    public int getItemCount() {
        return allProductsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);

        }
    }
}