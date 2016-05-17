package com.zshgif.laugh.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.adapter.GifPaictureAdapter;
import com.zshgif.laugh.bean.CommentsBean;
import com.zshgif.laugh.bean.GifitemBean;
import com.zshgif.laugh.bean.PictureBean;
import com.zshgif.laugh.bean.ReleaseUser;
import com.zshgif.laugh.listener.HttpCallbackListener;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.HttpUtils;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.utils.encryption.Md5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhush on 2016/5/15
 * gif图片页面.
 */
public class GifPictureFragment extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.listview)
    ListView listview;
    @ViewInject(R.id.id_swiperefreshlayout)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * 图片对象 集合
     */
    private List<GifitemBean> list = new ArrayList<GifitemBean>();
    /**
     * 适配器
     */
    private GifPaictureAdapter gifPaictureAdapter;
    public GifPictureFragment() {
    }


    public static GifPictureFragment newInstance() {
        GifPictureFragment fragment = new GifPictureFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //初始化控件
        View view = inflater.inflate(R.layout.fragment_gif_picture, container, false);
        ViewUtils.inject(this, view);
        initData();
        settingView();
        return view;
    }

    /**
     * 初始化数据
     */
    void initData(){

    }

    /**
     *  设置控件
     */
    void settingView(){
        gifPaictureAdapter=   new GifPaictureAdapter(getActivity(),R.layout.item_main,list);
        listview.setAdapter(gifPaictureAdapter);
    }

    /**
     * 设置刷新按钮
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_yellow_light, R.color.main_yellow_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 刷新回调
     */
    @Override
    public void onRefresh() {
        // 刷新时模拟数据的变化
//        new Handler().postDelayed(new Runnable() {
//            @Override public void run() {
//                mSwipeRefreshLayout.setRefreshing(false);
//                int temp = (int) (Math.random() * 10);
//
//            }
//        }, 1000);
        HashMap<String,String> param = new HashMap<>();
        param.put("group_id","6496629855");
        param.put("item_id","6496629855");
        param.put("count","30");
        param.put("offset","0");
        param.put("iid","4079531978");
        param.put("device_id","5807013269");
        param.put("ac","wifi");
        param.put("channel","smartisan");
        param.put("aid","7");
        param.put("app_name","joke_essay");
        param.put("version_code","500");
        param.put("version_name","5.0.0");
        param.put("device_platform","android");
        param.put("ssmix","a");
        param.put("device_type","YQ601");
        param.put("os_api","22");
        param.put("os_version","5.1.1");
        param.put("uuid","865790020929429");
        param.put("openudid","ea45d1407bdfba67");
        param.put("manifest_version_code","500");
        HttpUtils.sendHttpRequestDoPost(Constant.GET_PICTriURE_LIST_URL, param, new HttpCallbackListener() {
            @Override
            public void onHttpFinish(String response) {

                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    analysisJSON(response);
                } catch (JSONException e) {
                    setToastMessage("解析数据错误");
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpError(Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },getActivity());
    }

    /**
     * 解析JSON
     * @param response
     * @throws JSONException
     */
    void analysisJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if(!"success".equals(jsonObject.getString("message"))){
            setToastMessage("服务器异常");
            return;
        }
        list.clear();
        JSONObject dataObject =jsonObject.getJSONObject("data");
        JSONArray dataArray =dataObject.getJSONArray("data");
        for (int i = 0;i<dataArray.length();i++ ){
            JSONObject jsonObjectItem = dataArray.getJSONObject(i);
            //图片项目type=1  广告type=5
            if (jsonObjectItem.getInt("type")!=1){
                continue;
            }
            JSONObject group =jsonObjectItem.getJSONObject("group");
            int type = group.getInt("type");
            switch (type){
                case 1:
                    //普通单张 图片
                    singlePicture(group,jsonObjectItem.getJSONArray("comments"));
                    break;
                case 3:
                    //GIF 图片
                    singlePicture(group,jsonObjectItem.getJSONArray("comments"));
                    break;
                case 5:
                    //多张图片集合
                    break;
            }

        }
        setToastMessage("更新了"+list.size()+"组图片└(^o^)┘");
        gifPaictureAdapter.notifyDataSetChanged();
    }

    /**
     * 单张图片解析 type =1  type =3 GIF
     */
    private void singlePicture(JSONObject jsonObject,JSONArray comments){
        GifitemBean gifitemBean = new GifitemBean();
        try {
            gifitemBean.setType(jsonObject.getInt("type"));//图片类型
            gifitemBean.setId("id");
            gifitemBean.setContent(jsonObject.getString("text"));//图片描述
            gifitemBean.setCategory_name(jsonObject.getString("category_name"));//分类
            gifitemBean.setDigg_count(jsonObject.getInt("digg_count"));
            gifitemBean.setBury_count(jsonObject.getInt("bury_count"));
            gifitemBean.setComments_count(jsonObject.getInt("comment_count"));
            gifitemBean.setShare_url(jsonObject.getString("share_url"));

            String firstOne = jsonObject.getJSONObject("middle_image").getJSONArray("url_list").getJSONObject(0).getString("url");//第一张图片
            gifitemBean.setFirstOne(firstOne);
            gifitemBean.setFirstOneKey(Md5.getMd5Quick(firstOne));
            if (jsonObject.getInt("type")==3){
                // type =3的时候有个GIF的地址
                String gifUrl = jsonObject.getJSONObject("large_image").getJSONArray("url_list").getJSONObject(0).getString("url");//gifUrl
                gifitemBean.setGifUrl(gifUrl);
                gifitemBean.setGifUrlKey(Md5.getMd5Quick(gifUrl));
            }

            gifitemBean.setWidth(jsonObject.getJSONObject("large_image").getInt("width"));
            gifitemBean.setHeight(jsonObject.getJSONObject("large_image").getInt("height"));


            /**
             * 发布者和评论  最后将项目对象放到集合
             */
            releaseUserAndcomments(gifitemBean,jsonObject.getJSONObject("user"),comments);


        } catch (JSONException e) {
            LogUtils.e("错误的JSON",jsonObject.toString());
//            e.printStackTrace();
        }

    }

    /**
     * 多张图片解析 type =5
     */
    private void morePicture(JSONObject jsonObject,JSONArray comments){
        GifitemBean gifitemBean = new GifitemBean();
        try {
            gifitemBean.setType(jsonObject.getInt("type"));//图片类型
            gifitemBean.setId("id");
            gifitemBean.setContent(jsonObject.getString("text"));//图片描述
            gifitemBean.setCategory_name(jsonObject.getString("category_name"));//分类
            gifitemBean.setDigg_count(jsonObject.getInt("digg_count"));
            gifitemBean.setBury_count(jsonObject.getInt("bury_count"));
            gifitemBean.setComments_count(jsonObject.getInt("comment_count"));
            gifitemBean.setShare_url(jsonObject.getString("share_url"));


            JSONArray thumbImageList = jsonObject.getJSONArray("thumb_image_list");
            JSONArray largeImageList = jsonObject.getJSONArray("large_image_list");
            JSONObject thumbImage = null;
            JSONObject largeImage = null;
            List<PictureBean> picturelist =  new ArrayList<>();
            for (int i = 0;i<thumbImageList.length();i++){
                largeImage =largeImageList.getJSONObject(i);
                String largeImageUrl = largeImage.getString("url");
                thumbImage =thumbImageList.getJSONObject(i);
                String thumbImageUrl = thumbImage.getString("url");
                picturelist.add(new PictureBean(largeImageUrl,Md5.getMd5Quick(largeImageUrl),thumbImageUrl,Md5.getMd5Quick(thumbImageUrl)));
            }
            gifitemBean.setLarge_image_list(picturelist);

            /**
             * 发布者和评论  最后将项目对象放到集合
             */
            releaseUserAndcomments(gifitemBean,jsonObject.getJSONObject("user"),comments);
        } catch (JSONException e) {
            LogUtils.e("错误的JSON",jsonObject.toString());
            e.printStackTrace();
        }
    }

    /**
     * 发布者和评论者
     * @param gifitemBean
     * @param userObject
     * @param comments
     */
    private void releaseUserAndcomments(GifitemBean gifitemBean,JSONObject userObject,JSONArray comments) throws JSONException {
        /**
         * 发布者
         */
        ReleaseUser releaseUser = new ReleaseUser();
        releaseUser.setUserProfile(userObject.getString("avatar_url"));
        releaseUser.setUsername(userObject.getString("name"));
        releaseUser.setUserProfileKey(Md5.getMd5(userObject.getString("avatar_url"),true,"UTF-8"));
        gifitemBean.setReleaseUser(releaseUser);

        /**
         * 评论这信息
         */
        if (comments.length()!=0){
            JSONObject commentsJson = comments.getJSONObject(0);
            CommentsBean commentsBean = new CommentsBean();
            commentsBean.setComment(commentsJson.getString("text"));
            commentsBean.setCommentUserName(commentsJson.getString("user_name"));
            commentsBean.setCommentUserProfile(commentsJson.getString("avatar_url"));
            commentsBean.setCommentUserProfileKey(Md5.getMd5(commentsJson.getString("avatar_url"),true,"UTF-8"));
            gifitemBean.setComments(commentsBean);
        }
        list.add(gifitemBean);

    }
}
