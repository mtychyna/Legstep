package com.azzdorfrobotics.android.legstep.repositories;

import android.util.Log;
import android.widget.Toast;

import com.azzdorfrobotics.android.legstep.R;
import com.azzdorfrobotics.android.legstep.helpers.Common;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public abstract class RequestCallback {

    public int recurtionOrder;
    public int recurtionDeep;

    public RequestCallback() {
    }

    public RequestCallback(int recurtionDeep, int recurtionOrder) {
        this.recurtionDeep = recurtionDeep;
        this.recurtionOrder = recurtionOrder;
    }

    public void success(String response) {
        done(response);
    }

    public void error(int code, String response) {

        Common.showToast(Common.formatString(R.string.server_error_with_code, code), Toast.LENGTH_SHORT);
        Log.d("REQUEST", "Error: " + (response != null ? response : "< No Response >"));
    }

    public void done(String response) {
        // silence is golden
        Log.d("REQUEST", "Processed: " + (response != null ? response : "< No Response >"));
    }

}
