package com.chen.maplibrary;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.List;

/**
 * author  : czh
 * create Date : 2018/5/21  15:12
 * 详情 : 高德导航界面
 */
public class NavigationActivity extends Activity implements AMapNaviViewListener, AMapNaviListener {

    private AMapNaviView mAMapNaviView;
    private AMapNavi mAMapNavi;
//    private SpeechSynthesizer mTts;
    private List<NaviLatLng> startLatlngs;
    private List<NaviLatLng> targetLatlngs;
    private String navType;//路线规划类型 1: 驾车 2：骑行 3：步行

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navgation);
        //设置只能竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getIntentValue();
        //获取 AMapNaviView 实例
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.setAMapNaviViewListener(this);
        //当地图没有显示的时候检查是否有调用onCreate()方法。
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        //添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener(this);
//        //设置讯飞语音合成
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + "5a1f7d7b");
//        mTts = SpeechSynthesizer.createSynthesizer(this, null);
//        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        //设置在线合成发音人
//        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
//        //设置合成语速
//        mTts.setParameter(SpeechConstant.SPEED, "60");
//        //设置合成音调
//        mTts.setParameter(SpeechConstant.PITCH, "50");
//        //设置合成音量
//        mTts.setParameter(SpeechConstant.VOLUME, "100");

    }
    @Override
    public Resources getResources() {
        return getBaseContext().getResources();
    }
    /**
     * 获取开始和重点坐标
     */
    private void getIntentValue() {
        startLatlngs = getIntent().getParcelableArrayListExtra("StartLatlngs");
        targetLatlngs = getIntent().getParcelableArrayListExtra("TargetLatlngs");
        navType = getIntent().getStringExtra("NavType");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        mAMapNavi.destroy();
//        if (mTts != null) {
//            mTts.stopSpeaking();
//        }
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    /**
     * 路线规划
     */
    @Override
    public void onInitNaviSuccess() {
        switch (Integer.valueOf(navType)) {
            case 1:
                driver();
                break;
            case 2:
                bike();
                break;
            case 3:
                walk();
                break;
        }
    }

    /**
     * 步行
     */
    private void walk() {
        mAMapNavi.calculateWalkRoute(startLatlngs.get(0), targetLatlngs.get(0));
    }

    /**
     * 骑行导航
     */
    private void bike() {
        mAMapNavi.calculateRideRoute(startLatlngs.get(0), targetLatlngs.get(0));
    }

    /**
     * 方法: 驾车导航
     * int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
     * 参数:
     *
     * @congestion 躲避拥堵
     * @avoidhightspeed 不走高速
     * @cost 避免收费
     * @hightspeed 高速优先
     * @multipleroute 多路径
     * <p>
     * 说明:
     * 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
     * 注意:
     * 不走高速与高速优先不能同时为true
     * 高速优先与避免收费不能同时为true
     */
    private void driver() {
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, true, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(startLatlngs, targetLatlngs, null, strategy);
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    /**
     * 定位位置改变
     *
     * @param aMapNaviLocation
     */
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    /**
     * 在线语音合成
     *
     * @param i
     * @param s
     */
    @Override
    public void onGetNavigationText(int i, final String s) {
        //设置本地合成发音人voicer为空，默认通过语记界面指定发音人。
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (mTts != null) {
//                    mTts.startSpeaking(s, null);
//                }
//            }
//        }).start();
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        mAMapNavi.startNavi(NaviType.GPS);
//        mAMapNavi.startNavi(NaviType.EMULATOR);
//        //当使用模拟导航的时候可以设置模拟导航速度。
//        mAMapNavi.setEmulatorNaviSpeed(75);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        mAMapNavi.startNavi(NaviType.GPS);
//        mAMapNavi.startNavi(NaviType.EMULATOR);
//        //当使用模拟导航的时候可以设置模拟导航速度。
//        mAMapNavi.setEmulatorNaviSpeed(75);
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }
}
