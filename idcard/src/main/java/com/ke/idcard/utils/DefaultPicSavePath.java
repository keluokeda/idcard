package com.ke.idcard.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.format.Time;
import kernal.idcard.camera.SavePath;

import java.io.File;

/**
 * @author A@H
 * Description: 该类实现了SavePath方法 主要是生成图片的路径
 */
public class DefaultPicSavePath implements SavePath {
    public String PATH = Environment.getExternalStorageDirectory().toString()
            + "/wtimage/";
    private String parentPath;

    public DefaultPicSavePath(){
        File file=new File(PATH);
        if(!file.exists()){
            file.mkdirs();
        }
         parentPath=PATH;
    }

    public DefaultPicSavePath(@NonNull String parentPath){
        if(parentPath!=null&&!parentPath.equals("")){
            this.parentPath=parentPath;
        }else{
            this.parentPath=PATH;
        }
        File file=new File(parentPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public String getCropPicPath() {
        String cropPicPath=parentPath+"Android_WintoneIDCard_"+pictureName()+"_cut.jpg";
        return cropPicPath;
    }

    @Override
    public String getFullPicPath() {
        String fullPicPath=parentPath+"Android_WintoneIDCard_"+pictureName()+"_full.jpg";
        return fullPicPath;
    }

    @Override
    public String getHeadPicPath() {
        String headPicPath=parentPath+"Android_WintoneIDCard_"+pictureName()+"_head.jpg";
        return headPicPath;
    }

    @Override
    public String getThaiCodePath() {
        String thaiCoddPath=parentPath+"Android_WintoneIDCard_"+pictureName()+"thaicode.jpg";
        return thaiCoddPath;
    }

    public String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); //  Get system time。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10) {
            str = str + "0" + String.valueOf(date);
        } else {
            str = str + String.valueOf(date);
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
        return str;
    }

}
