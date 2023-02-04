package com.af.githubtrends.data.datasource.local;

import android.content.Context;
import com.af.githubtrends.data.storage.SharedPref;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class FavoriteLocalDataSource {

    private final SharedPref sharedPref;

    public FavoriteLocalDataSource(Context context) {
        sharedPref = SharedPref.getInstance(context);
    }

    public void saveFavoriteRepoItem(SearchRepositoriesResponse.Items item){
        String jsonArr = sharedPref.getStringObj(SharedPref.FAV_ITEMS);
        JSONArray jsonArray;
            try {
                if (jsonArr.equals("")) {
                    jsonArray = new JSONArray();
                }else {
                    jsonArray = new JSONArray(jsonArr);
                }
                String jsonObject = new Gson().toJson(item);
                jsonArray.put(jsonObject);
                sharedPref.saveString(SharedPref.FAV_ITEMS, jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    public ArrayList<SearchRepositoriesResponse.Items> getAllFavoriteItems(){
        ArrayList<SearchRepositoriesResponse.Items> items = new ArrayList<>();
        String jsonArr = sharedPref.getStringObj(SharedPref.FAV_ITEMS);
        if (!jsonArr.equals("")){
            try {
                JSONArray jsonArray = new JSONArray(jsonArr);
                for (int i = 0; i < jsonArray.length(); i ++) {
                    SearchRepositoriesResponse.Items itemsArrayList = new Gson().fromJson(jsonArray.getString(i), SearchRepositoriesResponse.Items.class);
                    items.add(itemsArrayList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return items;

    }

    public void removeItem(int itemId){

        ArrayList<SearchRepositoriesResponse.Items> items = getAllFavoriteItems();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < items.size(); i++){
            if (items.get(i).getId() == itemId){
                items.remove(items.get(i));
            }
        }

        for (int i = 0; i < items.size(); i++) {
            String jsonObject = new Gson().toJson(items.get(i));
            jsonArray.put(jsonObject);
        }

        sharedPref.saveString(SharedPref.FAV_ITEMS, jsonArray.toString());
    }

    public boolean isExists(int itemId){
        ArrayList<SearchRepositoriesResponse.Items> items = getAllFavoriteItems();
        for (SearchRepositoriesResponse.Items item : items){
            if (item.getId() == itemId){
                return true;
            }
        }
        return false;
    }

}
