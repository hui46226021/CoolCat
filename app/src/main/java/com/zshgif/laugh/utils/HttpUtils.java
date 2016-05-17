package com.zshgif.laugh.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.acticty.SetThemeActivty;
import com.zshgif.laugh.listener.HttpCallbackListener;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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




    public static void getNetworkBitmap(final String url,final NetworkBitmapCallbackListener listener){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                byte[] bytes =  HttpUtils.getHttpBitmap(url);
//                listener.onHttpFinish(bytes);
//            }
//        }).start();
//       final byte[] bytes = null;



        new AsyncTask<Void,Integer,byte[]>() {
            @Override
            protected byte[] doInBackground(Void... voids) {
                /**
                 * 先冲缓存中查找
                 */
                InputStream inputStream =  DiskLruCacheUtil.readFromDiskCache(url,ContextUtil.getInstance());
                if(inputStream!=null){
                    try {
                        LogUtils.e("读取缓存",inputStream.toString());
                        return HttpUtils.toByteArray(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                byte[]  bytes =  HttpUtils.getHttpBitmap(url);
                DiskLruCacheUtil.writeToDiskCache(url,bytes,ContextUtil.getInstance());
                return bytes;
            }
            @Override
            protected void onPostExecute(byte[] bytes) {
                listener.onHttpFinish(bytes);
            }

        }.execute();

    }
    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static  byte[] getHttpBitmap(String url){

        LogUtils.e("网络获取图片",url);
        URL myFileURL;
//        Bitmap bitmap=null;
        byte[] bytes= null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //得到二进制数据
            bytes = HttpUtils.toByteArray(is);
//            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bytes;

    }

    /**
     * 将输入流转换为byt[]
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
