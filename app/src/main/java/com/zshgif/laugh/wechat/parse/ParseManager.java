package com.zshgif.laugh.wechat.parse;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import com.zshgif.laugh.acticty.ContextUtil;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.bean.hxuser;

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
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 分析
 */
public class ParseManager {

//    private static final String TAG = ParseManager.class.getSimpleName();
//    private static final String ParseAppID = "UUL8TxlHwKj7ZXEUr2brF3ydOxirCXdIj9LscvJs";
//    private static final String ParseClientKey = "B1jH9bmxuYyTcpoFfpeVslhmLYsytWTxqYqKQhBJ";

    private static final String CONFIG_TABLE_NAME = "hxuser";
    private static final String CONFIG_USERNAME = "username";
    private static final String CONFIG_NICK = "nickname";
    private static final String CONFIG_AVATAR = "avatar";

    private Context appContext;
    private static ParseManager instance = new ParseManager();


    private ParseManager() {

    }

    public static ParseManager getInstance() {
        return instance;
    }

    public void onInit(Context context) {
        this.appContext = context.getApplicationContext();
//		Parse.enableLocalDatastore(appContext);
//		Parse.initialize(context, ParseAppID, ParseClientKey);

        Bmob.initialize(ContextUtil.getInstance(), "7c29f2e1a4468734731d8e7ec0de2335");
    }

    static boolean isUserRegister;

    public boolean isUserRegister(final String username) {

//		ParseQuery<ParseObject> pQuery = ParseQuery.getQuery(CONFIG_TABLE_NAME);
//		pQuery.whereEqualTo(CONFIG_USERNAME, username);
//		ParseObject pUser = null;
//		try {
//			 pUser = pQuery.getFirst();
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return false;
//		}
//		if (pUser==null){
//			return false;
//		}else {
//			return true;
//		}


        BmobQuery<hxuser> bmobQuery = new BmobQuery<hxuser>();
        bmobQuery.addWhereEqualTo(CONFIG_USERNAME, username);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(ContextUtil.getInstance(), new FindListener<hxuser>() {
            @Override
            public void onSuccess(List<hxuser> object) {

                for (hxuser gameScore : object) {
                    isUserRegister = true;
                }
                if (object == null || object.size() == 0) {
                    isUserRegister = false;
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

//		pUser.put(CONFIG_NICK, nickname);
//			pUser.save();
//			return true;
//		} catch (ParseException e) {
//			if(e.getCode()==ParseException.OBJECT_NOT_FOUND){
//				pUser = new ParseObject(CONFIG_TABLE_NAME);
//				pUser.put(CONFIG_USERNAME, username);
//				pUser.put(CONFIG_NICK, nickname);
//				try {
//					pUser.save();
//					return true;
//				} catch (ParseException e1) {
//					e1.printStackTrace();
//					EMLog.e(TAG, "parse error " + e1.getMessage());
//				}
//
//			}
//			e.printStackTrace();
//			EMLog.e(TAG, "parse error " + e.getMessage());
//		}
//		return false;
    }

    /**
     * 注册的时候设置昵称
     *
     * @param nickname
     * @return
     */
    public void updateRegisterParseNickName(final String username, final String nickname) {

//		ParseObject	pUser = new ParseObject(CONFIG_TABLE_NAME);
//				pUser.put(CONFIG_USERNAME, username);
//				pUser.put(CONFIG_NICK, nickname);
//				try {
//					pUser.save();
//				} catch (ParseException e1) {
//					e1.printStackTrace();
//					EMLog.e(TAG, "parse error " + e1.getMessage());
//				}


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

    public void getContactInfos(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
//		ParseQuery<ParseObject> pQuery = ParseQuery.getQuery(CONFIG_TABLE_NAME);
//		pQuery.whereContainedIn(CONFIG_USERNAME, usernames);
//		pQuery.findInBackground(new FindCallback<ParseObject>() {
//
//			@Override
//			public void done(List<ParseObject> arg0, ParseException arg1) {
//				if (arg0 != null) {
//					List<EaseUser> mList = new ArrayList<EaseUser>();
//					for (ParseObject pObject : arg0) {
//					    EaseUser user = new EaseUser(pObject.getString(CONFIG_USERNAME));
//						ParseFile parseFile = pObject.getParseFile(CONFIG_AVATAR);
//						if (parseFile != null) {
//							user.setAvatar(parseFile.getUrl());
//						}
//						user.setNick(pObject.getString(CONFIG_NICK));
//						EaseCommonUtils.setUserInitialLetter(user);
//						mList.add(user);
//					}
//					callback.onSuccess(mList);
//				} else {
//
//				}
//			}
//		});


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
                    EaseUser user = DemoHelper.getInstance().getContactList().get(username);

					if (hxuser_user.getAvatar() != null) {
					   user.setAvatar(hxuser_user.getAvatar());
					}

                    if (user != null) {
                        user.setNick(hxuser_user.getNickname());
                        //							if (pFile != null && pFile.getUrl() != null) {
//								user.setAvatar(pFile.getUrl());
//							}
                        callback.onSuccess(user);
                    }else {
                        callback.onError(0, "");
                    }


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
        String filename = "hehe2.jpg";
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
                    hxuser_user.setAvatar(url);
                    hxuser_user.update(ContextUtil.getInstance(), hxuser_user.getObjectId(), new UpdateListener() {

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

            }

            @Override
            public void onError(int code, String msg) {


            }
        });

    }

    String filePath = Environment.getExternalStorageDirectory()
            + "/finger/";
    private void bytToFile(byte[] bytes){


        String filename = "hehe2.jpg";
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
}



