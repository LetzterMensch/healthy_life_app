package com.example.gr.controller.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.R;
import com.example.gr.databinding.ActivityCreateFoodBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.view.adapter.SubFoodAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateFoodActivity extends BaseActivity {
    private ActivityCreateFoodBinding mActivityCreateFoodBinding;
    private Food newFood;
    private Diary mDiary;
    private List<Food> foodList; //search window
    private List<Food> subFoodList; //ingredients
    private int mMeal;
    private SubFoodAdapter mSubFoodAdapter;
    private SubFoodAdapter mIngredientAdapter; // Don't be confused
    private DecimalFormat df = new DecimalFormat("#.##");
    int currentItemCount;
    int subFoodItemCount;
    String searchkey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCreateFoodBinding = ActivityCreateFoodBinding.inflate(getLayoutInflater());
        setContentView(mActivityCreateFoodBinding.getRoot());
        currentItemCount = 0;
        subFoodItemCount = 0;
        subFoodList = new ArrayList<>();
        getDataIntent();
        initListener();
        initUI();
    }

    private void createNewFood() {
        float carb = 0;
        float protein = 0;
        float fat = 0;
        int calories = 0;
        StringBuilder subfoodIds = new StringBuilder();
        if (validateNewFood()) {
            for (Food subFood : subFoodList) {
                carb += subFood.getCarb();
                protein += subFood.getProtein();
                fat += subFood.getFat();
                calories += subFood.getCalories();
                subfoodIds.append(subFood.getId());
                subfoodIds.append(",");
            }
            subfoodIds.deleteCharAt(subfoodIds.length() - 1);
            String name = mActivityCreateFoodBinding.editNewFoodName.getText().toString().trim();
            newFood = new Food(name, calories, protein, fat, carb, subfoodIds.toString());
            LocalDatabase.getInstance(this).foodDAO().insertFood(newFood);
            //Fix bug không tìm thấy id của food trong foodlog
            newFood = LocalDatabase.getInstance(this).foodDAO().getFoodByTimestamp(newFood.getTimestamp());
            mDiary.logFood(new FoodLog(newFood, mMeal, newFood.getNumberOfServings(), mDiary.getId()));
            Toast.makeText(this, "Đã tạo món ăn và lưu vào nhật ký", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validateNewFood() {
        if (mActivityCreateFoodBinding.editNewFoodName.getText().toString().equals("") ||
                mActivityCreateFoodBinding.editNewFoodName.getText().toString().isEmpty()) {
            showAlertDialog("Vui lòng nhập tên món ăn");
            return false;
        }
        if (subFoodList.isEmpty()) {
            showAlertDialog("Vui lòng thêm các thực phẩm thành phần");
            return false;
        }
        return true;
    }

    private void initUI() {
        getFoodListFromLocalDatabase(""); //Display search food list
        displaySubFoodList();
        mActivityCreateFoodBinding.btnSaveNewFood.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_shape_corner_30));
        mActivityCreateFoodBinding.foodCarb.setText("0");
        mActivityCreateFoodBinding.foodProtein.setText("0");
        mActivityCreateFoodBinding.foodFat.setText("0");
        mActivityCreateFoodBinding.calories.setText("0");
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDiary = (Diary) bundle.get(Constant.KEY_INTENT_ADD_FOOD_OBJECT);
            mMeal = bundle.getInt("key_meal");
        }
    }

    private void deleteSubFoodItem(Food food) {
        subFoodList.remove(food);
        displaySubFoodList();
    }

    private void displayNutriInfo() {
        float carb = 0;
        float protein = 0;
        float fat = 0;
        int calories = 0;
        System.out.println("ingredients size : " + subFoodList.size());
        for (Food subFood : subFoodList) {
            carb += subFood.getCarb();
            protein += subFood.getProtein();
            fat += subFood.getFat();
            calories += subFood.getCalories();
        }
        mActivityCreateFoodBinding.calories.setText(getString(R.string.unit_calories_burnt, String.valueOf(calories)));
        mActivityCreateFoodBinding.foodCarb.setText(getString(R.string.unit_calories_burnt, df.format(carb)));
        mActivityCreateFoodBinding.foodProtein.setText(getString(R.string.unit_calories_burnt, df.format(protein)));
        mActivityCreateFoodBinding.foodFat.setText(getString(R.string.unit_calories_burnt, df.format(fat)));
    }

    private void displaySubFoodList() {
        if (mIngredientAdapter == null) {
            mIngredientAdapter = new SubFoodAdapter(subFoodList, this::onClickViewDetail, this::quickAddBtn, this::deleteSubFoodItem, false);
            mActivityCreateFoodBinding.rcvIngredients.setLayoutManager(new LinearLayoutManager(this));
            mActivityCreateFoodBinding.rcvIngredients.setAdapter(mIngredientAdapter);
            mActivityCreateFoodBinding.rcvIngredients.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            subFoodItemCount = mIngredientAdapter.getItemCount();
            mIngredientAdapter.updateSubFoodList(subFoodList);
        }
        displayNutriInfo();
    }

    private void getFoodListFromLocalDatabase(String searchkey) {
        if (searchkey == null || searchkey.isEmpty()) {
            foodList = LocalDatabase.getInstance(this).foodDAO().getAllFood();
        } else {
            foodList = LocalDatabase.getInstance(this).foodDAO().findFoodByName("%" + searchkey + "%");
        }
        displayFoodList();
    }

    private void displayFoodList() {
        if (mSubFoodAdapter == null) {
            mSubFoodAdapter = new SubFoodAdapter(foodList, this::onClickViewDetail, this::quickAddBtn, this::deleteSubFoodItem, true);
            mActivityCreateFoodBinding.rcvFoodList.setLayoutManager(new LinearLayoutManager(this));
            mActivityCreateFoodBinding.rcvFoodList.setAdapter(mSubFoodAdapter);
            currentItemCount = mSubFoodAdapter.getItemCount();
            if (currentItemCount < 5) {
                mActivityCreateFoodBinding.rcvFoodList.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                mActivityCreateFoodBinding.rcvFoodList.getLayoutParams().height = 800;
            }
        } else {
            for (int i = 0; i < foodList.size(); i++) {
                for (Food subFood : subFoodList
                ) {
                    if (foodList.get(i).getId() == subFood.getId()) {
                        foodList.remove(foodList.get(i));
                    }
                }
            }
            mSubFoodAdapter.updateSubFoodList(foodList);
            currentItemCount = mSubFoodAdapter.getItemCount();
            if (currentItemCount < 5) {
                mActivityCreateFoodBinding.rcvFoodList.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                mActivityCreateFoodBinding.rcvFoodList.getLayoutParams().height = 800;
            }
        }
    }

    private void quickAddBtn(Food food) {
        for (Food subFood : subFoodList
        ) {
            if (subFood.getId() == food.getId()) {
                Toast.makeText(this, "Thực phẩm đã có trong danh sách rồi !", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        float carbCalo = 0;
        float proteinCalo = 0;
        float fatCalo = 0;
        if (food.getServingSize() == 100f) {
            // handle the food from database which is mearsured by gram not Kcal for the first time
            if (food.getCarb() + food.getProtein() + food.getFat() < (food.getCalories() - 10)) {
                carbCalo = food.getCarb() * 4;
                proteinCalo = food.getProtein() * 4;
                fatCalo = food.getFat() * 9;
            } else {
                carbCalo = food.getCarb();
                proteinCalo = food.getProtein();
                fatCalo = food.getFat();
            }
        }
        food.setCarb(carbCalo);
        food.setProtein(proteinCalo);
        food.setFat(fatCalo);
        subFoodList.add(food);
        displaySubFoodList();
        displayFoodList();
    }

    private void onClickViewDetail(Food food) {
//        Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
        final AtomicInteger[] newCaloCount = {new AtomicInteger((int) ((int) food.getCalories() * food.getNumberOfServings()))};
        final float[] carbCalo = new float[1];
        final float[] proteinCalo = new float[1];
        final float[] fatCalo = new float[1];
        float[] originalCount = new float[1];
        if (food.getServingSize() == 100f) {
            // handle the food from database which is mearsured by gram not Kcal for the first time
            if (food.getCarb() + food.getProtein() + food.getFat() < (food.getCalories() - 10)) {
                carbCalo[0] = food.getCarb() * 4;
                proteinCalo[0] = food.getProtein() * 4;
                fatCalo[0] = food.getFat() * 9;
            } else {
                carbCalo[0] = food.getCarb();
                proteinCalo[0] = food.getProtein();
                fatCalo[0] = food.getFat();
            }
        } else {
            carbCalo[0] = food.getCarb();
            proteinCalo[0] = food.getProtein();
            fatCalo[0] = food.getFat();
        }
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_add_subfood, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        TextView tvSubFoodName = viewDialog.findViewById(R.id.dialog_subfood_item_name);
        TextView tvSubFoodCarb = viewDialog.findViewById(R.id.dialog_subfood_item_carb);
        TextView tvSubFoodProtein = viewDialog.findViewById(R.id.dialog_subfood_item_protein);
        TextView tvSubFoodFat = viewDialog.findViewById(R.id.dialog_subfood_item_fat);
        TextView tvSubFoodCalories = viewDialog.findViewById(R.id.dialog_subfood_item_calo);
        TextView tvSubtractBtn = viewDialog.findViewById(R.id.dialog_subfood_tv_subtract);
        TextView tvAddBtn = viewDialog.findViewById(R.id.dialog_subfood_tv_add);
        TextView tvCloseBtn = viewDialog.findViewById(R.id.dialog_subfood_sheet_close_btn);
        TextView tvAddToDiaryBtn = viewDialog.findViewById(R.id.dialog_tv_subfood_add_to_list);


        EditText tvCount = viewDialog.findViewById(R.id.dialog_subfood_tv_count);
        originalCount[0] = food.getNumberOfServings();

        tvSubFoodName.setText(food.getName());
        tvSubFoodCalories.setText(getString(R.string.unit_calories_burnt, String.valueOf(newCaloCount[0].get() / originalCount[0])));
        tvSubFoodCarb.setText(getString(R.string.unit_calories_burnt, df.format(carbCalo[0] / originalCount[0])));
        tvSubFoodProtein.setText(getString(R.string.unit_calories_burnt, df.format(proteinCalo[0] / originalCount[0])));
        tvSubFoodFat.setText(getString(R.string.unit_calories_burnt, df.format(fatCalo[0] / originalCount[0])));
        tvCount.setText(df.format(food.getNumberOfServings()));

        tvCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
                originalCount[0] = Float.parseFloat(tvCount.getText().toString().replace(',', '.'));
                if(originalCount[0] == 0){
                    originalCount[0] = 1;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().replace(',', '.');
//                StringBuilder textBuilder = new StringBuilder(text);
//                if(text.indexOf('.') == text.length() -1){
//                    textBuilder.append('0');
//                }
                float newCount = 0;
                if (!text.isEmpty()) {
                    newCount = Float.parseFloat(text.toString());
                }
                newCaloCount[0].set(Math.round(newCount * newCaloCount[0].get() / originalCount[0]));
                carbCalo[0] = newCount * carbCalo[0] / originalCount[0];
                proteinCalo[0] = newCount * proteinCalo[0] / originalCount[0];
                fatCalo[0] = newCount * fatCalo[0] / originalCount[0];
                tvSubFoodCalories.setText(getString(R.string.unit_calories_burnt, String.valueOf(newCaloCount[0])));
                tvSubFoodCarb.setText(getString(R.string.unit_calories_burnt, df.format(carbCalo[0])));
                tvSubFoodProtein.setText(getString(R.string.unit_calories_burnt, df.format(proteinCalo[0])));
                tvSubFoodFat.setText(getString(R.string.unit_calories_burnt, df.format(fatCalo[0])));
                food.setNumberOfServings(newCount);
            }
        });
        tvSubtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textCount = tvCount.getText().toString();
                float count = Float.parseFloat(textCount.replace(',', '.'));
                if (count <= 0.5f) {
                    return;
                }
                float newCount = count - 0.5f;
                tvCount.setText(String.valueOf(newCount));

//                newCaloCount[0].set(Math.round(newCount * food.getCalories() / originalCount[0]));
//                carbCalo[0] = newCount * food.getCarb() / originalCount[0];
//                proteinCalo[0] = newCount * food.getProtein() / originalCount[0];
//                fatCalo[0] = newCount * food.getFat() / originalCount[0];
//                tvSubFoodCalories.setText(getString(R.string.unit_calories_burnt, String.valueOf(newCaloCount[0])));
//                tvSubFoodCarb.setText(getString(R.string.unit_calories_burnt, df.format(carbCalo[0])));
//                tvSubFoodProtein.setText(getString(R.string.unit_calories_burnt, df.format(proteinCalo[0])));
//                tvSubFoodFat.setText(getString(R.string.unit_calories_burnt, df.format(fatCalo[0])));
//                food.setNumberOfServings(newCount);

            }
        });

        tvAddBtn.setOnClickListener(v -> {
            String textCount = tvCount.getText().toString();
            float newCount = Float.parseFloat(textCount.replace(',', '.'));
            newCount += 0.5f;
            tvCount.setText(String.valueOf(newCount));
//            newCaloCount[0].set(Math.round(newCount * food.getCalories() / originalCount[0]));
//            carbCalo[0] = newCount * food.getCarb() / originalCount[0];
//            proteinCalo[0] = newCount * food.getProtein()/ originalCount[0];
//            fatCalo[0] = newCount * food.getFat() / originalCount[0];
//            tvSubFoodCalories.setText(getString(R.string.unit_calories_burnt, String.valueOf(newCaloCount[0])));
//            tvSubFoodCarb.setText(getString(R.string.unit_calories_burnt, df.format(carbCalo[0])));
//            tvSubFoodProtein.setText(getString(R.string.unit_calories_burnt, df.format(proteinCalo[0])));
//            tvSubFoodFat.setText(getString(R.string.unit_calories_burnt, df.format(fatCalo[0])));
//            food.setNumberOfServings(newCount);
        });

        tvCloseBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvAddToDiaryBtn.setOnClickListener(v -> {

            for (int i = 0; i < subFoodList.size(); i++) {
                if (subFoodList.get(i).getId() == food.getId()) {
                    food.setCalories(newCaloCount[0].get());
                    food.setCarb(carbCalo[0]);
                    food.setProtein(proteinCalo[0]);
                    food.setFat(fatCalo[0]);
                    subFoodList.set(i, food);
                    displaySubFoodList();
                    displayFoodList();
                    bottomSheetDialog.dismiss();
                    return;
                }
            }
            food.setCalories(newCaloCount[0].get());
            food.setCarb(carbCalo[0]);
            food.setProtein(proteinCalo[0]);
            food.setFat(fatCalo[0]);
            subFoodList.add(food);
            displaySubFoodList();
            displayFoodList();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void initListener() {
        mActivityCreateFoodBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (foodList != null) foodList.clear();
                    getFoodListFromLocalDatabase("");
                }
            }
        });

        mActivityCreateFoodBinding.imgSearch.setOnClickListener(view -> searchSubFood());

        mActivityCreateFoodBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchSubFood();
                return true;
            }
            return false;
        });

        mActivityCreateFoodBinding.imgClose.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        mActivityCreateFoodBinding.btnSaveNewFood.setOnClickListener(v -> createNewFood());
    }

    private void searchSubFood() {
        searchkey = mActivityCreateFoodBinding.edtSearchName.getText().toString().trim();
        if (foodList != null) foodList.clear();
//        getListExerciseFromFirebase(searchkey);
        getFoodListFromLocalDatabase(searchkey);
        GlobalFunction.hideSoftKeyboard(this);
    }
}
