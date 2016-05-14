/*
 *
 *  *
 *  *  *
 *  *  *  * ===================================
 *  *  *  * Copyright (c) 2016.
 *  *  *  * 作者：安卓猴
 *  *  *  * 微博：@安卓猴
 *  *  *  * 博客：http://sunjiajia.com
 *  *  *  * Github：https://github.com/opengit
 *  *  *  *
 *  *  *  * 注意**：如果您使用或者修改该代码，请务必保留此版权信息。
 *  *  *  * ===================================
 *  *  *
 *  *  *
 *  *
 *
 */

package com.zshgif.laugh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;


import com.zshgif.laugh.R;
import com.zshgif.laugh.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {


  private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
  public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
  }

  public OnItemClickListener mOnItemClickListener;


  private SimpleAdapter sim_adapter;

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  public Context mContext;
  public List<String> mDatas;
  public LayoutInflater mLayoutInflater;

  public MyRecyclerViewAdapter(Context mContext) {
    this.mContext = mContext;
    mLayoutInflater = LayoutInflater.from(mContext);
    mDatas = new ArrayList<>();
    for (int i = 0; i <= 20; i++) {
      int x=(int)(Math.random()*30);
      StringBuilder  sb = new StringBuilder();
      for(int y = 0; y <= x; y++){
        sb.append("张月鹿  ");
      }
      mDatas.add(sb.toString());
    }
  }

  /**
   * 创建ViewHolder
   */
  @Override public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
   final MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
    String [] from ={"image"};
    int [] to = {R.id.image};
    sim_adapter = new SimpleAdapter(mContext, data_list, R.layout.gridview, from, to);
    mViewHolder.gridView.setAdapter(sim_adapter);
    mViewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SnackbarUtil.show( mViewHolder.gridView, "点击了图片", 0);
      }
    });
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
    if (mOnItemClickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mOnItemClickListener.onItemClick(holder.itemView, position);
        }
      });

//      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//        @Override public boolean onLongClick(View v) {
//          mOnItemClickListener.onItemLongClick(holder.itemView, position);
//          return true;
//        }
//      });

    }
    data_list.clear();
    getData();
    sim_adapter.notifyDataSetChanged();

  }

  @Override public int getItemCount() {
    return mDatas.size();
  }

  public List<Map<String, Object>> getData(){
    //cion和iconName的长度是相同的，这里任选其一都可以
    for(int i=0;i<9;i++){
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("image", R.drawable.loadimage);
      data_list.add(map);
    }

    return data_list;
  }
}
