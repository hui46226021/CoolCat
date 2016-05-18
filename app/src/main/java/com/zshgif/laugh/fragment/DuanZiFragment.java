package com.zshgif.laugh.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.zshgif.laugh.model.CommentsBean;
import com.zshgif.laugh.model.DuanZiBean;
import com.zshgif.laugh.model.ReleaseUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.adapter.DuanZiAdapter;

import com.zshgif.laugh.listener.HttpCallbackListener;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.dao.db.DBHelper;
import com.zshgif.laugh.utils.HttpUtils;
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
public class DuanZiFragment extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.listview)
    public  ListView listview;
    @ViewInject(R.id.id_swiperefreshlayout)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static DuanZiFragment instance;

    /**
     * 图片对象 集合
     */
    private List<DuanZiBean> list = new ArrayList<DuanZiBean>();
    /**
     * 适配器
     */
    private DuanZiAdapter duanZiAdapter;
    public DuanZiFragment() {
    }


    public static DuanZiFragment newInstance() {
        instance = new DuanZiFragment();

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
        initData();
        settingView();
        return view;
    }

    /**
     * 初始化数据
     */
    void initData(){
        list=  DBHelper.loadAllDuanZiBean();
    }

    /**
     *  设置控件
     */
    @TargetApi(Build.VERSION_CODES.M)
    void settingView(){
        duanZiAdapter=   new DuanZiAdapter(getActivity(),R.layout.item_main,list,this);
        listview.setAdapter(duanZiAdapter);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FIRST_ONE =  listview.getFirstVisiblePosition();
                LAST_ONE = listview.getLastVisiblePosition();
                if (LAST_ONE == (list.size()-5)){
                    list.addAll(DBHelper.loadAllDuanZiBeanDaoPushTen(LAST_ONE)) ;
                    duanZiAdapter.notifyDataSetChanged();
                }
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

        HttpUtils.sendHttpRequestDoPost(Constant.GET_DUANZI_LIST_URL, param, new HttpCallbackListener() {
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
            if (type!=3){
                return;
            }
            singleDuanZi(group,jsonObjectItem.getJSONArray("comments"));



        }
        setToastMessage("更新了"+list.size()+"条段子└(^o^)┘");
        duanZiAdapter.notifyDataSetChanged();
    }

    /**
     * 一个段子type =1  type =3 GIF
     */
    private void singleDuanZi(JSONObject jsonObject,JSONArray comments){
        DuanZiBean duanZiBean = new DuanZiBean();
        try {


            duanZiBean.setNETid(jsonObject.getLong("id"));
            duanZiBean.setContent(jsonObject.getString("text"));//图片描述
            duanZiBean.setCategory_name(jsonObject.getString("category_name"));//分类
            duanZiBean.setDigg_count(jsonObject.getInt("digg_count"));
            duanZiBean.setBury_count(jsonObject.getInt("bury_count"));
            duanZiBean.setComments_count(jsonObject.getInt("comment_count"));
            duanZiBean.setShare_url(jsonObject.getString("share_url"));

            /**
             * 发布者和评论  最后将项目对象放到集合
             */
            releaseUserAndcomments(duanZiBean,jsonObject.getJSONObject("user"),comments);


        } catch (JSONException e) {
            LogUtils.e("错误的JSON",jsonObject.toString());
//            e.printStackTrace();
        }

    }


    /**
     * 发布者和评论者
     * @param duanZiBean
     * @param userObject
     * @param comments
     */
    private void releaseUserAndcomments(DuanZiBean duanZiBean,JSONObject userObject,JSONArray comments) throws JSONException {
        /**
         * 发布者
         */
        ReleaseUser releaseUser = new ReleaseUser();
        releaseUser.setUserProfile(userObject.getString("avatar_url"));
        releaseUser.setUsername(userObject.getString("name"));

        duanZiBean.setReleaseUser(releaseUser);

        /**
         * 评论这信息
         */
        if (comments.length()!=0){
            JSONObject commentsJson = comments.getJSONObject(0);
            CommentsBean commentsBean = new CommentsBean();
            commentsBean.setComment(commentsJson.getString("text"));
            commentsBean.setCommentUserName(commentsJson.getString("user_name"));
            commentsBean.setCommentUserProfile(commentsJson.getString("avatar_url"));

            duanZiBean.setComments(commentsBean);
        }
        list.add(duanZiBean);
        DBHelper.insertIntoDuanZiBean(duanZiBean);

    }
}
