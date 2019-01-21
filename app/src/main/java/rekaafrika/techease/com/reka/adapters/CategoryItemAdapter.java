package rekaafrika.techease.com.reka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.CategoryItemModel;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.ProductsFragment;

public class CategoryItemAdapter   extends BaseAdapter {
    ArrayList<CategoryItemModel> allProductsModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    CategoryItemAdapter.MyViewHolder viewHolder = null;


    public CategoryItemAdapter(Context context, ArrayList<CategoryItemModel> allProductsModelArrayList) {
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
        final CategoryItemModel model = allProductsModelArrayList.get(position);
        if (allProductsModelArrayList != null && allProductsModelArrayList.size() > position)
            return allProductsModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CategoryItemModel model = allProductsModelArrayList.get(position);

        viewHolder = new CategoryItemAdapter.MyViewHolder();
        convertView = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        viewHolder.tvTitle = convertView.findViewById(R.id.tv_item_title);
        viewHolder.ivItem = convertView.findViewById(R.id.iv_item_view);
        viewHolder.layout_product = convertView.findViewById(R.id.category_layout);

        viewHolder.tvTitle.setText(model.getName());

        if(model.getImage().equals("") || model.getImage()==null){
            viewHolder.ivItem.setImageDrawable(context.getResources().getDrawable(R.drawable.shop));
        }
        else {
            Picasso.get().load(model.getImage()).into(viewHolder.ivItem);
        }

        viewHolder.layout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.putStringValueInEditor(context,"slug",model.getSlug());
                GeneralUtils.connectDrawerFragmentWithBack(context,new ProductsFragment());
            }
        });


        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder {
        ImageView ivItem;
        TextView tvTitle,tvPrice,tvRegularPrice;
        RelativeLayout layout_product;
    }
}
