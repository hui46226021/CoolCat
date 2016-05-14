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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.adapter.GifPaictureAdapter;
import com.zshgif.laugh.bean.GifitemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhush on 2016/5/15
 * gif图片页面.
 */
public class GifPictureFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
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
        for(int i = 0;i<30;i++){
            list.add(new GifitemBean());
        }
    }

    /**
     *  设置控件
     */
    void settingView(){
        listview.setAdapter(new GifPaictureAdapter(getActivity(),R.layout.item_main,list));
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
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                int temp = (int) (Math.random() * 10);

            }
        }, 1000);
    }
}
