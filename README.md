# android 对接VR文档

Android sdk对接文档持续更新，最新文档请查看 http://gitlab.source3g.com:10080/docs/android-vr-doc.git

[demo下载地址：https://github.com/2628748861/vr_sdk_android.git](https://github.com/2628748861/vr_sdk_android.git)


**一.添加依赖：**
1.在project的build.gradle中添加jitpack仓库支持
```java
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }

```
2.在module的build.gradle中添加依赖的引用
```java
dependencies {
    implementation 'com.github.2628748861:vr_sdk_android:1.0.1'
  }
```

**二.集成步骤：**

 1.添加权限
 ```java
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />

 ```
 
 2.在defaultConfig中,指定App使用的CPU架构.
  ```java
  defaultConfig {
        ndk {
        abiFilters "armeabi", "armeabi-v7a", "arm64-v8a"
      }
    }
  ```
  
3.新建Activity 继承 VrActivity
```java
public class MyVrActivity extends WxbVrActivity {
        /**
        * 微信邀请好友回调
        * */
        @Override
        public void onVrInviteShareToWeChat() {

            }
        /**
        * VR语音房加入成功 在vr带看中能和用户正常聊天带看
        * */
        @Override
        public void onEnterVoiceRoomSuccess() {

            }
        /**
        * VR语音房加入失败 不能在vr带看中能和用户聊天带看
        * */
        @Override
        public void onEnterVoiceRoomFail() {

            }
        /**
         * 进入房间进入成功  加载成功进入vr界面能进行同屏带看
        * */
        @Override
        public void onEnterRoomSuccess() {

            }
        /**
        * 进入房间进入失败  不能进行同屏带看
        * */
        @Override
        public void onEnterRoomFail() {

            }

        @Override
        protected void resetNav(RelativeLayout nav_layout, ImageButton backBtn, TextView vr_title) {
        super.resetNav(nav_layout, backBtn, vr_title);
        }

        public static void go(Context mContext, Class<?> clasz, WxbVrParams vrParams) {
            if (vrParams == null) {
                Toast.makeText(mContext, "跳转失败,缺少必要参数", Toast.LENGTH_SHORT).show();
                return;
            }
            //跳转界面前需要动态申请录音权限
            Intent intent = new Intent(mContext, clasz);
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", vrParams);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

```



```java

package com.wangxiaobao.vr_sdk_android_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.wangxiaobao.vr_sdk_android.WxbVrParams;

public class MainActivity extends Activity {
    /**
     * VR 界面标题
     * */
    String title = "vrDemo";

    /**
     * VR 加载链接
     *
     * */
    String url = "https://vrh5-test-e.source3g.com/#/";

    /**
     * VR 语音 appId
     * 如果填写不正确 可能导致不能进行语音通话
     * */
    int voice_appId = 0; //你的腾讯实时音视频的appId

    /**
     * VR 语音 appSecret
     * 如果填写不正确 可能导致不能进行语音通话
     * */
    String voice_secret = "你的腾讯实时音视频的appSecret";

    /**
     * VR token   通过对接接口获取
     * 如果不正确 可能导致不能加载VR界面
     * 通过文档中https://vrh5.source3g.com/user/auth/check 接口获取
     * */
    String vrToken = "你的vrToken";


    /**
     * VR appId   通过对接接口获取
     * 如果不正确 可能导致不能加载VR界面
     * 通过文档中https://vrh5.source3g.com/user/auth/check 接口获取
     * */
    String vrAppId = "你的appId";


    /**
     * VR 房间号
     * 保持平台唯一性，避免多人进入同一房间
     * */
    int roomId = (int) (System.currentTimeMillis() / 1000);


    /**
     * 用户id
     * 保持平台唯一性
     * */
    String clientId = "162";

    /**
     * 用户昵称
     * 在VR房间显示，区分用户
     * */
    String clientName = "xxx";

    /**
     * 用户头像
     * 在VR房间显示，区分用户
     * */
    String clientHeadImg = "https://upload.jianshu.io/users/upload_avatars/16806639/b8df57a8-6ef4-42e3-9097-2ac8a123daef?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96/format/webp";


    /**
     * 应用平台
     * 默认Android 原生应用填写 android
     * */
    String platform = "android";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roomId = 23232323;
        findViewById(R.id.govrBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WxbVrParams vrParams = new WxbVrParams(url, title, voice_appId, voice_secret,
                        vrToken, vrAppId, roomId, clientId, clientName, clientHeadImg, platform);
                MyVrActivity.go(MainActivity.this, MyVrActivity.class, vrParams);
            }
        });
    }
}

```


4.注册AndroidManifest.xml
```java
<application>
        <activity android:name=".MyVrActivity">

        </activity>
        <activity android:name=".MainActivity">

        </activity>
      </aplication>
```


 5.代码混淆
 ```java
 #vr配置
    -keep class com.just.agentweb.** {
    *;
    }
    -dontwarn com.just.agentweb.**
    -keepclassmembers class com.wangxiaobao.vr_sdk_android.WxbVrActivity.AndroidInterface{ *; }
    #语音配置
    -keep class com.tencent.** { *; }
    # 保留Parcelable序列化类不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }
 ```
 
 6.sdk参数说明
 
 voice_appId和voice_secret说明：
 
 获取需要在腾讯云注册账号 选择[实时音视频服务：https://cloud.tencent.com/product/trtc](https://cloud.tencent.com/product/trtc)，然后在控制台创建应用，在应用信息中获取SDKAppID(项目的voice_appId参数)和密匙Key(项目的voice_secret参数)
 
 
 vrAppId和vrToken说明：
 
 appId即是vrAppId
 
 vrToken需要通过appId和secret获取，如下

获取VR全局唯一后台接口调用凭据（token）。调用绝大多数后台接口时都需使用 token，开发者需要进行妥善保存。

请求地址

```
post https://vrh5.source3g.com/user/auth/check
```
method:application/x-www-form-urlencoded

参数说明

参数|是否必填|说明
---|---|---
appId|是|项目appId
appSecret|是|项目appSecret

[如何获取appId与appSecret](../获取appId与appSecret.md)

正确返回数据如下:

```
{
    "code": "0",
    "action": "/auth/check",
    "msg": "succeeded",
    "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTU5NDU3NjUsImlhdCI6MTU5NTk0Mzk2NX0.8F_a5ckcV5f8cJxDJnpBg70yaxQ6T0K_lLhyScVnybA",
    "timestamp": "2020-07-28 21:46:05",
    "ok": true
}
```

参数|描述
---|---
code| 0是正常
data| token内容

错误时返回数据

```
{
    "code": "-1",
    "action": "/auth/check",
    "msg": "appId错误",
    "timestamp": "2020-07-28 21:47:44",
    "ok": false
}
```


 
 