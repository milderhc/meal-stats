package com.mealstats.mealstats.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mealstats.mealstats.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetNutritionalInfo {
    private static String URL = "http://192.168.43.28:8080";
    private RequestQueue queue;
    private Context context;

    public GetNutritionalInfo(Context context){
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    private String encodeImage(String fileName) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(fileName);//You can get an inputStream using any IO API
        byte[] bytes;
        byte[] buffer = new byte[8192]; //8192 is the buffer size
        int bytesRead;
        ByteArrayOutputStream output;
        output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private String extractExtension(String fileName) throws RuntimeException{
        int i = fileName.lastIndexOf('.');
        if(i == -1)
            throw new RuntimeException("The file " + fileName + " does not have a extension.");
        String fileExtension = fileName.substring(i+1);
        return fileExtension;
    }

    public void sendRequest(String fileName,
                            OnResponseNutritionaIInfo onResponseNutritionaIInfo,
                            Response.ErrorListener onErrorListener)
            throws FileNotFoundException, RuntimeException {

        final String fileExtension = extractExtension(fileName);
        final String encodedString = encodeImage(fileName);


        JSONObject params = new JSONObject();


        try {
            JSONArray paramsArray = new JSONArray();
            JSONObject methodParams = new JSONObject();
            methodParams.put("image", encodedString);
            methodParams.put("file_extension", fileExtension);
            paramsArray.put(methodParams);

            params.put("method", "getNutritionalInfo")
                    .put("jsonrpc", "2.0")
                    .put("id", "0")
                    .put("params", paramsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                URL,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Map<String, String> data = null;
                        try {
                            data = parseJSONResponse(response);
                        } catch (JSONException e) {
                            data = new HashMap<>();
                            data.put("error", context.getResources().getString(R.string.error_receving_response));
                            data.put("details", e.toString());
                            data.put("originalResponse", response.toString());
                            e.printStackTrace();
                        }

                        onResponseNutritionaIInfo.onResponse(data);

                    }
                },
                onErrorListener
        );

        queue.add(jsonObjectRequest);
    }


    private Map<String, String> parseJSONResponse(JSONObject response) throws JSONException {
        Map<String, String> data = new HashMap<>();
        try {
            JSONArray responseArray = response.getJSONArray("result");
            for(int i=0; i<responseArray.length(); i++){
                JSONObject obj = responseArray.getJSONObject(i);
                for(String key : iteratorToIterable(obj.keys())){
                    data.put(key, obj.getString(key));
                }
            }
        } catch (JSONException e) {
            JSONObject obj = response.getJSONObject("result");
            for(String key : iteratorToIterable(response.keys())){
                data.put(key, response.getString(key));
            }
        }

        return data;
    }

    private <T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
        return () -> iterator;
    }
}