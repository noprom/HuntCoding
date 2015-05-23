package com.huntdreams.coding.common.network;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huntdreams.coding.FootUpdate;
import com.huntdreams.coding.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment基类
 * 封装了图片下载和网络请求
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 *          Created by noprom on 2015/5/22.
 */
public class BaseFragment extends Fragment implements NetworkCallback ,FootUpdate.LoadMore{


    public BaseFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }


    @Override
    public void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException {

    }

    @Override
    public void getNetwork(String url, String tag) {

    }

    @Override
    public void loadMore() {

    }
}
