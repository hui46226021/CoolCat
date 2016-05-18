package com.zshgif.laugh.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zshgif.laugh.R;
import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.bean.DuanZiBean;
import com.zshgif.laugh.bean.GifitemBean;
import com.zshgif.laugh.bean.PictureBean;
import com.zshgif.laugh.fragment.BaseFragment;
import com.zshgif.laugh.fragment.DuanZiFragment;
import com.zshgif.laugh.fragment.GifPictureFragment;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;
import com.zshgif.laugh.utils.HttpPictureUtils;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.view.RoundedImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


/**
 * GIF图片适配器
 * Created by zhush on 2016/5/15
 */
public class DuanZiAdapter extends ArrayAdapter<DuanZiBean> {
    private int resourceId;
    private Context context;
    private int onScreen;

    private BaseFragment baseFragment;
    /**
     * 所有图片集合
     */
    private List<DuanZiBean> gifitemBeanList;
    /**
     * 内部图片集合
     */
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    public DuanZiAdapter(Context context, int resource) {
        super(context, resource);
    }

    public DuanZiAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }



    public DuanZiAdapter(Context context, int resource, List<DuanZiBean> objects, BaseFragment baseFragment) {
        super(context, resource, objects);
        this.context =context;
        resourceId = resource;
        gifitemBeanList = objects;
       this.baseFragment=baseFragment;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        onScreen = position;
        GifPaictureHodler holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    resourceId, null);
            holder = new GifPaictureHodler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GifPaictureHodler) convertView.getTag();
        }
        /**
         * 公用
         */
        DuanZiBean  duanZiBean =  gifitemBeanList.get(position);
        geiBitmap(duanZiBean.getReleaseUser().getUserProfile(),holder.user_profile,position);
        holder.user_name.setText(duanZiBean.getReleaseUser().getUsername());
        holder.tital.setText(duanZiBean.getContent());
        holder.digg_count.setText(duanZiBean.getDigg_count()+"");
        holder.bury_count.setText(duanZiBean.getBury_count()+"");
        holder.comment_count.setText(duanZiBean.getComments_count()+"");
        holder.type.setText(duanZiBean.getCategory_name());
        holder.picture.setVisibility(View.GONE);
        holder.gridView.setVisibility(View.GONE);
        holder.gif_picture.setVisibility(View.GONE);


        /**
         * 评论
         */
        if (duanZiBean.getComments()!=null) {
            holder.comments_layout.setVisibility(View.VISIBLE);
            holder.comments_user.setText(duanZiBean.getComments().getCommentUserName());
            holder.comments_text.setText(duanZiBean.getComments().getComment());
            geiBitmap(duanZiBean.getComments().getCommentUserProfile(), holder.comments_user_profile,position);
        }else {
            holder.comments_layout.setVisibility(View.GONE);
        }




        return convertView;
    }



    /**
     * 适配缓存
     */
    public class GifPaictureHodler {

        RoundedImageView user_profile;//发布者头像
        TextView user_name;//发布者名称
        TextView tital;//标题
        GridView gridView; //图片组
        TextView type;//图片分类
        ImageView picture;//图片
        GifImageView gif_picture;//GIF图片
        LinearLayout comments_layout;//评论区布局

        RoundedImageView comments_user_profile;//评论人头像
        TextView comments_user;//评论人名称
        TextView comments_text;//评论

        Button digg_count;//点赞按钮
        Button bury_count;//鄙视按钮
        Button comment_count;//评论按钮
        public GifPaictureHodler(View view) {
            user_profile = (RoundedImageView) view.findViewById(R.id.user_profile);
            user_name = (TextView) view.findViewById(R.id.user_name);
            gridView = (GridView) view.findViewById(R.id.gridView);
            tital = (TextView) view.findViewById(R.id.tital);
            type = (TextView) view.findViewById(R.id.type);
            picture = (ImageView) view.findViewById(R.id.picture);
            gif_picture = (GifImageView) view.findViewById(R.id.gif_picture);
            comments_layout = (LinearLayout) view.findViewById(R.id.comments_layout);
            comments_user_profile = (RoundedImageView) view.findViewById(R.id.comments_user_profile);
            comments_user= (TextView) view.findViewById(R.id.comments_user);
            comments_text= (TextView) view.findViewById(R.id.comments_text);
            digg_count = (Button) view.findViewById(R.id.digg_count);
            bury_count = (Button) view.findViewById(R.id.bury_count);
            comment_count = (Button) view.findViewById(R.id.comment_count);
        }
    }

    void geiBitmap(String url,final ImageView imageView,final int position){

        HttpPictureUtils.getNetworkBitmap(baseFragment,position,url, new NetworkBitmapCallbackListener() {
            @Override
            public void onHttpFinish(byte[] bytes) {

                if (bytes ==null){
                    return;
                }
                if(!isload(position))  {
                    return;
                }

                Bitmap softReference =BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                WeakReference  weakReference = new  WeakReference( softReference );

                imageView.setImageBitmap((Bitmap) weakReference.get());
            }

            @Override
            public void onHttpError(Exception e) {

            }
        });
    }




    boolean isload(int position){

        if (position< baseFragment.FIRST_ONE||position>baseFragment.LAST_ONE){
            return false;
        }
        return true;
    }
}
