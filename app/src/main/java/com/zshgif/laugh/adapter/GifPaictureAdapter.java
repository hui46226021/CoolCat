package com.zshgif.laugh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.zshgif.laugh.model.GifitemBean;
import com.zshgif.laugh.model.PictureBean;
import com.zshgif.laugh.R;
import com.zshgif.laugh.acticty.ContextUtil;

import com.zshgif.laugh.fragment.BaseFragment;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;
import com.zshgif.laugh.http.HttpPictureUtils;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.view.RoundedImageView;

import net.youmi.android.spot.CustomerSpotView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
     * 原生插屏控件
     */
    CustomerSpotView mCustomerSpotView = null;
    /**
     * 所有图片集合
     */
    private List<GifitemBean> gifitemBeanList;
    /**
     * 图片队列   最多存放20个图片对象 当图片超过20个的时候 取出一个recycle()
     */
    private Queue<Bitmap> queue = new LinkedList<Bitmap>();

    /**
     * GIF图片队列   最多存放3个图片对象 当图片超过3个的时候 停止动画
     */
    private Queue<GifDrawable> queue_gif = new LinkedList<GifDrawable>();
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
        this.context = context;
        resourceId = resource;
        gifitemBeanList = objects;
        this.baseFragment = baseFragment;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * 公用
         */
        GifitemBean gifitemBean = gifitemBeanList.get(position);

        if (gifitemBean==null) {
            return setNativeSpotAd(convertView);
        }
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
         * 如果holder等于null说明此convertView 是之前广告用过的 要重新加载下布局
         */
        if(holder==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    resourceId, null);
            holder = new GifPaictureHodler(convertView);
            convertView.setTag(holder);
        }


        holder.user_name.setText(gifitemBean.getReleaseUser().getUsername());
        holder.tital.setText(gifitemBean.getContent());
        holder.digg_count.setText(gifitemBean.getDigg_count() + "");
        holder.bury_count.setText(gifitemBean.getBury_count() + "");
        holder.comment_count.setText(gifitemBean.getComments_count() + "");
        holder.progressBar.setProgress(0);
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.type.setText(gifitemBean.getCategory_name());
        holder.picture.setVisibility(View.GONE);
        holder.gridView.setVisibility(View.GONE);
        holder.gif_picture.setVisibility(View.GONE);
        /**
         * 初始化默认图片
         */
        holder.gif_picture.setImageResource(R.drawable.bg);
        holder.picture.setImageResource(R.drawable.bg);
        holder.comments_user_profile.setImageResource(R.drawable.mrtx);
        holder.user_profile.setImageResource(R.drawable.mrtx);
        geiBitmap(gifitemBean.getReleaseUser().getUserProfile(), holder.user_profile, position);
        /**
         * 评论
         */
        if (gifitemBean.getComments() != null) {
            holder.comments_layout.setVisibility(View.VISIBLE);
            holder.comments_user.setText(gifitemBean.getComments().getCommentUserName());
            holder.comments_text.setText(gifitemBean.getComments().getComment());
            geiBitmap(gifitemBean.getComments().getCommentUserProfile(), holder.comments_user_profile, position);
        } else {
            holder.comments_layout.setVisibility(View.GONE);
        }
        /**
         * 图片处理
         */
        switch (gifitemBean.getType()) {
            case 1:
                //普通图片
                holder.picture.setVisibility(View.VISIBLE);
                calculateHeight(holder.picture, ((double) gifitemBean.getHeight()) / ((double) gifitemBean.getWidth()));
                geiBitmap(gifitemBean.getFirstOne(), holder.picture, position);
                break;
            case 2:
                //动图
                holder.picture.setVisibility(View.VISIBLE);
                calculateHeight(holder.picture, ((double) gifitemBean.getHeight()) / ((double) gifitemBean.getWidth()));
                calculateHeight(holder.gif_picture, ((double) gifitemBean.getHeight()) / ((double) gifitemBean.getWidth()));
                geiBitmap(gifitemBean.getFirstOne(), holder.picture, position);

                geiGifPicture(gifitemBean.getGifUrl(), holder.gif_picture, holder.picture, position, holder.progressBar);

                break;
            case 5:
//                holder.picture.setVisibility(View.GONE);
//                holder.gif_picture.setVisibility(View.GONE);
//                holder.gridView.setVisibility(View.VISIBLE);
//                String[] from = {"image"};
//                int[] to = {R.id.image};
//                getData(gifitemBean.getLarge_image_list());
//                holder.gridView.setAdapter(new SimpleAdapter(context, data_list, R.layout.gridview, from, to));
                break;
        }


        return convertView;
    }

    /**
     * 适配多图那种 暂时没用
     * @param list
     * @return
     */
