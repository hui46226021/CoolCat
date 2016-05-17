package com.zshgif.laugh.utils;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;

import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.fragment.GifPictureFragment;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/17.
 */
public class HttpPictureUtils {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, byte[]> mMemoryCache ;
    static {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<>(cacheSize);

    }
    /**
     * 获取网络图片
     * @param url
     * @param listener
     */
    public static void getNetworkBitmap(final int position,final String url,final NetworkBitmapCallbackListener listener){



        new AsyncTask<Void,Integer,byte[]>() {
            @Override
            protected byte[] doInBackground(Void... voids) {
                //让控件先出现30毫秒再开始加载图片 要不卡顿
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isload(position)){
                    return null;
                };
                byte[]  bytes = null;
                bytes = mMemoryCache.get(url);
                if (bytes!=null){
                    LogUtils.e("读取LruCache缓存",bytes.toString());
                    return bytes;
                }
                /**
                 * 先冲缓存中查找
                 */
                InputStream inputStream =  DiskLruCacheUtil.readFromDiskCache(url, ContextUtil.getInstance());
                if(inputStream!=null){
                    try {
                        LogUtils.e("读取硬盘缓存",inputStream.toString());
                        return HttpPictureUtils.toByteArray(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (!isload(position)){
                    return null;
                };

                LogUtils.e("网络获取图片",url);
                URL myFileURL;

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
                    //获得byte[]
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    byte[] buffer = new byte[4096];
                    int n = 0;
                    while (-1 != (n = is.read(buffer))) {
                        output.write(buffer, 0, n);
                        if (!isload(position)){
                            conn.disconnect();
                            is.close();
                            return null;
                        };

                    }
                    bytes = output.toByteArray();


                    //关闭数据流
                    is.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                try {
                    DiskLruCacheUtil.writeToDiskCache(url,bytes,ContextUtil.getInstance());
                    mMemoryCache.put(url, bytes);
                }catch (Exception e){

                }

                return bytes;
            }
            @Override
            protected void onPostExecute(byte[] bytes) {
                listener.onHttpFinish(bytes);
            }


            @Override
            protected void onProgressUpdate(Integer... values) {
                LogUtils.e("进度",values+"");
                super.onProgressUpdate(values);
            }
        }.execute();

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

    /**
     * 验证当前控件在不在屏幕内
     * @param position
     * @return
     */
   static boolean isload(int position){
       if (position<GifPictureFragment.FIRST_ONE||position>GifPictureFragment.LAST_ONE){
           return false;
       }
        return true;
    }
}
