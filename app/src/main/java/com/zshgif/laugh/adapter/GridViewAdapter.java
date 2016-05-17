package com.zshgif.laugh.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zshgif.laugh.R;
import com.zshgif.laugh.bean.PictureBean;
import com.zshgif.laugh.fragment.GifPictureFragment;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;
import com.zshgif.laugh.utils.HttpPictureUtils;
import com.zshgif.laugh.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class GridViewAdapter extends ArrayAdapter<PictureBean> {

    private Context context;
    List<PictureBean> list;
    int listPosition;

    public GridViewAdapter(Context context, int resource, int textViewResourceId, List<PictureBean> objects,int listPosition) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.listPosition = listPosition;
        list = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PictureBean pictureBean= list.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview, parent, false);
            holder.iv_goods_picture = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        geiBitmap(pictureBean.getThumbImage(),holder.iv_goods_picture,listPosition);


        return convertView;
    }

    class Holder {

        ImageView iv_goods_picture;
    }

    void geiBitmap(String url,final ImageView imageView,final int position){

        HttpPictureUtils.getNetworkBitmap(position,url, new NetworkBitmapCallbackListener() {
            @Override
            public void onHttpFinish(byte[] bytes) {

                if (bytes ==null){
                    return;
                }
                if(!isload(position))  {
                    return;
                }

                Bitmap softReference = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                WeakReference weakReference = new  WeakReference( softReference );
                softReference=null; //弱引用
                imageView.setImageBitmap((Bitmap) weakReference.get());
            }

            @Override
            public void onHttpError(Exception e) {

            }
        });
    }
    boolean isload(int position){

        if (position< GifPictureFragment.FIRST_ONE||position>GifPictureFragment.LAST_ONE){
            LogUtils.e("当前"+position,"第一个"+GifPictureFragment.FIRST_ONE+"--"+"最后一个"+GifPictureFragment.LAST_ONE);
            return false;
        }
        return true;
    }
}
