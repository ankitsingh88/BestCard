package com.ankitsingh.bestcard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CategoryAdapter;
import com.ankitsingh.bestcard.util.DataUtil;

public class CategoriesFragment extends Fragment {

    private CategoryAdapter categoryAdapter;
    private RecyclerView listCategories;

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_categories, container, false);

        this.categoryAdapter =
                new CategoryAdapter(
                        getActivity(),
                        DataUtil.loadCategories());

        this.listCategories = view.findViewById(R.id.list_categories);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        this.listCategories.setAdapter(this.categoryAdapter);
        this.listCategories.setLayoutManager(linearLayoutManager);
        this.listCategories.addItemDecoration(new DividerItemDecoration(this.listCategories.getContext(), linearLayoutManager.getOrientation()));

        return view;
    }
}
