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

import java.util.List;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.CustomerOrderModel;
import rekaafrika.techease.com.reka.dateModels.OrderItemModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.activities.PaypalPaymentActivity;
import rekaafrika.techease.com.reka.views.fragments.PaymentMethodFragment;

import static rekaafrika.techease.com.reka.views.fragments.PaymentMethodFragment.totalPrice;

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
        final float currency = Float.parseFloat(model.getItemPrice()) * Float.parseFloat(GeneralUtils.getConvertedCurrency(context));

        viewHolder.tvPrice.setText(String.valueOf(currency));
        sumPrices(currency);

        switch (GeneralUtils.getCurrency(context)) {
            case "usd":
                PaymentMethodFragment.ivSubTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.dollar));
                PaymentMethodFragment.ivTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.dollar));
                viewHolder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.dollar));
                break;
            case "zar":
                PaymentMethodFragment.ivSubTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.rand));
                PaymentMethodFragment.ivTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.rand));
                viewHolder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.rand));
                break;
            case "euro":
                PaymentMethodFragment.ivSubTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.euro));
                PaymentMethodFragment.ivTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.euro));
                viewHolder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.euro));
                break;
            case "pound":
                PaymentMethodFragment.ivSubTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.pound));
                PaymentMethodFragment.ivTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.pound));
                viewHolder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.pound));
                break;
            case "pula":
                PaymentMethodFragment.ivSubTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.pula));
                PaymentMethodFragment.ivTotalSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.pula));
                viewHolder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.pula));
                break;
        }



    }

    @Override
    public int getItemCount() {
        return allProductsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView ivSymbol;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivSymbol = itemView.findViewById(R.id.iv_symbol);

        }
    }

    private void sumPrices(Float prices) {
        PaymentMethodFragment.totalPrice += prices;
        PaymentMethodFragment.tvSubTotal.setText(String.valueOf(totalPrice));
        PaymentMethodFragment.tvTotal.setText(String.valueOf(totalPrice));
        GeneralUtils.putStringValueInEditor(context, "sub_total", String.valueOf(totalPrice));
    }
}