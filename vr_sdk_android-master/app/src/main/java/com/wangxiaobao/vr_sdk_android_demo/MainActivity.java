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
    int voice_appId = 1400376695;

    /**
     * VR 语音 appSecret
     * 如果填写不正确 可能导致不能进行语音通话
     * */
    String voice_secret = "35cd88805babafbbee7577f965441566a9b7346bb4ea5754f14d36322b755d4e";

    /**
     * VR token   通过对接接口获取
     * 如果不正确 可能导致不能加载VR界面
     * 通过文档中https://vrh5.source3g.com/user/auth/check 接口获取
     * */
    String vrToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTczMDg0ODksImlhdCI6MTU5NzMwNjY4OX0.z45hXretx3yExYg-j9eVvgXgsBVqdIqtpkwXu-UuwOU";


    /**
     * VR appId   通过对接接口获取
     * 如果不正确 可能导致不能加载VR界面
     * 通过文档中https://vrh5.source3g.com/user/auth/check 接口获取
     * */
    String vrAppId = "w81ABzcU";


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
    String clientName = "姜雪莲.123";

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
