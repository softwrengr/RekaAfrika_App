package rekaafrika.techease.com.reka.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.dateModels.CartModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.ProductDetailsFragment;

public class CartAdapter  extends BaseAdapter {
    ArrayList<CartModel> allProductsModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    CartAdapter.MyViewHolder viewHolder = null;
    ArrayList<String> arrayList = new ArrayList<>();

    public CartAdapter(Context context, ArrayList<CartModel> allProductsModelArrayList) {
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
        final CartModel model = allProductsModelArrayList.get(position);
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CartModel model = allProductsModelArrayList.get(position);

        viewHolder = new CartAdapter.MyViewHolder();
        convertView = layoutInflater.inflate(R.layout.custome_item_layout, parent, false);
        viewHolder.tvTitle = convertView.findViewById(R.id.tv_item_title);
        viewHolder.ivItem = convertView.findViewById(R.id.iv_item_view);
        viewHolder.tvPrice = convertView.findViewById(R.id.tv_item_price);
        viewHolder.layout_product = convertView.findViewById(R.id.item_layout);


        viewHolder.tvTitle.setText(model.getItem_name());
        viewHolder.tvPrice.setText(model.getItem_price());
        Picasso.get().load(model.getItem_image()).into(viewHolder.ivItem);



        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder {
        ImageView ivItem;
        TextView tvTitle,tvPrice;
        RelativeLayout layout_product;
    }
}
