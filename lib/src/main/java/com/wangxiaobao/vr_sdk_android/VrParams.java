package com.wangxiaobao.vr_sdk_android;

import android.os.Parcel;
import android.os.Parcelable;

public class VrParams implements Parcelable
{
    //当前页的标题
    public String title;
    //vr链接地址
    public String url;

    //腾讯音视频：tc_appId
    public int tc_appId;
    //腾讯音视频：tc_secret
    public String tc_secret;


    //token
    public String token;

    public String appId;
    //语音房间ID
    public int roomId;

    //用户ID
    public String clientId;
    //用户昵称
    public String clientName;
    //用户头像
    public String img;

    //2:小程序发起 非2:app发起
    public int terminal;


    public VrParams(String title, String url, int tc_appId, String tc_secret, String token, String appId, int roomId, String clientId, String clientName, String img, int terminal) {
        this.title = title;
        this.url = url;
        this.tc_appId = tc_appId;
        this.tc_secret = tc_secret;
        this.token = token;
        this.appId = appId;
        this.roomId = roomId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.img = img;
        this.terminal = terminal;
    }

    public VrParams() {
    }

    protected VrParams(Parcel in) {
        title = in.readString();
        url = in.readString();
        tc_appId = in.readInt();
        tc_secret = in.readString();
        token = in.readString();
        appId = in.readString();
        roomId = in.readInt();
        clientId = in.readString();
        clientName = in.readString();
        img = in.readString();
        terminal = in.readInt();
    }

    public static final Creator<VrParams> CREATOR = new Creator<VrParams>() {
        @Override
        public VrParams createFromParcel(Parcel in) {
            return new VrParams(in);
        }

        @Override
        public VrParams[] newArray(int size) {
            return new VrParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeInt(tc_appId);
        parcel.writeString(tc_secret);
        parcel.writeString(token);
        parcel.writeString(appId);
        parcel.writeInt(roomId);
        parcel.writeString(clientId);
        parcel.writeString(clientName);
        parcel.writeString(img);
        parcel.writeInt(terminal);
    }
}
