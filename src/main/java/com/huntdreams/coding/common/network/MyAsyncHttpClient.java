package com.huntdreams.coding.common.network;

import android.content.Context;

import com.huntdreams.coding.common.Global;
import com.huntdreams.coding.common.network.apache.CustomSSLSocketFactory;
import com.huntdreams.coding.common.network.apache.CustomX509TrustManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.conn.scheme.Scheme;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * 自定义网络操作类 AsyncHttpClient
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
public class MyAsyncHttpClient{

    public static AsyncHttpClient createClient(Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        client.setCookieStore(cookieStore);

        try{
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new CustomX509TrustManager()},
                    new SecureRandom());
            org.apache.http.conn.ssl.SSLSocketFactory ssf = new CustomSSLSocketFactory(sslContext);
            ssf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme scheme = new Scheme("https", ssf, 443);
            client.getHttpClient().getConnectionManager().getSchemeRegistry()
                    .register(scheme);


        } catch (Exception e) {
            Global.errorLog(e);
        }

        return client;
    }
}
