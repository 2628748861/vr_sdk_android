package com.wangxiaobao.vr_sdk_android;

import android.os.Parcel;
import android.os.Parcelable;

public class WxbVrParams implements Parcelable
{
    /**
     * 当前页的标题
     * */
    public String title;
    /**
     * vr链接地址
     * */
    public String url;
    /**
     * 腾讯音视频：tc_appId
     * */
    public int tc_appId;

    /**
     * 腾讯音视频：tc_secret
     * */
    public String tc_secret;

    /**
     * VR token
     * */
    public String token;

    /**
     * VR appId
     * */
    public String appId;
    /**
     * VR 房间号
     * 语音房间ID
     * */
    public int roomId;

    /**
     * 用户id 必须是平台唯一性
     * */
    public String clientId;

    /**
     * 用户昵称
     * */
    public String clientName;

    /**
     * 用户头像
     * */
    public String img;

    /**
     * 应用平台 传入对应参数
     * android
     * ios
     * flutter
     * */
     public String platform;


    public WxbVrParams(String url, String title, int tc_appId, String tc_secret, String token, String appId, int roomId, String clientId, String clientName, String img, String platform) {
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
        this.platform = platform;
    }


    protected WxbVrParams(Parcel in) {
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
        platform = in.readString();
    }

    public static final Creator<WxbVrParams> CREATOR = new Creator<WxbVrParams>() {
        @Override
        public WxbVrParams createFromParcel(Parcel in) {
            return new WxbVrParams(in);
        }

        @Override
        public WxbVrParams[] newArray(int size) {
            return new WxbVrParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "WxbVrParams{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", tc_appId=" + tc_appId +
                ", tc_secret='" + tc_secret + '\'' +
                ", token='" + token + '\'' +
                ", appId='" + appId + '\'' +
                ", roomId=" + roomId +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", img='" + img + '\'' +
                ", platform='" + platform + '\'' +
                '}';
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
        parcel.writeString(platform);
    }
}
