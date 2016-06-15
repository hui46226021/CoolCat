
package com.zshgif.laugh.acticty;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.code.microlog4android.config.PropertyConfigurator;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.adapter.MyViewPagerAdapter;
import com.zshgif.laugh.fragment.DuanZiFragment;
import com.zshgif.laugh.fragment.GifPictureFragment;

import com.zshgif.laugh.dao.db.DBHelper;
import com.zshgif.laugh.cache.DiskLruCacheUtil;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.wechat.ui.AddContactActivity;
import com.zshgif.laugh.wechat.ui.LoginFragment;
import com.zshgif.laugh.wechat.ui.MainFragment;
import com.zshgif.laugh.wechat.ui.SettingActivity;
import com.zshgif.laugh.wechat.ui.SettingsFragment;

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

  long maxMemory;//当前硬盘里的最大存储

  static MyActivity myActivity;
  /**
   * 图片页面
   */
  private GifPictureFragment gifPictureFragment;
  /**
   * 段子页面
   */
  private DuanZiFragment duanZiFragment;
  /**
   * 聊天页面
   */
  private MainFragment mainFragment;

  private int currentPage=0;//0 图片页面  1 段子页面

  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    myActivity = this;
    //初始化控件
    ViewUtils.inject(this);
    //启用日志
    PropertyConfigurator.getConfigurator(this).configure();
    DBHelper.getInstance(this);

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
    gifPictureFragment = GifPictureFragment.newInstance();
    mFragments.add(0, gifPictureFragment);
    duanZiFragment = DuanZiFragment.newInstance();
    mFragments.add(1,duanZiFragment);


    mainFragment = MainFragment.newInstance();
    mFragments.add(2,mainFragment);
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
    mFloatingActionButton.setRippleColor(setThemeColor1());
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


    if (id == R.id.add_Contact) {
      startActivity(new Intent(this, AddContactActivity.class));
    }
    if(id == R.id.setting){
      startActivity(new Intent(this,SettingActivity.class));
    }
    if(id == R.id.set_theme){
      startActivity(new Intent(this,SetThemeActivty.class));
    }
    if(id == R.id.clear){
      AlertDialog.Builder builder = builder = new AlertDialog.Builder(MyActivity.this);
      builder.setTitle("提示");
      maxMemory = DiskLruCacheUtil.size();

      builder.setMessage("清除图片缓存，将释放"+(maxMemory/1024/1024)+"M 空间");
//      builder.setCancelable(false);
      builder.setPositiveButton("清除", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//                                //跳转下载链接
          cleanMemoryProgress();
          DiskLruCacheUtil.delete(MyActivity.this);



        }
      });
      builder.setNegativeButton("保留", null);
      builder.create().show();
      builder.create().dismiss();

    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onPageSelected(int position) {
    mToolbar.setTitle(mTitles[position]);
    currentPage =position;
    if (position==0||position==1){
      mFloatingActionButton.setVisibility(View.VISIBLE);
    }else {
      mFloatingActionButton.setVisibility(View.GONE);
    }
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
//        SnackbarUtil.show(v, getString(R.string.plusone), 0);
        /**
         * 刷新
         */
        switch (currentPage){
          case 0:
            gifPictureFragment.refreshPage();
            break;
          case 1:
            duanZiFragment.refreshPage();
            break;
        }
        break;
    }

  }



  @Override
  public void onBackPressed() {
    super.onBackPressed();
    WelcomeActivity.instance.finish();
  }

  @Override
  protected void onResume() {
    super.onResume();
    DiskLruCacheUtil.open(this);
    maxMemory =DiskLruCacheUtil.size();
    if ((maxMemory/1024/1024)>=1000){
      AlertDialog.Builder builder = builder = new AlertDialog.Builder(MyActivity.this);
      builder.setTitle("提示");

      builder.setMessage("您当前的图片缓存已大于1GB，过多无效的图片缓存会浪费您设备的存储空间");
//      builder.setCancelable(false);
      builder.setPositiveButton("清除缓存", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//                                //跳转下载链接
          cleanMemoryProgress();
          DiskLruCacheUtil.delete(MyActivity.this);



        }
      });
      builder.setNegativeButton("我还要留着离线看", null);
      builder.create().show();
      builder.create().dismiss();
    }
  }

  /**
   *获取删除的文件大小
   */
  long cleanSize;

  public void setDelectFileLength(long fileLength){
    cleanSize +=fileLength;
    try {
      int progress = (int)((cleanSize*1000)/maxMemory);
      dialog.incrementProgressBy(progress);
      if (progress>=999){
        cleanMemoryProgressDismiss();
      }

    }catch (Exception e){}

  }
  ProgressDialog dialog =null;
  void cleanMemoryProgress(){
    if (dialog ==null){
      dialog = new ProgressDialog(this);
    }

    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
    dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
    dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//    dialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
    dialog.setTitle("提示");
    dialog.setMax(1000);
    dialog.show();

  }
  void cleanMemoryProgressDismiss(){

    dialog.dismiss();
  }

  /**
   * 当其他地方startActivty的时候调用
   * @param intent
     */
  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    mainFragment.onNewIntent(intent);

  }


}
