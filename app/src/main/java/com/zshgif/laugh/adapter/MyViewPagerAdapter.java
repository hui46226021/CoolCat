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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zshgif.laugh.R;
import com.zshgif.laugh.utils.Constant;

import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
  private Context context;
  private String[] mTitles;
  private List<Fragment> mFragments;


  public MyViewPagerAdapter(Context context,FragmentManager fm, String[] mTitles, List<Fragment> mFragments) {
    super(fm);
    this.mTitles = mTitles;
    this.mFragments = mFragments;
    this.context =context;
  }

  @Override public CharSequence getPageTitle(int position) {

    return null;

  }
  @Override public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override public int getCount() {
    return mFragments.size();
  }

  public View getView(int position){

    View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);

    TextView tv= (TextView) view.findViewById(R.id.btn_conversation);
    tv.setText(mTitles[position]);
    TextView img = (TextView) view.findViewById(R.id.unread_msg_number);
      img.setVisibility(View.INVISIBLE);
    return view;
  }


}
