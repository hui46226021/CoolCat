package com.zshgif.laugh.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.lidroid.xutils.exception.HttpException;
import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.listener.HttpCallbackListener;
import com.zshgif.laugh.utils.LogCollect;
import com.zshgif.laugh.utils.LogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网络下载数据
 */
public class HttpUtils {

    /**
     * 初始化httpClient
     */
    public static HttpClient httpClient = null;
    private static int CONNECTION_TIMEOUT = 50000;// 设置连接超时时间3s
    private static int SO_TIMEOUT = 30000;// 数据传输超时时间30s




    /**
     * 通知
     */
    static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:

                    Toast.makeText(ContextUtil.getInstance(), "当前网络状况不好，请稍后尝试", Toast.LENGTH_SHORT)
                            .show();

                    break;
                default:
                    break;
            }
        }

    };

    static {

        httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);// 设置连接超时时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                SO_TIMEOUT);// 数据传输超时时间

       /* // 添加对ssl的支持
        httpClient = WebClientDevWrapper.wrapClient(httpClient);*/

    }




    /**
     * 发送post 请求
     *
     * @param address
     * @param param
     * @param listener
     */

    public static void sendHttpRequestDoPost(final String address, final Map<String, String> param, final HttpCallbackListener listener, final Activity activty) {
        if (!NetWorkUtils.isConn(ContextUtil.getInstance())) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                doPostSynchroonized(address, param, listener,activty);
            }
        }).start();
    }

    /**
     * 同步锁方法
     *
     * @param address
     * @param param
     * @param listener
     */
    public static synchronized void doPostSynchroonized(final String address, final Map<String, String> param, final HttpCallbackListener listener,final Activity activty) {

        LogUtils.e("url", address);
        Set<String> set = param.keySet();
        StringBuilder strpar = new StringBuilder("[");
        for (String key : set) {

            strpar.append(key + "=" + param.get(key) + ",");
        }
        LogUtils.e("url", strpar.toString() + "]");

        try {

          final   String response = doPost(address, param);
            if(response.contains("<html><head><title>")){
                if (listener != null) {
                    listener.onHttpError(new HttpException());
                    LogCollect.logCollect("HttpUtils", "sendHttpRequest","网络请求报错", address+"||"+response);

                }
            }
            if (listener != null) {
                // 回调onFinish()方法
                LogUtils.e("url返回", response);
                activty.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onHttpFinish(response.toString());
                    }
                });

            }
        } catch (final SocketTimeoutException e) {

            if (listener != null) {

                activty.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onHttpError(e);
                        e.printStackTrace();
                    }
                });



            }
        } catch (final  Exception e) {

            if (listener != null) {

                activty.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onHttpError(e);
                        e.printStackTrace();
                    }
                });
            }
        }
    }


    public static String doPost(String url, Map params) throws IOException {

        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, (String) params.get(key)));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpEntity entity = httpClient.execute(post).getEntity();

        String body = null;
        body = EntityUtils.toString(entity);
        LogUtils.e("response", "response=" + body);
        return body;
    }





}
