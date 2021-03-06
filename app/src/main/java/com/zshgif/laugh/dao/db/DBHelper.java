package com.zshgif.laugh.dao.db;

import android.content.Context;

import com.zshgif.laugh.dao.VideoBeanDao;
import com.zshgif.laugh.fragment.BaseFragment;
import com.zshgif.laugh.model.CommentsBean;
import com.zshgif.laugh.dao.CommentsBeanDao;
import com.zshgif.laugh.model.DuanZiBean;
import com.zshgif.laugh.dao.DuanZiBeanDao;
import com.zshgif.laugh.model.GifitemBean;
import com.zshgif.laugh.dao.GifitemBeanDao;
import com.zshgif.laugh.dao.PictureBeanDao;
import com.zshgif.laugh.model.ReleaseUser;
import com.zshgif.laugh.dao.ReleaseUserDao;
import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.model.VideoBean;
import com.zshgif.laugh.utils.LogUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zhush on 2016/5/18.
 */
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper instance;
    private static Context appContext;
    private static CommentsBeanDao commentsBeanDao;
    private static DuanZiBeanDao duanZiBeanDao;
    private static GifitemBeanDao gifitemBeanDao;
    private static PictureBeanDao pictureBeanDao;
    private static ReleaseUserDao releaseUserDao;
    private static VideoBeanDao videoBeanDao;
    private DaoSession mDaoSession;
    private DBHelper() {
    }
    //单例模式，DBHelper只初始化一次
    public static  DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = ContextUtil.getDaoSession(context);
            commentsBeanDao = instance.mDaoSession.getCommentsBeanDao();
            duanZiBeanDao = instance.mDaoSession.getDuanZiBeanDao();
            gifitemBeanDao =instance.mDaoSession.getGifitemBeanDao();
            pictureBeanDao = instance.mDaoSession.getPictureBeanDao();
            releaseUserDao = instance.mDaoSession.getReleaseUserDao();
            videoBeanDao = instance.mDaoSession.getVideoBeanDao();
        }
        return instance;
    }

    //创建所有表
    public void createAllTable()
    {
        commentsBeanDao.createTable(mDaoSession.getDatabase(), true);
        duanZiBeanDao.createTable(mDaoSession.getDatabase(), true);
        gifitemBeanDao.createTable(mDaoSession.getDatabase(), true);
        pictureBeanDao.createTable(mDaoSession.getDatabase(), true);
        releaseUserDao.createTable(mDaoSession.getDatabase(), true);
        videoBeanDao.createTable(mDaoSession.getDatabase(), true);
    }

    public static void insertIntoGifitemBean(GifitemBean gifitemBean){
      LogUtils.e("GifitemBean插入数据库",instance.gifitemBeanDao.insert(gifitemBean)+"");
    }
    public static void insertIntoDuanZiBean(DuanZiBean duanZiBean){
        LogUtils.e("DuanZiBean插入数据库",instance.duanZiBeanDao.insert(duanZiBean)+"");
    }
    public static void insertIntoCommentsBean(CommentsBean commentsBean){
        LogUtils.e("CommentsBean插入数据库",instance.commentsBeanDao.insertOrReplace(commentsBean)+"");
    }
    public static void insertIntoReleaseUser(ReleaseUser releaseUse){
        LogUtils.e("ReleaseUser插入数据库",instance.releaseUserDao.insertOrReplace(releaseUse)+"");
    }
    public static void insertIntoVideoBean(VideoBean videoBean){
        LogUtils.e("videoBean插入数据库",instance.videoBeanDao.insertOrReplace(videoBean)+"");
    }

    /** 初始化GifitemBean */
    /**
     *
     * @param id
     * @return根据ID 查询 id前7个到最后一个倒叙
     *
     */
    public static List<GifitemBean> loadAllGifitemBean(int id) {
        QueryBuilder<GifitemBean> query = instance.gifitemBeanDao.queryBuilder()
                .orderDesc(GifitemBeanDao.Properties.Id)
                .where(GifitemBeanDao.Properties.Id.gt(id-BaseFragment.ALLOWANCE+""));

        List<GifitemBean> all = query.list();

        return all;
    }
    /** 查询所有的10条GifitemBean */
    /**
     *
     * @param id
     * @return
     */
    public static List<GifitemBean> loadAllGifitemBeanPushTen(int id) {
        QueryBuilder<GifitemBean> query = instance.gifitemBeanDao.queryBuilder()
                .orderDesc(GifitemBeanDao.Properties.Id)
                .where(GifitemBeanDao.Properties.Id.gt(id-25+""),GifitemBeanDao.Properties.Id.lt(id-5+""));

        List<GifitemBean> all = query.list();

        return all;
    }
    /** 查询所有的10条GifitemBean */
    public static List<DuanZiBean> loadAllDuanZiBeanDaoPushTen(int id) {

        QueryBuilder<DuanZiBean> query = instance.duanZiBeanDao.queryBuilder()
                .orderDesc(DuanZiBeanDao.Properties.Id)
                .where(DuanZiBeanDao.Properties.Id.gt(id-25+""),DuanZiBeanDao.Properties.Id.lt(id-5+""));

        List<DuanZiBean> all = query.list();
        return all;
    }

    /** 查询所有的DuanZiBean */
    public static List<DuanZiBean> loadAllDuanZiBean(int id) {
        QueryBuilder<DuanZiBean> query = instance.duanZiBeanDao.queryBuilder()
                .orderDesc(DuanZiBeanDao.Properties.Id)
                .where(DuanZiBeanDao.Properties.Id.gt(id- BaseFragment.ALLOWANCE+""));

        List<DuanZiBean> all = query.list();

        return all;
    }


    /** 查询所有的10条GifitemBean */
    public static List<VideoBean> loadAllVideoBeanPushTen(int id) {

        QueryBuilder<VideoBean> query = instance.videoBeanDao.queryBuilder()
                .orderDesc(VideoBeanDao.Properties.Id)
                .where(VideoBeanDao.Properties.Id.gt(id-25+""),VideoBeanDao.Properties.Id.lt(id-5+""));

        List<VideoBean> all = query.list();
        return all;
    }

    /** 查询所有的DuanZiBean */
    public static List<VideoBean> loadAllVideoBean(int id) {
        QueryBuilder<VideoBean> query = instance.videoBeanDao.queryBuilder()
                .orderDesc(VideoBeanDao.Properties.Id)
                .where(VideoBeanDao.Properties.Id.gt(id- BaseFragment.ALLOWANCE+""));

        List<VideoBean> all = query.list();

        return all;
    }

    /** 查询所有的GifitemBean */
    public static CommentsBean loadCommentsBean(Long id) {


        QueryBuilder qb = instance.commentsBeanDao.queryBuilder();
        qb.where(CommentsBeanDao.Properties.NETid.eq(id));
        List<CommentsBean> all = qb.list();
        if (all!=null&&all.size()>0){
            return (CommentsBean)all.get(0);
        }else {
            return null;
        }

    }
    /** 查询所有的GifitemBean */
    public static ReleaseUser loadReleaseUser(Long id) {


        QueryBuilder qb = instance.releaseUserDao.queryBuilder();
        qb.where(ReleaseUserDao.Properties.NETid.eq(id));
        List<ReleaseUser> all = qb.list();
        if (all!=null&&all.size()>0){
            return (ReleaseUser)all.get(0);
        }else {
            return null;
        }

    }



}
