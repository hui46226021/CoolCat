package com.hyphenate.easeui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class HttpPictureUtils {


    /**
     * 获取网络图片
     * @param url

     */
    public static void ggetAvatarBitmap(final String url,final ImageView imageView,final Context context,final int defultImage){

            if (url==null){
                imageView.setImageResource(defultImage);
                return;
            }

           new AsyncTask<Void,Integer,byte[]>() {
            @Override
            protected byte[] doInBackground(Void... voids) {

                System.gc();

                byte[]  bytes = null;


                /**
                 * 先冲缓存中查找
                 */
                InputStream inputStream =  DiskLruCacheUtil.readFromDiskCache(url,context);
                if(inputStream!=null){
                    try {

                        bytes = HttpPictureUtils.toByteArray(inputStream);

                        return bytes;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                URL myFileURL;
                InputStream is = null;
                ByteArrayOutputStream output = null;
                WeakReference  weakReference = null;
                try{
                    myFileURL = new URL(url);
                    //获得连接
                    java.net.HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
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

                    while (-1 != (n = is.read(buffer))) {
                        output.write(buffer, 0, n);


                    }
                    buffer = null;
                    bytes = output.toByteArray();
                    /**
                     * 存入缓存
                     */

                    DiskLruCacheUtil.writeToDiskCache(url,bytes,context);



                }catch (Exception e){
                    e.printStackTrace();
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
                if (bytes==null){
                    imageView.setImageResource(defultImage);
                    return;
                }
                WeakReference weakReference = new WeakReference(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));//弱引用
                imageView.setImageBitmap((Bitmap) weakReference.get());


            }


            @Override
            protected void onProgressUpdate(Integer... values) {

                super.onProgressUpdate(values);

            }

               @Override
               protected void onCancelled() {
                   super.onCancelled();
                     }
               /**
                * executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); 线程将异步执行
                * execute()   线程将同步执行
                */
           }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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







}
