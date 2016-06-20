package com.zshgif.laugh.wechat.parse;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.cache.MapCache;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.LogUtils;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.bean.hxuser;
import com.zshgif.laugh.wechat.bean.state;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 分析
 */
public class ParseManager {


    private static final String CONFIG_USERNAME = "username";


    private Context appContext;
    private static ParseManager instance = new ParseManager();


    private ParseManager() {

    }

    public static ParseManager getInstance() {
        return instance;
    }

    public void onInit(Context context) {
        this.appContext = context.getApplicationContext();
        appContext =  context;

        /**
         * 初始化
         */
        Bmob.initialize(ContextUtil.getInstance(), "7c29f2e1a4468734731d8e7ec0de2335");
    }

    static boolean isUserRegister;

    /**
     * 查询是否注册过
     * @param username
     * @return
     */
    public boolean isUserRegister(final String username) {


        BmobQuery<hxuser> bmobQuery = new BmobQuery<hxuser>();
        bmobQuery.addWhereEqualTo(CONFIG_USERNAME, username);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<hxuser>() {
            @Override
            public void onSuccess(List<hxuser> object) {


                if (object == null || object.size() == 0) {
                    isUserRegister = false;
                }else {
                    isUserRegister = true;
                }
            }

            @Override
            public void onError(int code, String msg) {
                isUserRegister = false;

            }
        });
        return isUserRegister;

    }

    boolean updateParseNickName;

    /**
     * 更新昵称
     * @param nickname
     * @return
     */
    public boolean updateParseNickName(final String nickname) {
        String username = EMClient.getInstance().getCurrentUser();
        BmobQuery<hxuser> bmobQuery = new BmobQuery<hxuser>();
        bmobQuery.addWhereEqualTo(CONFIG_USERNAME, username);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<hxuser>() {
            @Override
            public void onSuccess(List<hxuser> object) {

                for (hxuser user : object) {

                    user.setNickname(nickname);
                    user.update(ContextUtil.getInstance(), user.getObjectId(), new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "更新成功：");
                            updateParseNickName = true;
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "更新失败：" + msg);
                            updateParseNickName = false;
                        }
                    });
                }
                if (object == null || object.size() == 0) {
                    updateParseNickName = false;
                }
            }

            @Override
            public void onError(int code, String msg) {
                updateParseNickName = false;

            }
        });
        return updateParseNickName;


    }

    /**
     * 注册的时候设置昵称
     *
     * @param nickname
     * @return
     */
    public void updateRegisterParseNickName(final String username, final String nickname) {


        hxuser user = new hxuser();
        user.setNickname(nickname);
        user.setUsername(username);
        user.save(ContextUtil.getInstance(), new SaveListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int code, String arg0) {
                // 添加失败
            }
        });

    }

    /**
     * 获取好友列表信息
     * @param usernames
     * @param callback
     */
    public void getContactInfos(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {

        BmobQuery<hxuser> bmobQuery = new BmobQuery<hxuser>();
        bmobQuery.addWhereContainedIn(CONFIG_USERNAME, usernames);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<hxuser>() {
            @Override
            public void onSuccess(List<hxuser> object) {
                List<EaseUser> mList = new ArrayList<EaseUser>();
                for (hxuser hxuser_user : object) {
                    EaseUser user = new EaseUser(hxuser_user.getUsername());
                    if (hxuser_user.getAvatar() != null) {
                        user.setAvatar(hxuser_user.getAvatar());
                    }
                    user.setNick(hxuser_user.getNickname());
                    mList.add(user);
                }
                callback.onSuccess(mList);

            }

            @Override
            public void onError(int code, String msg) {
                callback.onError(0, "");

            }
        });


    }

    /**
     * 同步好友数据
     * @param callback
     */
    public void asyncGetCurrentUserInfo(final EMValueCallBack<EaseUser> callback) {
        final String username = EMClient.getInstance().getCurrentUser();
        asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser value) {
                callback.onSuccess(value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                /**
                 * 没有查到数据
                 */
				if (error == 0) {

                    hxuser user = new hxuser();

                    user.setUsername(username);
                    user.save(ContextUtil.getInstance(), new SaveListener() {

                        @Override
                        public void onSuccess() {
                            callback.onSuccess(new EaseUser(username));
                        }

                        @Override
                        public void onFailure(int code, String arg0) {
                            // 添加失败
                            callback.onError(0, "");
                        }
                    });


				}else{
					callback.onError(error, errorMsg);
				}
            }
        });
    }

    public void asyncGetUserInfo(final String username, final EMValueCallBack<EaseUser> callback) {

        BmobQuery<hxuser> bmobQuery = new BmobQuery<hxuser>();
        bmobQuery.addWhereEqualTo(CONFIG_USERNAME, username);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<hxuser>() {
            @Override
            public void onSuccess(List<hxuser> object) {

                for (hxuser hxuser_user : object) {
//                    EaseUser user = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo();
                    String nick = hxuser_user.getNickname();
                    String avatar = hxuser_user.getAvatar();
                    EaseUser user = DemoHelper.getInstance().getContactList().get(username);

                    if (user != null) {
                        user.setNick(hxuser_user.getNickname());
                        if (hxuser_user.getAvatar() != null) {
                            user.setAvatar(hxuser_user.getAvatar());
                        }

                    }else {

                        user = new EaseUser(username);
                        user.setNick(nick);
                        if (hxuser_user.getAvatar() != null) {
                            user.setAvatar(hxuser_user.getAvatar());
                        }
                    }

                    callback.onSuccess(user);
                }

                if(object==null||object.size()==0){
                    callback.onError(0, "");
                }

            }

            @Override
            public void onError(int code, String msg) {
                callback.onError(0, "");

            }
        });
    }

   static BmobFile bmobFile;

    public String uploadParseAvatar(byte[] data) {


        bytToFile(data);
        File fileFolder = new File(filePath);
        bmobFile = new BmobFile(new File(fileFolder, filename));
        bmobFile.uploadblock(ContextUtil.getInstance(), new UploadFileListener() {

            @Override
            public void onSuccess() {

                saveAvatar(bmobFile.getFileUrl(ContextUtil.getInstance()));
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e("bmob","上传失败："+msg+"错误吗"+code);
            }
        });


        return "";
    }

    private void saveAvatar(final String  url){
        String username = EMClient.getInstance().getCurrentUser();
        BmobQuery<hxuser> bmobQuery = new BmobQuery<hxuser>();
        bmobQuery.addWhereEqualTo(CONFIG_USERNAME, username);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<hxuser>() {
            @Override
            public void onSuccess(List<hxuser> object) {

                for (hxuser hxuser_user : object) {
                    if (!TextUtils.isEmpty(hxuser_user.getAvatar())){
                        //如果之前有头像 删除之前的头像
                        deleteeAvatar(hxuser_user.getAvatar());
                    }
                    hxuser_user.setAvatar(url);
                    hxuser_user.update(ContextUtil.getInstance(), hxuser_user.getObjectId(), new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "更新成功：");
                            DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(url);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "更新失败：" + msg);

                        }
                    });

                }

            }

            @Override
            public void onError(int code, String msg) {


            }
        });

    }

    String filePath = Environment.getExternalStorageDirectory()
            + "/finger/";
    String filename =null;
    private void bytToFile(byte[] bytes){

        filename = System.currentTimeMillis()+"hehe.jpg";
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = null; // 文件输出流
        try {
            outputStream = new FileOutputStream(jpgFile);
            outputStream.write(bytes); // 写入sd卡中
            outputStream.close(); // 关闭输出流
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void deleteeAvatar(String url){
        /**
         * 删除原来的图片
         */
        BmobFile file = new BmobFile();
        file.setUrl(url);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(ContextUtil.getInstance(), new DeleteListener() {

            @Override
            public void onSuccess() {
                LogUtils.e("删除头像","成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                LogUtils.e("删除头像","失败");
            }
        });
    }

    /**
     * 获取审核状态
     */
    public void getExamineState(){
        BmobQuery<state> bmobQuery = new BmobQuery<state>();
        bmobQuery.addWhereEqualTo("name", Constant.EXAMINENAME);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<state>() {
            @Override
            public void onSuccess(List<state> object) {


                if (object == null || object.size() == 0) {
                    MapCache.putObject( Constant.EXAMINENAME,false);
                }else {
                    //状态对象
                    state  stateObject= object.get(0);
                    MapCache.putObject( Constant.EXAMINENAME,stateObject.isState());
                }
            }

            @Override
            public void onError(int code, String msg) {


            }
        });

    }
}



