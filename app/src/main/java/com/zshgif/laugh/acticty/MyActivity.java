
package com.zshgif.laugh.acticty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.adapter.DuanZiAdapter;
import com.zshgif.laugh.adapter.GifPaictureAdapter;
import com.zshgif.laugh.adapter.MyViewPagerAdapter;
import com.zshgif.laugh.fragment.DuanZiFragment;
import com.zshgif.laugh.fragment.GifPictureFragment;
import com.zshgif.laugh.fragment.MyFragment;
import com.zshgif.laugh.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_FIXED;
import static android.support.design.widget.TabLayout.OnClickListener;

public class MyActivity extends BaseActivity
    implements ViewPager.OnPageChangeListener, OnClickListener {

  //初始化各种控件，照着xml中的顺序写
  @ViewInject(R.id.id_coordinatorlayout)
  private CoordinatorLayout mCoordinatorLayout;
  @ViewInject(R.id.id_appbarlayout)
  private AppBarLayout mAppBarLayout;
  @ViewInject(R.id.id_toolbar)
  private Toolbar mToolbar;
  @ViewInject(R.id.id_tablayout)
  private TabLayout mTabLayout;
  @ViewInject(R.id.id_viewpager)
  private ViewPager mViewPager;
  @ViewInject(R.id.id_floatingactionbutton)
  private FloatingActionButton mFloatingActionButton;


  // TabLayout中的tab标题
  private String[] mTitles;
  // 填充到ViewPager中的Fragment
  private List<Fragment> mFragments;
  // ViewPager的数据适配器
  private MyViewPagerAdapter mViewPagerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    //初始化控件
    ViewUtils.inject(this);

    // 初始化mTitles、mFragments等ViewPager需要的数据
    //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
    initData();

    // 对各种控件进行设置、适配、填充数据
    configViews();
  }

  /**
   * 初始化数据
   */
  private void initData() {

    // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
    mTitles = getResources().getStringArray(R.array.tab_titles);

    //初始化填充到ViewPager中的Fragment集合
    mFragments = new ArrayList<>();
    mFragments.add(0, GifPictureFragment.newInstance());
    mFragments.add(1, DuanZiFragment.newInstance());
    for (int i = 2; i < mTitles.length; i++) {
      Bundle mBundle = new Bundle();
      mBundle.putInt("flag", i);
      MyFragment mFragment = new MyFragment();
      mFragment.setArguments(mBundle);
      mFragments.add(i, mFragment);
    }
  }

  /**
   * 设置各种控件
   */
  private void configViews() {

    // 设置显示Toolbar
    setSupportActionBar(mToolbar);

    // 初始化ViewPager的适配器，并设置给它
    mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
    mViewPager.setAdapter(mViewPagerAdapter);
    // 设置ViewPager最大缓存的页面个数
    mViewPager.setOffscreenPageLimit(3);
    // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
    mViewPager.addOnPageChangeListener(this);

    mTabLayout.setTabMode(MODE_FIXED);
    // 将TabLayout和ViewPager进行关联，让两者联动起来
    mTabLayout.setupWithViewPager(mViewPager);
    // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
    mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

    // 设置FloatingActionButton的点击事件
    mFloatingActionButton.setOnClickListener(this);
  }


  /**
   * 设置菜单
   * @param menu
   * @return
     */
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_my, menu);
    return true;
  }

  /**
   * toobar 菜单点击
   * @param item
   * @return
     */
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.sign_out) {
      onBackPressed();
    }

    if(id == R.id.set_theme){
      startActivity(new Intent(this,SetThemeActivty.class));
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onPageSelected(int position) {
    mToolbar.setTitle(mTitles[position]);
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override public void onPageScrollStateChanged(int state) {

  }

  /**
   * 点击事件
   * @param v
     */
  @Override public void onClick(View v) {
    switch (v.getId()) {
      // FloatingActionButton的点击事件
      case R.id.id_floatingactionbutton:
        SnackbarUtil.show(v, getString(R.string.plusone), 0);
        break;
    }

  }



  @Override
  public void onBackPressed() {
    super.onBackPressed();
    WelcomeActivity.instance.finish();
  }
}
