package com.huntdreams.coding.common.network;

/**
 * 封装好的网络操作类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/28.
 */
public class NetworkImpl {

    public static final int NETWORK_ERROR = -1;
    public static final int NEWWORK_ERROR_SERVICE = -2;
    private final NetworkCallback callback;

//    public HashMap<String,>
    public NetworkImpl(NetworkCallback callback) {
        this.callback = callback;
    }
}
