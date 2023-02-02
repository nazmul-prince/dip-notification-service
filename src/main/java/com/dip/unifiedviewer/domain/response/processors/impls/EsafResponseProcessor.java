package com.dip.unifiedviewer.domain.response.processors.impls;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
import com.dip.unifiedviewer.domain.response.processors.ResponseProcessor;

import static com.dip.unifiedviewer.constansts.JsonConstants.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class EsafResponseProcessor implements ResponseProcessor {
    private final PublicApiWorkerTaskType workerTaskType = PublicApiWorkerTaskType.ESAF;

    @Override
    public JSONObject processResponse(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray(JSON_KEY_RESPONSE_RECORD);
        JSONArray newJsonArray = new JSONArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject newJsonObject = new JSONObject();
            if (jsonArray.getJSONObject(i).has("address")) {
                newJsonObject.put("address", jsonArray.getJSONObject(0).get("address"));
            }

            if (jsonArray.getJSONObject(i).has("phone")) {
                newJsonObject.put("phone", jsonArray.getJSONObject(0).get("phone"));
            }

            if (jsonArray.getJSONObject(i).has("nid")) {
                newJsonObject.put("nid", jsonArray.getJSONObject(0).get("nid"));
            }

            if (jsonArray.getJSONObject(i).has("name")) {
                newJsonObject.put("name", jsonArray.getJSONObject(0).get("name"));
            }

            if (jsonArray.getJSONObject(i).has("birthDate")) {
                newJsonObject.put("birthDate", jsonArray.getJSONObject(0).get("birthDate"));
            }

            if (jsonArray.getJSONObject(i).has("operator")) {
                newJsonObject.put("operator", jsonArray.getJSONObject(0).get("operator"));
            }

            newJsonArray.put(newJsonObject);
        }

        jsonObject.put(JSON_KEY_RESPONSE_RECORD, newJsonArray);
        return jsonObject;
    }

    @Override
    public PublicApiWorkerTaskType getWorkerTaskType() {
        return workerTaskType;
    }
}
