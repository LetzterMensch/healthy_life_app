package com.example.gr.controller.activity;

import static com.example.gr.utils.RecipeScraperUtils.parseMNMNCalories;
import static com.example.gr.utils.RecipeScraperUtils.parseMNMNDescription;
import static com.example.gr.utils.RecipeScraperUtils.parseMNMNIngredients;
import static com.example.gr.utils.RecipeScraperUtils.parseMNMNNutrition;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.example.gr.R;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.databinding.ActivityImportRecipeBinding;
import com.example.gr.model.Recipe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeRecipeActivity extends BaseActivity {
    private ActivityImportRecipeBinding mActivityImportRecipeBinding;
    private WebView webView;
    private String targetUrl = "https://monngonmoingay.com/";
    private CardView importBtn;
    private Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityImportRecipeBinding = ActivityImportRecipeBinding.inflate(getLayoutInflater());
        setContentView(mActivityImportRecipeBinding.getRoot());
        importBtn = mActivityImportRecipeBinding.btnImport;
        webView = mActivityImportRecipeBinding.webview;
        importBtn.setOnClickListener(v -> {
            if (mRecipe == null) {
                Toast.makeText(this, "Không tìm thấy công thức nấu ăn !", Toast.LENGTH_SHORT).show();
            }else{
                goToRecipeDetail(mRecipe);
            }
        });
        // Cấu hình WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Kích hoạt JavaScript nếu cần thiết
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        // Sử dụng WebViewClient để mở các liên kết trong WebView thay vì trình duyệt mặc định
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println(url);
                if (url.contains(targetUrl)) {
                    // Đọc nội dung HTML của trang khi nó đã được tải xong
//                    webView.evaluateJavascript(
//                            "(function() { return  document.getElementsByClassName(\"container grid grid-cols-1 lg:grid-cols-[1fr_370px] justify-between gap-4 lg:gap-6 mt-6 mb-10 lg:mb-20\"); })();",
//                            html -> scrapeRecipes(html)
//                    );
                    scrapeMNMNRecipes(url);
                }
                // Kiểm tra URL hiện tại và quản lý trạng thái của nút import recipe
                showImportButton(url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("WebView", "Error: " + error.getDescription());
                }
            }
        });
        // Tải trang web
        webView.loadUrl("file:///android_asset/index.html");
//        initToolbar();
    }

    private void goToRecipeDetail(@NonNull Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_CREATE_RECIPE_OBJECT, recipe);
        GlobalFunction.startActivity(this, RecipeDetailActivity.class, bundle);
        finish();
    }

    // Quản lý trạng thái của FAB dựa trên URL hiện tại
    private void showImportButton(String currentUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("show import button thread is running !!");
                if (currentUrl.contains(targetUrl)) {
                    importBtn.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                    importBtn.setEnabled(true);
                } else {
                    importBtn.setCardBackgroundColor(getResources().getColor(R.color.textColorAccent));
                    importBtn.setEnabled(false);
                }
            }
        });
    }

    // Phương thức để scrape dữ liệu từ HTML
    private void scrapeMNMNRecipes(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("trying to scrape this site");
                    Document doc = Jsoup.connect(url).get();
//                    System.out.println(doc);
                    Elements recipes = doc.select("div[class=container grid grid-cols-1 lg:grid-cols-[1fr_370px] justify-between gap-4 lg:gap-6 mt-6 mb-10 lg:mb-20]"); // Selector cần điều chỉnh theo cấu trúc HTML của trang web
                    if (recipes.size() > 0) {
//                        System.out.println(recipes.get(0).toString());
                        for (Element recipe : recipes) {
                            Element elementTitle = (Element) recipe.firstChild().firstChild();
                            String imgUrl = recipe.firstChild().lastChild().attr("data-img");
                            Element elementDescription = (Element) recipe.getElementsByClass("detail_main bg-white flex flex-col gap-4 relative").first();
                            Element elementCalories = (Element) recipe.lastChild().firstChild().lastChild().lastChild().firstChild();
                            Element elementNutrition = (Element) recipe.lastChild().firstChild().lastChild().lastChild().lastChild();
                            Elements elementIngredients = recipe.getElementsByClass("block-nguyenlieu tab-content");
                            assert elementCalories != null;
                            mRecipe = new Recipe(
                                    elementTitle.selectFirst("span").text(),
                                    imgUrl,
                                    parseMNMNDescription(elementDescription),
                                    parseMNMNNutrition(elementNutrition, parseMNMNCalories(elementCalories)).get("carb"),
                                    parseMNMNNutrition(elementNutrition, parseMNMNCalories(elementCalories)).get("protein"),
                                    parseMNMNNutrition(elementNutrition, parseMNMNCalories(elementCalories)).get("fat"),
                                    parseMNMNCalories(elementCalories),
                                    parseMNMNIngredients(elementIngredients),
                                    url
                            );
                            System.out.println("----------------------------");
                            System.out.println(("Title: " + mRecipe.getName()));
                            System.out.println(("image : " + imgUrl));
                            System.out.println(("description: " + mRecipe.getDescription()));
                            System.out.println(("calories: " + mRecipe.getCalories()));
                            System.out.println(("nutrition: " + mRecipe.getCarbs() + " " + mRecipe.getProtein() + " " + mRecipe.getFat()));
                            System.out.println("Ingredients: " + mRecipe.getIngredients());
                            System.out.println(("-----------------------"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Xử lý việc quay lại trang trước đó khi nhấn nút Back
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
