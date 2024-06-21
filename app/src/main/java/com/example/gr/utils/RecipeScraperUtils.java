package com.example.gr.utils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RecipeScraperUtils {
    //TODO : Get description, nutrition info, ingredients
    // sample : <div class="text-sm flex justify-between"><span>G:P:L (Đường : Đạm : Béo):</span> <strong>52:18:30</strong>
    //Nguyên Liệu: Muỗng Gram M: muỗng canh - m: muỗng cafe
    // Cá nục con nhỏ 400g
    // Mía cây 3 đốt
    // Hành tím 50g
    // Hành lá 20g
    // Ớt trái 3 trái
    // Tỏi băm 30g
    // Hạt nêm Aji-ngon®
    // HEO 4g (1m)
    // Nước mắm 40g (5M) Đường 4g (1m) Bột ngọt AJI-NO-MOTO® 6g (1m) Tương ớt 1M Nước màu 1M Ăn trưa kèm: Cơm, Cánh gà xào ớt xiêm, Canh rau dền khoai sọ GIA VỊ: nước mắm, đường, nước màu, tiêu, tương ớt Hạt nêm Aji-ngon® Heo Bột ngọt AJI-NO-MOTO® Cá nục con nhỏ 400g Mía cây 3 đốt Hành tím 50g Hành lá 20g Ớt trái 3 trái Tỏi băm 30g Hạt nêm Aji-ngon® HEO 4g (1m) Nước mắm 40g (5M) Đường 4g (1m) Bột ngọt AJI-NO-MOTO® 6g (1m) Tương ớt 1M Nước màu 1M Ăn trưa kèm: Cơm, Cánh gà xào ớt xiêm, Canh rau dền khoai sọ GIA VỊ: nước mắm, đường, nước màu, tiêu, tương ớt Hạt nêm Aji-ngon® Heo Bột ngọt AJI-NO-MOTO®
    public static String parseMNMNDescription(Element elementDescription){
        return elementDescription.selectFirst("p").text();
    }
    public static String parseMNMNIngredients(Elements elementListIngredients){
        Element elementIngredients = elementListIngredients.first();
        StringBuilder ingredientsList = new StringBuilder();
        ingredientsList.append("M: muỗng canh - m: muỗng cafe \n");
        Elements liElements = elementIngredients.getElementsByTag("li");
        for (Element li : liElements) {
            ingredientsList.append("• "+li.text() + "\n");
        }
        return ingredientsList.toString();
    }
    public static Map<String,Float> parseMNMNNutrition(Element elementNutrition, int calories){
        String nutritionTagContent = elementNutrition.selectFirst("strong").text();
        Map<String,Float> nutritionMap = new HashMap<>();
        // Carb:Protein:Fat
        Scanner scanner = new Scanner(nutritionTagContent).useDelimiter(":");
        float carb = (float) (scanner.nextInt() * calories) /100;
        float protein= (float) (scanner.nextInt() * calories) /100;
        float fat = (float) (scanner.nextInt() * calories) /100;
        nutritionMap.put("carb",carb);
        nutritionMap.put("protein",protein);
        nutritionMap.put("fat",fat);
        return nutritionMap;
    }
    public static int parseMNMNCalories(Element elementCalories){
        String caloriesTagContent = elementCalories.selectFirst("strong").text();
        int peoplePerServing = 0;
        StringBuilder caloriesBuilder = new StringBuilder();
        boolean canParse = false;
        for (int i = 0; i < caloriesTagContent.length() - 1; i++){
            if(Character.isDigit(caloriesTagContent.charAt(i)) && peoplePerServing == 0){
                peoplePerServing =  Character.getNumericValue(caloriesTagContent.charAt(i));
            }
            if (caloriesTagContent.charAt(i) == '('){
                canParse = true;
                continue;
            }
            if (caloriesTagContent.charAt(i) == 'k'){
                break;
            }
            if(canParse){
                if(caloriesTagContent.charAt(i) != '.'){
                    caloriesBuilder.append(caloriesTagContent.charAt(i));
                }
            }
        }

        return Integer.parseInt(caloriesBuilder.toString().trim())/peoplePerServing;
    }
}
