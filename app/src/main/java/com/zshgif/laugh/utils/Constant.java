package com.zshgif.laugh.utils;

/**
 * Created by Administrator on 2016/5/15.
 */
public class Constant {
    /**
     * 获取网络图片列表
     */
    public final static String GET_PICTriURE_LIST_URL ="http://ic.snssdk.com/neihan/stream/mix/v1/";
    /**
     * 获取段子
     */
    public final static String GET_DUANZI_LIST_URL ="http://ic.snssdk.com/neihan/stream/mix/v1/?mpic=1&webp=1&essence=1&content_type=-102&message_cursor=-1&bd_longitude=121.595604&bd_latitude=37.381848&bd_city=%E7%83%9F%E5%8F%B0%E5%B8%82&am_longitude=121.595399&am_latitude=37.381672&am_city=%E7%83%9F%E5%8F%B0%E5%B8%82&am_loc_time=1463496953448&count=30&min_time=1463466772&screen_width=1080&iid=4079531978&device_id=5807013269&ac=wifi&channel=smartisan&aid=7&app_name=joke_essay&version_code=500&version_name=5.0.0&device_platform=android&ssmix=a&device_type=YQ601&os_api=22&os_version=5.1.1&uuid=865790020929428&openudid=ea45d1407bdfba67&manifest_version_code=500";

    /**
     * 环信
     */
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";


    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";

    public static final String MESSAGE_ATTR_AT_MSG = "em_at_message";




    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";

    /**
     * 登录状态
     */
    public static final String IS_LOGIN_KEY = "isLoginkey";

    public static final String PHONE_CONTACTS_LIST = "phone_contacts_list";
    /**
     * 聊天页面的位置
     */
    public static final int CALL__PAGE_LOAC = 2;
    /**
     * 有米广告
     */
    public static final boolean YM_STATE = false;//发布状态
    public static final String YM_APP_ID = "83694273f3f3d3f0";//appid
    public static final String YM_APP_SIGN = "b7d0da8fea76b6dc";//签名

}
