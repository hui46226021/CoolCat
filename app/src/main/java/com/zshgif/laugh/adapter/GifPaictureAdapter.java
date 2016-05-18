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


import com.zshgif.laugh.model.GifitemBean;
import com.zshgif.laugh.model.PictureBean;
import com.zshgif.laugh.R;
import com.zshgif.laugh.acticty.ContextUtil;

import com.zshgif.laugh.fragment.BaseFragment;
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
public class GifPaictureAdapter extends ArrayAdapter<GifitemBean> {
    private int resourceId;
    private Context context;
    private int onScreen;

    private BaseFragment baseFragment;
    /**
     * 所有图片集合
     */
    private List<GifitemBean> gifitemBeanList;
    /**
     * 内部图片集合
     */
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    public GifPaictureAdapter(Context context, int resource) {
        super(context, resource);
    }

    public GifPaictureAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }



    public GifPaictureAdapter(Context context, int resource, List<GifitemBean> objects, BaseFragment baseFragment) {
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
        GifitemBean  gifitemBean =  gifitemBeanList.get(position);
        geiBitmap(gifitemBean.getReleaseUser().getUserProfile(),holder.user_profile,position);
        holder.user_name.setText(gifitemBean.getReleaseUser().getUsername());
        holder.tital.setText(gifitemBean.getContent());
        holder.digg_count.setText(gifitemBean.getDigg_count()+"");
        holder.bury_count.setText(gifitemBean.getBury_count()+"");
        holder.comment_count.setText(gifitemBean.getComments_count()+"");
        holder.type.setText(gifitemBean.getCategory_name());
        holder.picture.setVisibility(View.GONE);
        holder.gridView.setVisibility(View.GONE);
        holder.gif_picture.setVisibility(View.GONE);
        holder.gif_picture.setImageBitmap(null);
        holder.picture.setImageResource(R.drawable.bg);

        /**
         * 评论
         */
        if (gifitemBean.getComments()!=null) {
            holder.comments_layout.setVisibility(View.VISIBLE);
            holder.comments_user.setText(gifitemBean.getComments().getCommentUserName());
            holder.comments_text.setText(gifitemBean.getComments().getComment());
            geiBitmap(gifitemBean.getComments().getCommentUserProfile(), holder.comments_user_profile,position);
        }else {
            holder.comments_layout.setVisibility(View.GONE);
        }
        /**
         * 图片处理
         */
        switch (gifitemBean.getType()){
            case 1:
                holder.picture.setVisibility(View.VISIBLE);
                calculateHeight( holder.picture,((double) gifitemBean.getHeight())/((double)gifitemBean.getWidth()));
                geiBitmap(gifitemBean.getFirstOne(),holder.picture,position);
                break;
            case 3:
                holder.picture.setVisibility(View.VISIBLE);
                calculateHeight( holder.picture,((double) gifitemBean.getHeight())/((double)gifitemBean.getWidth()));
                calculateHeight( holder.gif_picture,((double) gifitemBean.getHeight())/((double)gifitemBean.getWidth()));
                geiBitmap(gifitemBean.getFirstOne(),holder.picture,position);
                geiGifPicture(gifitemBean.getGifUrl(),holder.gif_picture,holder.picture,position);
                break;
            case 5:
                holder.picture.setVisibility(View.GONE);
                holder.gif_picture.setVisibility(View.GONE);
                holder.gridView.setVisibility(View.VISIBLE);
                String [] from ={"image"};
                int [] to = {R.id.image};
                getData(gifitemBean.getLarge_image_list());
                holder.gridView.setAdapter(new SimpleAdapter(context, data_list, R.layout.gridview, from, to));
                break;
        }



        return convertView;
    }


    public List<Map<String, Object>> getData(List<PictureBean> list){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<list.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.loadimage);
            data_list.add(map);
        }

        return data_list;
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


                WeakReference  weakReference = new  WeakReference( BitmapFactory.decodeByteArray(bytes, 0, bytes.length) );//弱引用
                imageView.setImageBitmap((Bitmap) weakReference.get());
            }

            @Override
            public void onHttpError(Exception e) {

            }
        });
    }

    void geiGifPicture(String url,final GifImageView gifImageView,final ImageView imageView,final int position){

        HttpPictureUtils.getNetworkBitmap(baseFragment,position,url, new NetworkBitmapCallbackListener() {
            @Override
            public void onHttpFinish(byte[] bytes) {

                if (bytes ==null){
                    return;
                }
              if(!isload(position))  {
                    return;
                }
                try {

                    WeakReference  weakReference = new WeakReference(  new GifDrawable( bytes ) );//弱引用
                    gifImageView.setImageDrawable((Drawable) weakReference.get());
                    gifImageView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }

            @Override
            public void onHttpError(Exception e) {

            }
        });
    }

    /**
     * 计算图片高度
     */
    void calculateHeight(View view,double ratio){
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams();
        if (ratio>0){
            linearParams.weight = (int)(ContextUtil.getInstance().getScreenWidth()*0.9);
            linearParams.height = (int)(ContextUtil.getInstance().getScreenWidth()*0.9*ratio);
        }
        view.setLayoutParams(linearParams);
    }

    boolean isload(int position){

        if (position<baseFragment.FIRST_ONE||position>baseFragment.LAST_ONE){
            LogUtils.e("当前"+position,"第一个"+baseFragment.FIRST_ONE+"--"+"最后一个"+baseFragment.LAST_ONE);
            return false;
        }
        return true;
    }
}
