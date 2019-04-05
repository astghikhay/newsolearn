package com.astghik.newsolearn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.astghik.newsolearn.activities.BaseActivity;
import com.astghik.newsolearn.models.ItemModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreferencesData {

    private static SharedPreferencesData obj;

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";
    public static final String PINNED = "pinned_list";
    private List<ItemModel> offlineItemList;
    private ArrayList<String> pinedNewsList;

    private SharedPreferencesData() {

    }

    public static SharedPreferencesData getInstance() {
        if (obj == null) {
            obj = new SharedPreferencesData();
        }

        return obj;
    }


    // This four methods are used for maintaining offlineItemList.
    public void saveOfflineNews(Context context, List<ItemModel> favorites) {
        this.offlineItemList = favorites;
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addForOfflieUse(Context context, ItemModel product) {
        List<ItemModel> favorites = getOfflineNews(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getNeewsID().equals(product.getNeewsID())) {
                return;
            }
        }
        favorites.add(product);
        saveOfflineNews(context, favorites);

    }

    public void removeFromOfflineUse(Context context, ItemModel product) {
        ArrayList<ItemModel> favorites = getOfflineNews(context);
        if (favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {
                if (favorites.get(i).getNeewsID().equals(product.getNeewsID())) {
                    favorites.remove(i);
                    break;
                }
            }
            saveOfflineNews(context, favorites);
        }
    }

    public ArrayList<ItemModel> getOfflineNews(Context context) {
        SharedPreferences settings;
        List<ItemModel> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            ItemModel[] favoriteItems = gson.fromJson(jsonFavorites,
                    ItemModel[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<ItemModel>) favorites;
    }

    public void addPinnedItem(BaseActivity activity, String id) {
        if (pinedNewsList == null) {
            pinedNewsList = new ArrayList<>();
        }
        pinedNewsList.add(id);
        savePinnedItemArrayList(activity);
    }

    public void removePinnedItem(BaseActivity activity, String id) {
        if (pinedNewsList == null) {
            pinedNewsList = new ArrayList<>();
        }
        pinedNewsList.remove(id);
        savePinnedItemArrayList(activity);
    }

    public void savePinnedItemArrayList(BaseActivity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pinedNewsList);
        editor.putString(PINNED, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getPinnedItemArrayList(BaseActivity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(PINNED, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type) == null ? new ArrayList<String>() : (ArrayList<String>) gson.fromJson(json, type);
    }

}
