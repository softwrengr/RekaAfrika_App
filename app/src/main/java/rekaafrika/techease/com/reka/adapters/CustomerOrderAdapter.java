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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.dateModels.CustomerOrderModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.activities.PaypalPaymentActivity;
import rekaafrika.techease.com.reka.views.fragments.AddCartFragment;
import rekaafrika.techease.com.reka.views.fragments.PaymentMethodFragment;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.MyViewHolder> {
    List<CustomerOrderModel> allProductsModelArrayList;
    Context context;
    ShopCrud shopCrud;

    public CustomerOrderAdapter(Activity context, List<CustomerOrderModel> allProductsModelArrayList) {
        this.context = context;
        this.allProductsModelArrayList = allProductsModelArrayList;
    }


    @NonNull
    @Override
    public CustomerOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_order_layout, parent, false);

        return new CustomerOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        final CustomerOrderModel model = allProductsModelArrayList.get(position);
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.size();
        return 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final CustomerOrderAdapter.MyViewHolder viewHolder, final int position) {
        final CustomerOrderModel model = allProductsModelArrayList.get(position);

        viewHolder.tvOrderID.setText("Order ID "+model.getOrderID());
        viewHolder.tvOrderNo.setText("Oder No "+model.getOrderNumber());
        viewHolder.tvOrderStatus.setText("Order Status "+model.getOrderStatus());
        viewHolder.tvOrderTotal.setText("Total "+model.getOrderTotal());

        viewHolder.layout_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.putStringValueInEditor(context,"position",String.valueOf(position));
                GeneralUtils.connectDrawerFragmentWithBack(context,new PaymentMethodFragment());
            }
        });


    }

    @Override
    public int getItemCount() {
        return allProductsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderID, tvOrderNo, tvOrderStatus,tvOrderTotal;
        RelativeLayout layout_order;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderID = itemView.findViewById(R.id.tv_order_id);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            layout_order = itemView.findViewById(R.id.order_layout);

        }
    }



}

