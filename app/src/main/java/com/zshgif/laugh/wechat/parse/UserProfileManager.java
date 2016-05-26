package com.zshgif.laugh.wechat.parse;

import android.content.Context;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import com.hyphenate.easeui.domain.EaseUser;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.DemoHelper.DataSyncListener;
import com.zshgif.laugh.wechat.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class UserProfileManager {

	/**
	 * application context
	 */
	protected Context appContext = null;

	/**
	 * init flag: test if the sdk has been inited before, we don't need to init
	 * again
	 */
	private boolean sdkInited = false;

	/**
	 * HuanXin sync contact nick and avatar listener
	 */
	private List<DataSyncListener> syncContactInfosListeners;

	private boolean isSyncingContactInfosWithServer = false;

	private EaseUser currentUser;

	public UserProfileManager() {
	}

	public synchronized boolean init(Context context) {
		if (sdkInited) {
			return true;
		}
		ParseManager.getInstance().onInit(context);
		syncContactInfosListeners = new ArrayList<DataSyncListener>();
		sdkInited = true;
		return true;
	}

	public void addSyncContactInfoListener(DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (!syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.add(listener);
		}
	}

	public void removeSyncContactInfoListener(DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.remove(listener);
		}
	}

	public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
		if (isSyncingContactInfosWithServer) {
			return;
		}
		isSyncingContactInfosWithServer = true;
		ParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {

			@Override
			public void onSuccess(List<EaseUser> value) {
				isSyncingContactInfosWithServer = false;
				// in case that logout already before server returns,we should
				// return immediately
				if (!DemoHelper.getInstance().isLoggedIn()) {
					return;
				}
				if (callback != null) {
					callback.onSuccess(value);
				}
			}

			@Override
			public void onError(int error, String errorMsg) {
				isSyncingContactInfosWithServer = false;
				if (callback != null) {
					callback.onError(error, errorMsg);
				}
			}

		});

	}

	public void notifyContactInfosSyncListener(boolean success) {
		for (DataSyncListener listener : syncContactInfosListeners) {
			listener.onSyncComplete(success);
		}
	}

	public boolean isSyncingContactInfoWithServer() {
		return isSyncingContactInfosWithServer;
	}

	public synchronized void reset() {
		isSyncingContactInfosWithServer = false;
		currentUser = null;
		PreferenceManager.getInstance().removeCurrentUserInfo();
	}
	//获取最近联系人
	public synchronized EaseUser getCurrentUserInfo() {
		if (currentUser == null) {
			//获取当前用户
			String username = EMClient.getInstance().getCurrentUser();
			currentUser = new EaseUser(username);
			//获取当前昵称
			String nick = getCurrentUserNick();
			currentUser.setNick((nick != null) ? nick : username);
			//化身？
			currentUser.setAvatar(getCurrentUserAvatar());
		}
		return currentUser;
	}

	public boolean updateCurrentUserNickName(final String nickname) {
		boolean isSuccess = ParseManager.getInstance().updateParseNickName(nickname);
		if (isSuccess) {
			setCurrentUserNick(nickname);
		}
		return isSuccess;
	}

	public String uploadUserAvatar(byte[] data) {
		String avatarUrl = ParseManager.getInstance().uploadParseAvatar(data);
		if (avatarUrl != null) {
			setCurrentUserAvatar(avatarUrl);
		}
		return avatarUrl;
	}

	public void asyncGetCurrentUserInfo() {
		ParseManager.getInstance().asyncGetCurrentUserInfo(new EMValueCallBack<EaseUser>() {

			@Override
			public void onSuccess(EaseUser value) {
			    if(value != null){
    				setCurrentUserNick(value.getNick());
    				setCurrentUserAvatar(value.getAvatar());
			    }
			}

			@Override
			public void onError(int error, String errorMsg) {

			}
		});

	}
	public void asyncGetUserInfo(final String username, final EMValueCallBack<EaseUser> callback){
		ParseManager.getInstance().asyncGetUserInfo(username, callback);
	}
	private void setCurrentUserNick(String nickname) {
		getCurrentUserInfo().setNick(nickname);
		PreferenceManager.getInstance().setCurrentUserNick(nickname);
	}

	private void setCurrentUserAvatar(String avatar) {
		getCurrentUserInfo().setAvatar(avatar);
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
	}

	/**
	 * 获取当前昵称
	 * @return
     */
	private String getCurrentUserNick() {
		return PreferenceManager.getInstance().getCurrentUserNick();
	}

	/**
	 * 获取化身
	 * @return
     */
	private String getCurrentUserAvatar() {
		return PreferenceManager.getInstance().getCurrentUserAvatar();
	}

}
