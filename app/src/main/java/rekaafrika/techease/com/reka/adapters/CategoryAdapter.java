package rekaafrika.techease.com.reka.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

import rekaafrika.techease.com.reka.R;
import rekaafrika.techease.com.reka.dateModels.CategoryDataModel;
import rekaafrika.techease.com.reka.utilities.GeneralUtils;
import rekaafrika.techease.com.reka.views.fragments.CategoryItemsFragment;
import rekaafrika.techease.com.reka.views.fragments.ProductsFragment;

public class CategoryAdapter  extends BaseAdapter {
    ArrayList<CategoryDataModel> categoryDataModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    CategoryAdapter.MyViewHolder viewHolder = null;


    public CategoryAdapter(Context context, ArrayList<CategoryDataModel> categoryDataModelArrayList) {
        this.categoryDataModelArrayList = categoryDataModelArrayList;
        this.context = context;
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);

        }
    }

    @Override
    public int getCount() {
        if (categoryDataModelArrayList != null) return categoryDataModelArrayList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (categoryDataModelArrayList != null && categoryDataModelArrayList.size() > position)
            return categoryDataModelArrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        final CategoryDataModel model = categoryDataModelArrayList.get(position);
        if (categoryDataModelArrayList != null && categoryDataModelArrayList.size() > position)
            return categoryDataModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CategoryDataModel model = categoryDataModelArrayList.get(position);

        viewHolder = new CategoryAdapter.MyViewHolder();
        convertView = layoutInflater.inflate(R.layout.custom_categories_layout, parent, false);
        viewHolder.tvTitle = convertView.findViewById(R.id.tv_category_name);
        viewHolder.ivCategory = convertView.findViewById(R.id.iv_category_view);
        viewHolder.layout_category = convertView.findViewById(R.id.category_layout);
        viewHolder.typeface = Typeface.createFromAsset(context.getAssets(),
                "medium.otf");

        viewHolder.tvTitle.setText(model.getName());
        viewHolder.tvTitle.setTypeface(viewHolder.typeface);

        if(model.getImage().equals("") || model.getImage()==null){
            viewHolder.ivCategory.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }
        else {
            Picasso.get().load(model.getImage()).into(viewHolder.ivCategory);
        }


        viewHolder.layout_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("no", position);
                GeneralUtils.connectDrawerFragmentWithBack(context, new CategoryItemsFragment()).setArguments(bundle);
            }
        });


        convertView.setTag(viewHolder);
        return convertView;
    }


    private class MyViewHolder {
        Typeface typeface;
        ImageView ivCategory;
        TextView tvTitle;
        RelativeLayout layout_category;
    }
}

