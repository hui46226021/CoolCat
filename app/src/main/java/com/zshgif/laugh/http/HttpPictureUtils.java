package com.zshgif.laugh.http;

import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ProgressBar;

import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.cache.DiskLruCacheUtil;
import com.zshgif.laugh.fragment.BaseFragment;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;
import com.zshgif.laugh.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
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
//        // 获取应用程序最大可用内存
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        /**
         * 内存中最多放10找那个图片
         */
        mMemoryCache = new LruCache<>(10);

    }


    /**
     * 获取网络图片
     * @param url
     * @param listener
     */
    public static void getNetworkBitmap(final ProgressBar progressBar,final View view, final BaseFragment baseFragment, final int position, final String url, final NetworkBitmapCallbackListener listener){

            if (url==null){
                return;
            }

           new AsyncTask<Void,Integer,byte[]>() {
            @Override
            protected byte[] doInBackground(Void... voids) {
                //让控件先出现30毫秒再开始加载图片 要不卡顿
                System.gc();
                if (!isload(position,baseFragment)){
                    return null;
                };



                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isload(position,baseFragment)){

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
                        bytes = HttpPictureUtils.toByteArray(inputStream);
                        mMemoryCache.put(url, bytes);
                        return bytes;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (!isload(position,baseFragment)){

                    return null;
                };



                LogUtils.e("网络获取图片",url);
                URL myFileURL;
                InputStream is = null;
                ByteArrayOutputStream output = null;
                WeakReference  weakReference = null;
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
                    //数据总大小
                    int length = conn.getContentLength();
                    is = conn.getInputStream();
                    //获得byte[]
                    output = new ByteArrayOutputStream();

                    byte[] buffer = new byte[4096];
                    int n = 0;
                    int count = 0;
                    while (-1 != (n = is.read(buffer))) {
                        output.write(buffer, 0, n);

                        if (!isload(position,baseFragment)){
                            conn.disconnect();
                            is.close();
                            output.close();
                            this.cancel(true);
                            return null;
                        };
                        //计算当前下载百分比
                        count += n;
                        publishProgress((int)(((float)count / length) * 100));

                    }
                    buffer = null;
                    bytes = output.toByteArray();
                    /**
                     * 存入缓存
                     */

                    DiskLruCacheUtil.writeToDiskCache(url,bytes,ContextUtil.getInstance());
                    mMemoryCache.put(url, bytes);


                }catch (Exception e){

                }
                finally {
                    //关闭数据流
                    if (is!=null){
                        try {
                            is.close();
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                return  bytes;
            }
            @Override
            protected void onPostExecute(byte[] bytes) {
                try{
                    if (view.getTag().toString().equals(url)){

                        listener.onHttpFinish(bytes);
                    }
                    this.cancel(true);
                }catch (Exception e){}

            }


            @Override
            protected void onProgressUpdate(Integer... values) {

                super.onProgressUpdate(values);
                if (progressBar!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(values[0]);
                    if (values[0]==100){
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

               @Override
               protected void onCancelled() {
                   super.onCancelled();
                       if (view.getTag().toString().equals(url)){
                           listener.onHttpFinish(null);

               }}
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
        try {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;

        }

        return output.toByteArray();
    }

    /**
     * 验证当前控件在不在屏幕内
     * @param position
     * @return
     */
   static boolean isload(int position,BaseFragment baseFragment){
       if (position<baseFragment.FIRST_ONE||position>baseFragment.LAST_ONE){
           return false;
       }
        return true;
    }





}
