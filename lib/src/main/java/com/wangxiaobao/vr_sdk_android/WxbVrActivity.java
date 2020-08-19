package com.wangxiaobao.vr_sdk_android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;

public abstract class WxbVrActivity extends Activity implements IWxbVrControll {
    private final String TAG = "WXB_VR_SDK";
    protected Context mContext;
    private ImageButton backBtn;
    private RelativeLayout nav_layout;
    private TextView vr_title;
    private LinearLayout webview_parent;
    private AgentWeb mAgentWeb;
    private WxbVrParams vrParams;

    private TRTCCloud mTRTCCloud;
    private boolean isLoadFinished;
    private boolean isError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);
        Bundle bundle=savedInstanceState==null?getIntent().getExtras():savedInstanceState;
        vrParams=bundle.getParcelable("data");

        checkParams();
        initRtc();
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data",vrParams);
    }

    private void checkParams(){
        if(vrParams==null){
            Toast.makeText(mContext,"参数不能为空",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void initRtc(){
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.enableAudioVolumeEvaluation(800);
        mTRTCCloud.setListener(new TRTCCloudListener(){
            @Override
            public void onError(int errCode, String errMsg, Bundle extraInfo) {
                Log.d(TAG,"onError: " + errMsg + "[" + errCode+ "]");
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    finish();
                }
            }

            @Override
            public void onEnterRoom(long result) {
                super.onEnterRoom(result);
                if(result>0){
                    Log.d(TAG,"进入语音房间成功("+result+")");
                    onEnterVoiceRoomSuccess();
                }else{
                    Log.d(TAG,"进入语音房间失败("+result+")");
                    onEnterVoiceRoomFail();
                }
            }

            @Override
            public void onUserVoiceVolume(ArrayList<TRTCCloudDef.TRTCVolumeInfo> arrayList, int i) {
                super.onUserVoiceVolume(arrayList, i);
            }
        });
        mTRTCCloud.startLocalAudio();
        mTRTCCloud.muteAllRemoteAudio(false);
        enable16KSampleRate(true);
    }
    private void initView(){
        mContext=this;
        nav_layout=findViewById(R.id.nav_layout);
        backBtn=findViewById(R.id.backBtn);
        vr_title=findViewById(R.id.vr_title);
        webview_parent=findViewById(R.id.webview_parent);

        resetNav(nav_layout,backBtn,vr_title);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String url=vrParams.url+"?appId="+vrParams.appId+"&token="+vrParams.token+"&appId"+vrParams.appId+"&isMaster=1&roomId="+vrParams.roomId+"&title="+vrParams.title+"&terminal=0&clientId=&platform="+vrParams.platform;
        Log.d(TAG,"h5加载url : "+url);
         mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webview_parent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                 .setWebChromeClient(new WebChromeClient(){

                     @Override
                     public boolean onConsoleMessage(ConsoleMessage cm) {
                         //return super.onConsoleMessage(consoleMessage);
                         Log.e(TAG,"webView "+ cm.message() + " -- From line "
                                 + cm.lineNumber() + " of "
                                 + cm.sourceId() );
                         return true;
                     }
                 })
                 .setWebViewClient(new WebViewClient(){
                     @Override
                     public void onPageFinished(WebView view, String url) {
                         super.onPageFinished(view, url);
                         isLoadFinished=true;
                         callJs("initRoom('"+vrParams.clientId+"','"+vrParams.clientName+"','"+vrParams.img+"')");
                     }

                     @Override
                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                         super.onReceivedError(view, request, error);
                         isError=true;
                     }
                 })
                .createAgentWeb()
                .ready()
                .go(url);

        //配置js调用java
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface());
    }

    /**
     * java调用js
     * */
    private void callJs(String method){
        Log.d(TAG,"java调用js方法: "+method);
        mAgentWeb.getJsAccessEntrace().quickCallJs(method);
    }

    class AndroidInterface extends Object {
        @JavascriptInterface
        public void showMessage(String msg){
            Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
            Log.d(TAG,"js回调 showMessage : "+msg);
        }

        //创建vr房间回调
        @JavascriptInterface
        public void createdRoom(String json){
            Log.d(TAG,"js回调 createdRoom : "+json);
            try {
                JSONObject jsonObject=new JSONObject(json);
                boolean status=jsonObject.getBoolean("status");
                if(status){

                    Log.d(TAG,"vr房间创建成功");
                    TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
                    trtcParams.sdkAppId = vrParams.tc_appId;
                    trtcParams.userId = vrParams.clientId;
                    trtcParams.roomId = vrParams.roomId;
                    trtcParams.userSig = TrtcUtils.genTestUserSig(vrParams.clientId,vrParams.tc_appId,vrParams.tc_secret);
                    mTRTCCloud.enterRoom(trtcParams, TRTC_APP_SCENE_VIDEOCALL);
                    onEnterRoomSuccess();
                }else{
                    Log.d(TAG,"vr房间创建失败");
                    onEnterRoomFail();
                }
            } catch (JSONException e) {
                Log.d(TAG,"vr房间创建失败  房间参数解析失败"+vrParams.toString());
                onEnterRoomFail();
            }
        }
        //退出房间的回调
        @JavascriptInterface
        public void exitRoom(){
            Log.d(TAG,"js回调 exitRoom : ");
            if(mTRTCCloud!=null){
                mTRTCCloud.exitRoom();
            }
            finish();
        }

        //重新邀请的回调
        @JavascriptInterface
        public void invite(){
            Log.d(TAG,"js回调  invite");
            onVrInviteShareToWeChat();
        }

        //开关麦克风
        @JavascriptInterface
        public void openMic()
        {
            Log.d(TAG,"js回调  openMic");
            mTRTCCloud.muteLocalAudio(false);
        }

        //开关麦克风
        @JavascriptInterface
        public void stopMic()
        {
            Log.d(TAG,"js回调  stopMic");
            mTRTCCloud.muteLocalAudio(true);
        }
    }


    @Override
    public void onBackPressed() {
        if(!isLoadFinished||isError){
            super.onBackPressed();
        }
        else{
            callJs("endLook()");
        }
    }

    @Override
    public void finish() {
        callJs("logout()");
        super.finish();
    }


    @Override
    protected void onResume() {
        callJs("homeApp('"+2+"')");
        if(mAgentWeb!=null){
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        callJs("homeApp('"+1+"')");
        if(mAgentWeb!=null){
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(mAgentWeb!=null){
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }


    /**覆写该函数用于改变导航布局
     * @param nav_layout  导航-根布局
     * @param backBtn 导航-返回键
     * @param vr_title 导航-标题
     */
    protected void resetNav(RelativeLayout nav_layout,ImageButton backBtn,TextView vr_title){
        vr_title.setText(vrParams.title==null?"":vrParams.title);
        backBtn.setPadding(20,0,20,0);
    }

    /**
     * 声音采样率
     *
     * @param enable true 开启16k采样率 false 开启48k采样率
     */
    public void enable16KSampleRate(boolean enable) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("api", "setAudioSampleRate");
            JSONObject params = new JSONObject();
            params.put("sampleRate", enable ? 16000 : 48000);
            jsonObject.put("params", params);
            mTRTCCloud.callExperimentalAPI(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
