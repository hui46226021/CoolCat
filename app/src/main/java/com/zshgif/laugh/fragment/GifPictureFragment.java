package com.zshgif.laugh.fragment;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.zshgif.laugh.model.CommentsBean;
import com.zshgif.laugh.model.GifitemBean;
import com.zshgif.laugh.model.PictureBean;
import com.zshgif.laugh.model.ReleaseUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.adapter.GifPaictureAdapter;

import com.zshgif.laugh.listener.HttpCallbackListener;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.dao.db.DBHelper;
import com.zshgif.laugh.http.HttpUtils;
import com.zshgif.laugh.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhush on 2016/5/15
 * gif图片页面.
 */
public class GifPictureFragment extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.listview)
    public  ListView listview;
    @ViewInject(R.id.id_swiperefreshlayout)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static GifPictureFragment instance;
    /**
     * 数据储存
     */
    private SharedPreferences preferences;

    public long first_one_id =6496629855l;



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
        instance = new GifPictureFragment();

        return instance;
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

        initOk =true;//初始化完成
        lazyLoad();
        return view;
    }
    @Override
    protected void lazyLoad() {
        if(!initOk || !isVisible) {
            return;
        }
        super.lazyLoad();
        initData();
            settingView();

    }

    @Override
    protected void unlazyLoad() {
        super.unlazyLoad();
        saveId();
    }

    /**
     * 初始化数据
     */
    void initData(){
        /**
         * 根据之前推出时候保存的 项目ID
         */
        preferences = getActivity().getSharedPreferences("gifpicture", getActivity().MODE_PRIVATE);
       int item_id = (int) preferences.getLong("item_id",-1l);
        if (item_id==-1){
            //如果之前 没有保存的ID 就刷新下
            mSwipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }
        /**
         * 查询 id前7个到最后一个倒叙
         */
        list=  DBHelper.loadAllGifitemBean(item_id);
        if (list.size()==0){
            return;
        }
        /**
         * 将当前第一个 项目的 段子ID 记录
         */
        try{ first_one_id = list.get(0).getNETid();}catch (Exception e){}
        for (GifitemBean gg:list){
            LogUtils.e("查询出",gg.getId()+"");
        }
    }

    /**
     *  设置控件
     */
    @TargetApi(Build.VERSION_CODES.M)
    void settingView(){
        gifPaictureAdapter=   new GifPaictureAdapter(getActivity(),R.layout.item_main,list,this);
        listview.setAdapter(gifPaictureAdapter);
        /**
         * 如果根据之前的ID 查出来的 大于7个 就定位到 原来的位置
         *
         * 查出ID的前7个倒叙 那原来的位置 就在倒数第7个
         */
        if (list.size()>BaseFragment.ALLOWANCE){
            listview.setSelection(list.size()-BaseFragment.ALLOWANCE+1);
        }

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FIRST_ONE =  listview.getFirstVisiblePosition()-1;
                LAST_ONE = listview.getLastVisiblePosition()+1;
                /**
                 * 当最后一个是倒数第五个的时候 加载
                 */
                try {
                    if (listview.getLastVisiblePosition() == (list.size()-1-5)){
                        /**
                         * 根据当前屏幕里最后一个 元素 的 数据库id 查询 后5到后25条
                         * 因为当前屏幕下面应该还有5个
                         */
                        int id = Integer.parseInt(list.get(LAST_ONE).getId()+"");
                        List listload=  DBHelper.loadAllGifitemBeanPushTen(id);
                        if (listload.size()>0){
                            list.addAll(listload) ;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gifPaictureAdapter.notifyDataSetChanged();
                                }
                            });

                        }

                    }
                }catch (Exception e){}



            }
        });
    }

    /**
     * 设置刷新按钮
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(setThemeColor2(),setThemeColor1());
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 刷新回调
     */
    @Override
    public void onRefresh() {
        // 刷新时模拟数据的变化
        try {
            first_one_id = list.get(0).getNETid();
        }catch (Exception e){

        }
        HashMap<String,String> param = new HashMap<>();
        param.put("group_id",first_one_id+"");
        param.put("item_id",first_one_id+"");
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


                try {
                    analysisJSON(response);
                } catch (Exception e) {
                    setToastMessage("解析数据错误");
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpError(Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 解析JSON
     * @param response
     * @throws JSONException
     */
    void analysisJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if(!"success".equals(jsonObject.getString("message"))){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setToastMessage("服务器异常");
                }
            });

            return;
        }
        list.clear();
        JSONObject dataObject =jsonObject.getJSONObject("data");
        JSONArray dataArray =dataObject.getJSONArray("data");
        for (int i = 0;i<dataArray.length();i++ ){
            if(i==6){
                /**
                 * 刷新的时候往第7个位置插入一个 空的对象   在适配器里如果 到空的这个就放一张广告
                 */
                list.add(null);
            }

            JSONObject jsonObjectItem = dataArray.getJSONObject(i);
            //图片项目type=1  广告type=5
            if (jsonObjectItem.getInt("type")!=1){
                continue;
            }
            JSONObject group =jsonObjectItem.getJSONObject("group");
            int type = group.getInt("media_type");
            switch (type){
                case 1:
                    //普通单张 图片
                    singlePicture(group,jsonObjectItem.getJSONArray("comments"));
                    break;
                case 2:
                    //GIF 图片
                    singlePicture(group,jsonObjectItem.getJSONArray("comments"));
                    break;
                case 3:
                    //视频

                    break;
                case 5:
                    //多张图片集合
                    morePicture(group,jsonObjectItem.getJSONArray("comments"));
                    break;
                default:
                    LogUtils.e("其他 media_type=",type+"");
            }

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                    FIRST_ONE =0;
                    if(pageState==true){

                        setToastMessage("更新了"+list.size()+"组图片└(^o^)┘");
                        gifPaictureAdapter.notifyDataSetChanged();
                        listview.setSelection(0);
                    }
                }catch (Exception e){}


            }
        });


    }

    /**
     * 单张图片解析 media_type =1  media_type =2 GIF
     */
    private void singlePicture(JSONObject jsonObject,JSONArray comments){
        GifitemBean gifitemBean = new GifitemBean();
        try {
            gifitemBean.setType(jsonObject.getInt("media_type"));//图片类型
            gifitemBean.setNETid(jsonObject.getLong("id"));
            gifitemBean.setContent(jsonObject.getString("text"));//图片描述
            gifitemBean.setCategory_name(jsonObject.getString("category_name"));//分类
            gifitemBean.setDigg_count(jsonObject.getInt("digg_count"));
            gifitemBean.setBury_count(jsonObject.getInt("bury_count"));
            gifitemBean.setComments_count(jsonObject.getInt("comment_count"));
            gifitemBean.setShare_url(jsonObject.getString("share_url"));



            if (jsonObject.getInt("media_type")==2){
                // type =3的时候有个GIF的地址
                String firstOne = jsonObject.getJSONObject("middle_image").getJSONArray("url_list").getJSONObject(0).getString("url");//第一张图片
                gifitemBean.setFirstOne(firstOne);
                String gifUrl = jsonObject.getJSONObject("large_image").getJSONArray("url_list").getJSONObject(0).getString("url");//gifUrl
                gifitemBean.setGifUrl(gifUrl);

            }
            if(jsonObject.getInt("media_type")==1){
                // type =1单张图片
                String firstOne = jsonObject.getJSONObject("large_image").getJSONArray("url_list").getJSONObject(0).getString("url");//第一张图片
                gifitemBean.setFirstOne(firstOne);
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
            gifitemBean.setNETid(jsonObject.getLong("id"));
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
                PictureBean pictureBean =   new PictureBean();
                pictureBean.setLargeImage(largeImageUrl);
                pictureBean.setThumbImage(thumbImageUrl);
                picturelist.add(pictureBean);
            }
//            gifitemBean.setLarge_image_list(picturelist);

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

            gifitemBean.setComments(commentsBean);
        }
        list.add(gifitemBean);

        DBHelper.insertIntoGifitemBean(gifitemBean);

    }

    @Override
    public void onStop() {
        super.onStop();
        saveId();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void saveId(){
        try {
            SharedPreferences.Editor editor = preferences.edit();
            //设置参数
            editor.putLong("item_id",  list.get(FIRST_ONE).getId());
            //提交
            editor.commit();
            LogUtils.e("保存",list.get(FIRST_ONE).getId()+"");
        }catch (Exception e){

        }
    }
    /**
     * 刷新页面
     */
    public void refreshPage(){
        listview.setSelection(0);
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }
}
