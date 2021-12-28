package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Url_JsonUtils{

    public static String parseUrl(URL url) {
        if (url == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // открываем соедиение к указанному URL
        // помощью конструкции try-with-resources
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String inputLine;
            // построчно считываем результат в объект StringBuilder
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    // парсим некоторые данные о людях
    public static List<JSONObject> parseCurrentAPIJson(String resultJson) {

        // конвертируем строку с Json в JSONObject для дальнейшего его парсинга
        JSONObject apiJsonObject = new JSONObject(resultJson);

        // получаем строку после response
        //System.out.println("Строка: " + apiJsonObject.get("response"));

        // получаем массив элементов для поля apiArray
        JSONObject apiArray = (JSONObject) apiJsonObject.get("response");
        JSONArray apiArrays = (JSONArray) apiArray.get("items");
        // достаем из массива первый элемент
        List<JSONObject> s = new ArrayList<>();
        for (int i = 0; i < apiArrays.length();i++) {
            JSONObject apiData = (JSONObject) apiArrays.get(i);
            s.add(apiData);
        }
        return s;
    }

    public static int parseCountAPIJson(String resultJson) {

        JSONObject apiJsonObject = new JSONObject(resultJson);

        JSONObject apiArray = (JSONObject) apiJsonObject.get("response");

        return (int)apiArray.get("count");
    }
    // создаем объект URL из указанной в параметре строки
    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
