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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.zshgif.laugh.R;


/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

  public GridView gridView;

  public MyRecyclerViewHolder(View itemView) {
    super(itemView);
    gridView = (GridView) itemView.findViewById(R.id.gridView);
  }
}
