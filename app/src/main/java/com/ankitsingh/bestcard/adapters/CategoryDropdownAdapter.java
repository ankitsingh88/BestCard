package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.holders.CategoryDropdownStateHolder;
import com.ankitsingh.bestcard.adapters.holders.CategoryDropdownViewHolder;

import java.util.List;

public class CategoryDropdownAdapter extends ArrayAdapter<CategoryDropdownStateHolder> {

    public CategoryDropdownAdapter(
            @NonNull final Context context,
            final int resource,
            @NonNull final List<CategoryDropdownStateHolder> categoryDropdownStateHolders) {
        super(context, resource, categoryDropdownStateHolders);
    }

    @Override
    public View getView(
            final int position,
            View convertView,
            final ViewGroup parent) {
        CategoryDropdownViewHolder categoryDropdownViewHolder = CategoryDropdownViewHolder.builder().build();

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.form_reward_category, parent, false);

            categoryDropdownViewHolder.setCategoryName(convertView.findViewById(R.id.form_reward_category_name));
            categoryDropdownViewHolder.setCategoryId(convertView.findViewById(R.id.form_reward_category_id));

            convertView.setTag(categoryDropdownViewHolder);
        } else {
            categoryDropdownViewHolder = (CategoryDropdownViewHolder) convertView.getTag();
        }

        final CategoryDropdownStateHolder categoryDropdownStateHolder = getItem(position);

        if (categoryDropdownStateHolder!= null) {
            categoryDropdownViewHolder.getCategoryName().setText(categoryDropdownStateHolder.getCategory().getName());

            categoryDropdownViewHolder.getCategoryId().setText(categoryDropdownStateHolder.getCategory().getId());
            categoryDropdownViewHolder.getCategoryId().setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
