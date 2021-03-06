package rekaafrika.techease.com.reka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.helpers.ShopCrud;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.CategoriesFragment;
import rekaafrika.techease.com.reka.views.fragments.ProductDetailsFragment;
import rekaafrika.techease.com.reka.views.fragments.ProductsFragment;

import static rekaafrika.techease.com.reka.views.fragments.PriceFilterFragment.strCurrency;

public class ProductAdapter extends BaseAdapter {
    ArrayList<AllProductsModel> allProductsModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;
    ShopCrud shopCrud;

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
        shopCrud = new ShopCrud(context);

        viewHolder = new MyViewHolder();
        convertView = layoutInflater.inflate(R.layout.custom_product_layout, parent, false);
        viewHolder.tvTitle = convertView.findViewById(R.id.tv_item_title);
        viewHolder.ivItem = convertView.findViewById(R.id.iv_item_view);
        viewHolder.tvPrice = convertView.findViewById(R.id.tv_item_price);
        viewHolder.layout_product = convertView.findViewById(R.id.item_layout);
        viewHolder.ivAddCart = convertView.findViewById(R.id.iv_item_add);
        viewHolder.ivCurrency = convertView.findViewById(R.id.iv_currency);

        switch (GeneralUtils.getCurrency(context)) {
            case "usd":
                viewHolder.ivCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.dollar));
                break;
            case "zar":
                viewHolder.ivCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.rand));
                break;
            case "euro":
                viewHolder.ivCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.euro));
                break;
            case "pound":
                viewHolder.ivCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.pound));
                break;
            case "pula":
                viewHolder.ivCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.pula));
                break;
        }

        final float currency = Float.parseFloat(model.getPrice()) * Float.parseFloat(GeneralUtils.getConvertedCurrency(context));

        viewHolder.tvTitle.setText(model.getTitle());
        viewHolder.tvPrice.setText(String.valueOf(currency));
        Glide.with(context).load(model.getImage()).into(viewHolder.ivItem);

        viewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.putStringValueInEditor(context, "product_id", model.getProduct_id());
                GeneralUtils.putStringValueInEditor(context, "item_price", String.valueOf(currency));
                GeneralUtils.connectDrawerFragmentWithBack(context, new ProductDetailsFragment());

            }
        });

        viewHolder.ivAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopCrud.insertSingleProduct(model.getProduct_id(), model.getTitle(), model.getImage(), model.getPrice(), "1");
            }
        });


        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder {
        ImageView ivItem, ivAddCart, ivCurrency;
        TextView tvTitle, tvPrice;
        RelativeLayout layout_product;
    }
}
