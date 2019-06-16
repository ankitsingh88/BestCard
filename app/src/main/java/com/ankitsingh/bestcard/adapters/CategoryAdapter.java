package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.model.Category;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    private final BestCardActivity bestCardActivity;

    private final List<Category> masterCategories = new ArrayList<>();
    private final List<Category> currentCategories = new ArrayList<>();

    public static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Category>() {
                @Override
                public boolean areItemsTheSame(
                        final Category oldItem,
                        final Category newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
                @Override
                public boolean areContentsTheSame(
                        final Category oldItem,
                        final Category newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            };

    public CategoryAdapter(
            final Context context,
            final List<Category> categories) {
        super(DIFF_CALLBACK);

        this.bestCardActivity = (BestCardActivity) context;

        this.currentCategories.addAll(categories);
        this.masterCategories.addAll(categories);

        submitList(this.currentCategories);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            int viewType) {
        final View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);

        return new CategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final CategoryViewHolder categoryViewHolder,
            final int position) {
        final Category category = getItem(position);

        categoryViewHolder
                .getCategoryIcon()
                .setImageResource(
                        this.bestCardActivity
                                .getResources()
                                .getIdentifier(
                                        category.getIconName(),
                                        "drawable",
                                        "com.ankitsingh.bestcard"));

        categoryViewHolder
                .getCategoryName()
                .setText(category.getName());
    }

    @Getter
    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryIcon;
        private TextView categoryName;

        private RelativeLayout categoryRow;

        public CategoryViewHolder(final View categoryView) {
            super(categoryView);

            this.categoryRow = (RelativeLayout) categoryView;

            this.categoryIcon = categoryView.findViewById(R.id.category_icon);
            this.categoryName = categoryView.findViewById(R.id.category_name);
        }
    }
}
