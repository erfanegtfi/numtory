package com.numtory.application.data.utils;

import com.numtory.application.data.remote.baseResponse.ApiBaseResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;

public class UtilsError {

    public static ApiBaseResponse parseError(ResponseBody response) {

        ApiBaseResponse error = new ApiBaseResponse();
        JSONObject jsonObject;
        try {
            Gson gson = new Gson();
            jsonObject = new JSONObject(response.string());
            error = gson.fromJson(jsonObject.toString(), ApiBaseResponse.class);
            if (error != null) {
                if (jsonObject.get("message") instanceof JSONObject)
                    error.setMessage(printJsonObject(jsonObject.getJSONObject("message")));
                else if (jsonObject.get("message") instanceof String)
                    error.setMessage(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            error.setMessage("خطایی رخ داد!");
            return error;
        }

        return error;
    }


    private static String printJsonObject(JSONObject jsonObj) {
        StringBuilder builder = new StringBuilder();
        try {
            for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
                String keyStr = it.next();
                Object keyvalue = jsonObj.get(keyStr);

//                if (keyvalue instanceof JSONObject) {
//                    printJsonObject((JSONObject) keyvalue);
//                } else
                if (keyvalue instanceof JSONArray) {
                    JSONArray array = (JSONArray) keyvalue;
                    for (int i = 0; i < array.length(); i++) {
//                        if (array.get(i) instanceof JSONObject)
//                            printJsonObject((JSONObject) array.get(i));
                        if (array.get(i) instanceof String)
                            builder.append(array.get(i)).append("\n");
                    }

                } else {
                    builder.append(keyvalue.toString()).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }


}
