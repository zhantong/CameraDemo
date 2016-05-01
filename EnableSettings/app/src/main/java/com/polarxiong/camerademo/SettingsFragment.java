package com.polarxiong.camerademo;

import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhantong on 16/4/30.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String KEY_PREF_PREV_SIZE="preview_size";
    public static final String KEY_PREF_PIC_SIZE="picture_size";
    public static final String KEY_PREF_VIDEO_SIZE="video_size";
    public static final String KEY_PREF_FLASH_MODE="flash_mode";
    public static final String KEY_PREF_FOCUS_MODE="focus_mode";
    public static final String KEY_PREF_WHITE_BALANCE="white_balance";
    public static final String KEY_PREF_SCENE_MODE="scene_mode";
    public static final String KEY_PREF_GPS_DATA="gps_data";
    public static final String KEY_PREF_EXPOS_COMP="exposure_compensation";
    public static final String KEY_PREF_JPEG_QUALITY="jpeg_quality";
    static Camera mCamera;
    static Camera.Parameters mParameters;
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("test!!!");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        loadSupportedPreviewSize();
        loadSupportedPictureSize();
        loadSupportedVideoeSize();
        loadSupportedFlashMode();
        loadSupportedFocusMode();
        loadSupportedWhiteBalance();
        loadSupportedSceneMode();
        loadSupportedExposeCompensation();
        setDefault();
    }
    public static void passCamera(Camera camera){
        mCamera=camera;
        mParameters=camera.getParameters();
    }
    public void setDefault(){
        ListPreference prefPreviewSize=(ListPreference)getPreferenceScreen().findPreference(KEY_PREF_PREV_SIZE);
        if(prefPreviewSize.getValue()==null){
            prefPreviewSize.setValueIndex(0);
            ListPreference prefPictureSize=(ListPreference)getPreferenceScreen().findPreference(KEY_PREF_PIC_SIZE);
            prefPictureSize.setValueIndex(0);
            ListPreference prefVideoSize=(ListPreference)getPreferenceScreen().findPreference(KEY_PREF_VIDEO_SIZE);
            prefVideoSize.setValueIndex(0);
            ListPreference prefFocusMode=(ListPreference)getPreferenceScreen().findPreference(KEY_PREF_FOCUS_MODE);
            if(prefFocusMode.findIndexOfValue("continuous-picture")!=-1){
                prefFocusMode.setValue("continuous-picture");
            }else{
                prefFocusMode.setValue("continuous-video");
            }
        }
    }
    public static void init(SharedPreferences sharedPref){
        setPreviewSize(sharedPref.getString(KEY_PREF_PREV_SIZE,""));
        setPictureSize(sharedPref.getString(KEY_PREF_PIC_SIZE,""));
        setFlashMode(sharedPref.getString(KEY_PREF_FLASH_MODE,""));
        setFocusMode(sharedPref.getString(KEY_PREF_FOCUS_MODE,""));
        setWhiteBalance(sharedPref.getString(KEY_PREF_WHITE_BALANCE,""));
        setSceneMode(sharedPref.getString(KEY_PREF_SCENE_MODE,""));
        setExposComp(sharedPref.getString(KEY_PREF_EXPOS_COMP,""));
        setJpegQuality(sharedPref.getString(KEY_PREF_JPEG_QUALITY,""));
        setGpsData(sharedPref.getBoolean(KEY_PREF_GPS_DATA,false));
        mCamera.stopPreview();
        mCamera.setParameters(mParameters);
        mCamera.startPreview();
    }
    private void  loadSupportedPreviewSize(){
        cameraSizeListToListPreference(mParameters.getSupportedPreviewSizes(),KEY_PREF_PREV_SIZE);
    }
    private  void  loadSupportedPictureSize(){
        cameraSizeListToListPreference(mParameters.getSupportedPictureSizes(),KEY_PREF_PIC_SIZE);
    }
    private void  loadSupportedVideoeSize(){
        cameraSizeListToListPreference(mParameters.getSupportedVideoSizes(),KEY_PREF_VIDEO_SIZE);
    }
    private void  loadSupportedFlashMode(){
        stringListToListPreference(mParameters.getSupportedFlashModes(),KEY_PREF_FLASH_MODE);
    }
    private void  loadSupportedFocusMode(){
        stringListToListPreference(mParameters.getSupportedFocusModes(),KEY_PREF_FOCUS_MODE);
    }
    private void  loadSupportedWhiteBalance(){
        stringListToListPreference(mParameters.getSupportedWhiteBalance(),KEY_PREF_WHITE_BALANCE);
    }
    private void  loadSupportedSceneMode(){
        stringListToListPreference(mParameters.getSupportedSceneModes(),KEY_PREF_SCENE_MODE);
    }
    private void  loadSupportedExposeCompensation(){
        int minExposComp=mParameters.getMinExposureCompensation();
        int maxExposComp=mParameters.getMaxExposureCompensation();
        List<String> exposComp=new ArrayList<>();
        for(int value=minExposComp;value<=maxExposComp;value++){
            exposComp.add(Integer.toString(value));
        }
        stringListToListPreference(exposComp,KEY_PREF_EXPOS_COMP);
    }
    private void cameraSizeListToListPreference(List<Camera.Size> list,String key){
        List<String> stringList=new ArrayList<>();
        for(Camera.Size size:list){
            String stringSize=size.width+"x"+size.height;
            stringList.add(stringSize);
        }
        stringListToListPreference(stringList,key);
    }
    private void stringListToListPreference(List<String> list,String key){
        final CharSequence[] charSeq=list.toArray(new CharSequence[list.size()]);
        ListPreference listPref=(ListPreference)getPreferenceScreen().findPreference(key);
        listPref.setEntries(charSeq);
        listPref.setEntryValues(charSeq);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key){
        switch (key){
            case KEY_PREF_PREV_SIZE:
                setPreviewSize(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_PIC_SIZE:
                setPictureSize(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_FOCUS_MODE:
                setFocusMode(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_FLASH_MODE:
                setFlashMode(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_WHITE_BALANCE:
                setWhiteBalance(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_SCENE_MODE:
                setSceneMode(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_EXPOS_COMP:
                setExposComp(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_JPEG_QUALITY:
                setJpegQuality(sharedPreferences.getString(key,""));
                break;
            case KEY_PREF_GPS_DATA:
                setGpsData(sharedPreferences.getBoolean(key,false));
                break;
        }
        mCamera.stopPreview();
        mCamera.setParameters(mParameters);
        mCamera.startPreview();
    }
    private static void setPreviewSize(String key){
        String[] split=key.split("x");
        mParameters.setPreviewSize(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
    }
    private static void setPictureSize(String key){
        String[] split=key.split("x");
        mParameters.setPictureSize(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
    }
    private static void setFocusMode(String key){
        mParameters.setFocusMode(key);
    }
    private static void setFlashMode(String key){
        mParameters.setFlashMode(key);
    }
    private static void setWhiteBalance(String key){
        mParameters.setWhiteBalance(key);
    }
    private static void setSceneMode(String key){
        mParameters.setSceneMode(key);
    }
    private static void setExposComp(String key){
        mParameters.setExposureCompensation(Integer.parseInt(key));
    }
    private static void setJpegQuality(String key){
        mParameters.setJpegQuality(Integer.parseInt(key));
    }
    private static void setGpsData(Boolean key){
        if(key.equals(false)){
            mParameters.removeGpsData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
