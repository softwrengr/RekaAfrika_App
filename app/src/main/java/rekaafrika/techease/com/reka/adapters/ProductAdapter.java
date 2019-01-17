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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.AllProductsModel;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.CategoriesFragment;
import rekaafrika.techease.com.reka.views.fragments.ProductDetailsFragment;

public class ProductAdapter  extends BaseAdapter {
    ArrayList<AllProductsModel> allProductsModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;
    ArrayList<String> arrayList = new ArrayList<>();

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
        convertView = layoutInflater.inflate(R.layout.custome_item_layout, parent, false);
        viewHolder.tvTitle = convertView.findViewById(R.id.tv_item_title);
        viewHolder.ivItem = convertView.findViewById(R.id.iv_item_view);
        viewHolder.tvPrice = convertView.findViewById(R.id.tv_item_price);
        viewHolder.layout_product = convertView.findViewById(R.id.item_layout);


        viewHolder.tvTitle.setText(model.getTitle());
        viewHolder.tvPrice.setText(model.getPrice());
        Picasso.get().load(model.getImage()).into(viewHolder.ivItem);


        viewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.putStringValueInEditor(context,"product_id",model.getProduct_id());
                GeneralUtils.connectDrawerFragmentWithBack(context,new ProductDetailsFragment());

            }
        });


        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder {
        ImageView ivItem;
        TextView tvTitle,tvPrice;
        RelativeLayout layout_product;
    }
}
