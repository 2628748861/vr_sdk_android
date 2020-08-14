package com.wangxiaobao.vr_sdk_android;


public interface IWxbVrControll {

    /**
     * 当VR语音房间进入成功
     */
     void onEnterVoiceRoomSuccess();

    /**
     * 当VR语音房间进入失败
     */
    void onEnterVoiceRoomFail();


    /**
     * 当VR房间进入成功
     */
    void onEnterRoomSuccess();

    /**
     * 当VR房间进入失败
     */
    void onEnterRoomFail();

    /**
     * 当网页触发'邀请微信好友‘动作时
     */
     void onVrInviteShareToWeChat();
}
