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
    private final DecimalFormat df = new DecimalFormat("#.##");
    int currentItemCount;
    int subFoodItemCount;
    String searchkey;
    final float[] carbCalo = new float[1];
    final float[] proteinCalo = new float[1];
    final float[] fatCalo = new float[1];

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
                getOriginalFoodData(subFood, carbCalo, proteinCalo, fatCalo);

                carb += carbCalo[0] * subFood.getNumberOfServings();
                protein += proteinCalo[0] * subFood.getNumberOfServings();
                fat += fatCalo[0] * subFood.getNumberOfServings();
                calories += subFood.getCalories() * subFood.getNumberOfServings();
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

    private boolean validateNewFoodV2() {
        if (mActivityCreateFoodBinding.editNewFoodName.getText().toString().equals("") ||
                mActivityCreateFoodBinding.editNewFoodName.getText().toString().isEmpty()) {
            showAlertDialog("Vui lòng nhập tên món ăn");
            return false;
        }
        if (mActivityCreateFoodBinding.editQuickAddCalories.getText().toString().trim().isEmpty() ||
                mActivityCreateFoodBinding.editQuickAddCarb.getText().toString().trim().isEmpty() ||
                mActivityCreateFoodBinding.editQuickAddFat.getText().toString().trim().isEmpty() ||
                mActivityCreateFoodBinding.editQuickAddProtein.getText().toString().trim().isEmpty() ||
                mActivityCreateFoodBinding.editQuickAddServingSize.getText().toString().trim().isEmpty()) {
            showAlertDialog("Vui lòng nhập đủ thông tin");
            return false;
        }
        return true;
    }

    private void addNewFood() {
        if (validateNewFoodV2()) {
            String name = mActivityCreateFoodBinding.editNewFoodName.getText().toString().trim();
            float servingSize = Float.parseFloat(String.valueOf(mActivityCreateFoodBinding.editQuickAddServingSize.getText()));
            float carb = Float.parseFloat(String.valueOf(mActivityCreateFoodBinding.editQuickAddCarb.getText()));
            float protein = Float.parseFloat(String.valueOf(mActivityCreateFoodBinding.editQuickAddProtein.getText()));
            float fat = Float.parseFloat(String.valueOf(mActivityCreateFoodBinding.editQuickAddFat.getText()));
            int calories = Math.round(Float.parseFloat(String.valueOf(mActivityCreateFoodBinding.editQuickAddCalories.getText())));
            int calculatedCalories = Math.round((carb + protein) * 4 + fat * 9);
            if (calculatedCalories < calories - 15 || calculatedCalories > calories + 15) {
                showAlertDialog("Thông tin dinh dưỡng chưa đúng");
                return;
            }
            newFood = new Food(name, servingSize, calories, protein, fat, carb);
            LocalDatabase.getInstance(this).foodDAO().insertFood(newFood);
            //Fix bug không tìm thấy id của food trong foodlog
            newFood = LocalDatabase.getInstance(this).foodDAO().getFoodByTimestamp(newFood.getTimestamp());
            mDiary.logFood(new FoodLog(newFood, mMeal, newFood.getNumberOfServings(), mDiary.getId()));
            Toast.makeText(this, "Đã tạo thực phẩm và lưu vào nhật ký", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initUI() {
        getFoodListFromLocalDatabase(""); //Display search food list
        displaySubFoodList();
        mActivityCreateFoodBinding.btnSaveNewFood.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_shape_corner_30));
        mActivityCreateFoodBinding.foodCarb.setText("0");
        mActivityCreateFoodBinding.foodProtein.setText("0");
        mActivityCreateFoodBinding.foodFat.setText("0");
        mActivityCreateFoodBinding.calories.setText("0");
        mActivityCreateFoodBinding.optionToggleBtn.setChecked(false);
        mActivityCreateFoodBinding.optionToggleBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_shape_corner_6));
        mActivityCreateFoodBinding.quickAddLayout.setVisibility(View.GONE);
        mActivityCreateFoodBinding.btnQuickSaveNewFood.setVisibility(View.GONE);
        mActivityCreateFoodBinding.createFoodFromDb.setVisibility(View.VISIBLE);
        mActivityCreateFoodBinding.btnSaveNewFood.setVisibility(View.VISIBLE);
    }

    private void switchOption() {
        if (mActivityCreateFoodBinding.optionToggleBtn.isChecked()) {
            mActivityCreateFoodBinding.optionToggleBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_shape_corner_6));
            mActivityCreateFoodBinding.quickAddLayout.setVisibility(View.VISIBLE);
            mActivityCreateFoodBinding.btnQuickSaveNewFood.setVisibility(View.VISIBLE);
            mActivityCreateFoodBinding.createFoodFromDb.setVisibility(View.GONE);
            mActivityCreateFoodBinding.btnSaveNewFood.setVisibility(View.GONE);
        } else {
            mActivityCreateFoodBinding.optionToggleBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_shape_corner_6));
            mActivityCreateFoodBinding.quickAddLayout.setVisibility(View.GONE);
            mActivityCreateFoodBinding.btnQuickSaveNewFood.setVisibility(View.GONE);
            mActivityCreateFoodBinding.createFoodFromDb.setVisibility(View.VISIBLE);
            mActivityCreateFoodBinding.btnSaveNewFood.setVisibility(View.VISIBLE);
        }
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
        float fat = 0;
        float protein = 0;
        int calories = 0;
        System.out.println("ingredients size : " + subFoodList.size());
        for (Food subFood : subFoodList) {

            getOriginalFoodData(subFood, carbCalo, proteinCalo, fatCalo);

            carb += carbCalo[0] * subFood.getNumberOfServings();
            protein += proteinCalo[0] * subFood.getNumberOfServings();
            fat += fatCalo[0] * subFood.getNumberOfServings();
            calories += subFood.getCalories() * subFood.getNumberOfServings();
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
        // number of servings = 1
        getOriginalFoodData(food, carbCalo, proteinCalo, fatCalo);
        food.setCarb(carbCalo[0]);
        food.setProtein(proteinCalo[0]);
        food.setFat(fatCalo[0]);
        subFoodList.add(food);
        displaySubFoodList();
        displayFoodList();
    }

    private void onClickViewDetail(Food food) {
//        Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
        final AtomicInteger[] newCaloCount = {new AtomicInteger((int) ((int) food.getCalories() * food.getNumberOfServings()))};

        getOriginalFoodData(food, carbCalo, proteinCalo, fatCalo);

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

        tvSubFoodName.setText(food.getName());
        tvSubFoodCalories.setText(getString(R.string.unit_calories_burnt, df.format(newCaloCount[0].get())));
        tvSubFoodCarb.setText(getString(R.string.unit_calories_burnt, df.format(carbCalo[0]*food.getNumberOfServings())));
        tvSubFoodProtein.setText(getString(R.string.unit_calories_burnt, df.format(proteinCalo[0]*food.getNumberOfServings())));
        tvSubFoodFat.setText(getString(R.string.unit_calories_burnt, df.format(fatCalo[0]*food.getNumberOfServings())));
        tvCount.setText(df.format(food.getNumberOfServings()));

        tvCount.addTextChangedListener(new TextWatcher() {
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

                // Get original food data on fat, carb and protein
                getOriginalFoodData(food, carbCalo, proteinCalo, fatCalo);
                String text = s.toString().replace(',', '.');
                float newCount = 1;
                if (!text.isEmpty()) {
                    newCount = Float.parseFloat(text);
                } else {
                    newCount = 0;
                }
                newCaloCount[0].set(Math.round(newCount * food.getCalories()));
                carbCalo[0] = newCount * carbCalo[0];
                proteinCalo[0] = newCount * proteinCalo[0];
                fatCalo[0] = newCount * fatCalo[0];
                tvSubFoodCalories.setText(getString(R.string.unit_calories_burnt, df.format(newCaloCount[0])));
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
                tvCount.setText(df.format(newCount));
            }
        });

        tvAddBtn.setOnClickListener(v -> {
            String textCount = tvCount.getText().toString();
            float newCount = Float.parseFloat(textCount.replace(',', '.'));
            newCount += 0.5f;
            tvCount.setText(df.format(newCount));
        });

        tvCloseBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvAddToDiaryBtn.setOnClickListener(v -> {
            if (subFoodList.contains(food)) {
                subFoodList.set(subFoodList.indexOf(food), food);
            } else {
                subFoodList.add(food);
            }
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
        mActivityCreateFoodBinding.optionToggleBtn.setOnClickListener(v -> switchOption());
        mActivityCreateFoodBinding.btnQuickSaveNewFood.setOnClickListener(v -> addNewFood());
    }

    private void getOriginalFoodData(Food food, float[] carbCalo, float[] proteinCalo, float[] fatCalo) {
        if (food.getServingSize() != 1) {
            // handle the food from database which is measured by gram not Kcal for the first time
            if (food.getCarb() + food.getProtein() + food.getFat() < (food.getCalories() - 10)) {
                double calDiff = Math.round((food.getFat() * 9 + (food.getCarb() + food.getProtein()) * 4)) - food.getCalories();
                float correctionVal = (float) (calDiff / 3);
                fatCalo[0] = Math.round((food.getFat() * 9 - correctionVal));
                carbCalo[0] = Math.round((food.getCarb() * 4 - correctionVal));
                proteinCalo[0] = Math.round((food.getProtein() * 4 - correctionVal));

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
    }

    private void searchSubFood() {
        searchkey = mActivityCreateFoodBinding.edtSearchName.getText().toString().trim();
        if (foodList != null) foodList.clear();
//        getListExerciseFromFirebase(searchkey);
        getFoodListFromLocalDatabase(searchkey);
        GlobalFunction.hideSoftKeyboard(this);
    }
}
