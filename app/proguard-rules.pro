# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Java\android_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# 指定代码的压缩级别
-optimizationpasses 5
# 包明不混合大小写
-dontusemixedcaseclassnames
# 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
# 优化 不优化输入的类文件
-dontoptimize
# 预校验
-dontpreverify
# 混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/,!class/merging/
# 保护注解
-keepattributes Annotation

-dontoptimize
-dontpreverify

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
# 忽略警告
-ignorewarning
# 记录生成的日志数据,gradle build时在本项目根目录输出
# apk 包内所有 class 的内部结构
-dump class_files.txt
# 未混淆的类和成员
-printseeds seeds.txt
# 列出从 apk 中删除的代码
-printusage unused.txt




#第三方开始

#环信
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**
#环信完
#有米广告
-dontwarn net.youmi.android.**
-keep class net.youmi.android.** {
    *;
}
#有米广告完

#bmob云后端
# 不混淆BmobSDK
-keep class cn.bmob.v3.** {*;}

# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
-keep class * extends cn.bmob.v3.BmobObject {
    *;
}

#bmob云后端完

 # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
#-libraryjars libs/greendao-1.3.7.jar
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
#用来保持生成的表名不被混淆
public static java.lang.String TABLENAME; }
-keep class **$Properties
# # -------------------------------------------
# #  ######## greenDao混淆完  ##########
# # -
-keep class com.zshgif.laugh.model.**{*;}