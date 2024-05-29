package com.example.gr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.gr.activity.RecipeDetailActivity;
import com.example.gr.adapter.RecipeSearchTabAdapter;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.databinding.FragmentRecipeSearchBinding;
import com.example.gr.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListSearchFragment extends BaseFragment{
    private FragmentRecipeSearchBinding mFragmentRecipeSearchBinding;
    private RecipeSearchTabAdapter mRecipeSearchTabAdapter;
    private List<Recipe> mRecipeList;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentRecipeSearchBinding = FragmentRecipeSearchBinding.inflate(inflater, container, false);

        initUi();
//        initListener();

        return mFragmentRecipeSearchBinding.getRoot();
    }
    private void initUi(){
        mRecipeList = new ArrayList<>();
        mRecipeList.add(new Recipe("Bò sốt tiêu đen","Thịt bò là nguyên liệu thơm ngon và giàu chất dinh dưỡng. Điện máy XANH mách bạn công thức chế biến món bò sốt tiêu đen vô cùng đậm đà ngay tại nhà nhé. Cùng vào bếp với món xào hấp dẫn ngay thôi."));
        mRecipeList.add(new Recipe("Ếch xào măng","Thịt bò là nguyên liệu thơm ngon và giàu chất dinh dưỡng. Điện máy XANH mách bạn công thức chế biến món bò sốt tiêu đen vô cùng đậm đà ngay tại nhà nhé. Cùng vào bếp với món xào hấp dẫn ngay thôi."));
        mRecipeList.add(new Recipe("Gà rang muối","Thịt bò là nguyên liệu thơm ngon và giàu chất dinh dưỡng. Điện máy XANH mách bạn công thức chế biến món bò sốt tiêu đen vô cùng đậm đà ngay tại nhà nhé. Cùng vào bếp với món xào hấp dẫn ngay thôi."));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setLayoutManager(gridLayoutManager);
        mRecipeSearchTabAdapter = new RecipeSearchTabAdapter(mRecipeList,this::goToRecipeDetail);
        mFragmentRecipeSearchBinding.rcvRecipeSearchTab.setAdapter(mRecipeSearchTabAdapter);
    }
    @Override
    protected void initToolbar() {

    }
    private void goToRecipeDetail(@NonNull Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_RECIPE_OBJECT, recipe);
        GlobalFunction.startActivity(getActivity(), RecipeDetailActivity.class, bundle);
    }
}