//    public List<Map<String, Object>> getData(List<PictureBean> list) {
//        //cion和iconName的长度是相同的，这里任选其一都可以
//        for (int i = 0; i < list.size(); i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("image", R.drawable.loadimage);
//            data_list.add(map);
//        }
//
//        return data_list;
//    }

    /**
     * hodler
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
        ProgressBar progressBar;//进度条

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
            comments_user = (TextView) view.findViewById(R.id.comments_user);
            comments_text = (TextView) view.findViewById(R.id.comments_text);
            digg_count = (Button) view.findViewById(R.id.digg_count);
            bury_count = (Button) view.findViewById(R.id.bury_count);
            comment_count = (Button) view.findViewById(R.id.comment_count);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);


        }
    }

    /**
     * 获取普通图片
     * @param url
     * @param imageView
     * @param position
     */
    void geiBitmap(String url, final ImageView imageView, final int position) {
        imageView.setTag(url);

        HttpPictureUtils.getNetworkBitmap(null, imageView, baseFragment, position, url, new NetworkBitmapCallbackListener() {

            @Override
            public void onHttpFinish(byte[] bytes) {

                if (bytes == null) {
                    return;
                }
                WeakReference weakReference = new WeakReference(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));//弱引用
                imageView.setImageBitmap((Bitmap) weakReference.get());
                queue.offer((Bitmap) weakReference.get());
                recycleBitmap();
            }

            @Override
            public void onHttpError(Exception e) {

            }
        });
    }

    /**
     * 获取 GIF图片
     * @param url
     * @param gifImageView
     * @param imageView
     * @param position
     * @param progressBar
     */
    void geiGifPicture(String url, final GifImageView gifImageView, final ImageView imageView, final int position, final ProgressBar progressBar) {


        gifImageView.setTag(url);
        if (!isload(position)) {

            return;
        }
        HttpPictureUtils.getNetworkBitmap(progressBar, gifImageView, baseFragment, position, url, new NetworkBitmapCallbackListener() {
            @Override
            public void onHttpFinish(byte[] bytes) {

                if (bytes == null) {
                    return;
                }
                if (!isload(position)) {
                    return;
                }


                try {
                    WeakReference weakReference = new WeakReference(new GifDrawable(bytes));//弱引用
                    bytes = null;
                    gifImageView.setImageDrawable((Drawable) weakReference.get());
                    gifImageView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    queue_gif.offer((GifDrawable) weakReference.get());
                    stopGifDrawable();
                } catch (IOException e) {
//                    e.printStackTrace();
                }


            }

            @Override
            public void onHttpError(Exception e) {

            }
        });
    }

    /**
     * 计算图片高度
     */
    void calculateHeight(View view, double ratio) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (ratio > 0) {
            linearParams.weight = (int) (ContextUtil.getInstance().getScreenWidth() * 0.9);
            linearParams.height = (int) (ContextUtil.getInstance().getScreenWidth() * 0.9 * ratio);
        }
        view.setLayoutParams(linearParams);
    }

    boolean isload(int position) {

        if (position < baseFragment.FIRST_ONE || position > baseFragment.LAST_ONE) {
            return false;
        }

        return true;
    }

    /**
     * 销毁Bitmap(
     */
    synchronized void recycleBitmap() {
        if (queue.size() > 20) {

            try {
                Bitmap bitmap = queue.poll();
                bitmap.recycle();
                bitmap = null;
                LogUtils.e("删除了一张图片", "当前size=" + queue.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 销毁GIF图片
     */
    synchronized void stopGifDrawable() {
        LogUtils.e("queue_gif长度", "" + queue_gif.size());
        if (queue_gif.size() > 5) {

            try {
                GifDrawable gifDrawable = queue_gif.poll();
                gifDrawable.stop();
                gifDrawable.setCallback(null);
                gifDrawable.recycle();
                gifDrawable = null;
                LogUtils.e("删除了一张GIF", "当前size=" + queue_gif.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 设置原生插屏广告
     */
    public View setNativeSpotAd(View convertView) {
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_ymad, null);

       final RelativeLayout mNativeAdLayout = (RelativeLayout) convertView.findViewById(R.id.rl_native_ad);


        // 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图
        SpotManager.getInstance(context)
                .setSpotOrientation(SpotManager.ORIENTATION_LANDSCAPE);




        new Thread(new Runnable() {
            @Override
            public void run() {
                //切换到子线程获取原生控件
                mCustomerSpotView = SpotManager.getInstance(context)
                        .cacheCustomerSpot(context, new SpotDialogListener() {
                            @Override
                            public void onShowSuccess() {
                                Log.i("", "原生插屏展示成功");
                            }

                            @Override
                            public void onShowFailed() {
                                Log.i("", "原生插屏展示失败");
                            }

                            @Override
                            public void onSpotClosed() {
                                Log.i("", "原生插屏被关闭");
                            }

                            @Override
                            public void onSpotClick(boolean isWebPath) {
                                Log.i("", "原生插屏被点击，isWebPath = " + isWebPath);
                            }
                        });
                //获取成功
                Activity activity = (Activity)context;
                if (mCustomerSpotView != null) {
                    //切换到UI线程

                   activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RelativeLayout.LayoutParams params =
                                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            if (mNativeAdLayout != null) {
                                mNativeAdLayout.removeAllViews();
                                mNativeAdLayout.addView(mCustomerSpotView, params);
                            }
                        }
                    });
                } else {
                    //获取失败
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            convertView.setVisibility(View.GONE);
//                        }
//                    });
                }
            }
        }).start();

        return convertView;

    }
}
