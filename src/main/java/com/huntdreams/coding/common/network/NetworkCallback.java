package com.huntdreams.coding.common.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络访问回调接口
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/22.
 */
public interface NetworkCallback {
    void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException;

    void getNetwork(String uri, String tag);
}
