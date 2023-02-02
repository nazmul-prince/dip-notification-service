package com.dip.unifiedviewer.domain.services.impls;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.domain.services.ResponseUpdater;

import static com.dip.unifiedviewer.constansts.JsonConstants.*;

import java.util.Objects;

@Service
public class ResponseUpdaterImpl implements ResponseUpdater {
    @Override
    public JSONObject esafReponseUpdate(JSONObject data) {
        JSONObject esafResponse = data.getJSONObject(JSON_KEY_ESAF);
        JSONObject mnpResponse = new JSONObject();
        if (data.has(JSON_KEY_MNP)) {
            mnpResponse = data.getJSONObject(JSON_KEY_MNP);
        }

        JSONObject esafResponseRecord = new JSONObject();
        JSONObject mnpResponseRecord = new JSONObject();
        if (esafResponse.length() > 0 && esafResponse.getJSONArray(JSON_KEY_RESPONSE_RECORDS).length() > 0) {
            esafResponseRecord = esafResponse.getJSONArray(JSON_KEY_RESPONSE_RECORDS).getJSONObject(0);
        }

        if (mnpResponse.length() > 0 && mnpResponse.getJSONArray(JSON_KEY_RESPONSE_RECORDS).length() > 0) {
            mnpResponseRecord = mnpResponse.getJSONArray(JSON_KEY_RESPONSE_RECORDS).getJSONObject(0);
        }

        JSONObject mnpData = new JSONObject();
        if ((esafResponseRecord.length() > 0 && esafResponseRecord.getInt(JSON_KEY_NUMBER_OF_RECORDS_FOUND) > 0) &&
                (mnpResponseRecord.length() > 0 && mnpResponseRecord.getInt(JSON_KEY_NUMBER_OF_RECORDS_FOUND) > 0)) {
            mnpData = mnpResponseRecord.getJSONArray(JSON_KEY_RESPONSE_RECORD).getJSONObject(0);
        }

        String currentOperator = null;
        if (mnpData.length() > 0) {
            currentOperator = mnpData.getString(JSON_KEY_OPERATOR);
        }

        if (esafResponseRecord.length() > 0) {
            for (int i = 0; i < esafResponseRecord.getJSONArray(JSON_KEY_RESPONSE_RECORD).length(); i++) {
                JSONObject esafData = esafResponseRecord.getJSONArray(JSON_KEY_RESPONSE_RECORD).getJSONObject(i);
                esafData.put(JSON_KEY_CURRENT_OPERATOR, !Objects.equals(currentOperator, null) ? currentOperator : esafData.getString(JSON_KEY_OPERATOR));
            }
        }

        return data;
    }
}
