package com.ke.idcard.utils;

import android.content.Context;
import com.ke.idcard.R;
import kernal.idcard.android.ResultMessage;

/**
 * @author A@H
 *
 */
public class ManageIDCardRecogResult {
    /**
     *  识别成功，返回识别结果
     *  @param resultMessage
     * @return
     */
    public static String managerSucessRecogResult(ResultMessage resultMessage){
        String recogResultString="";
        String[] GetFieldName = resultMessage.GetFieldName;
        String[] GetRecogResult = resultMessage.GetRecogResult;
        for (int i = 1; i < GetFieldName.length; i++) {
            if (GetRecogResult[i] != null) {
                if (!recogResultString.equals("")) {
                    recogResultString = recogResultString
                            + GetFieldName[i] + ":" + GetRecogResult[i]
                            + ",";
                } else {
                    recogResultString = GetFieldName[i] + ":"
                            + GetRecogResult[i] + ",";
                }
            }
        }

        if (resultMessage.ReturnRecogIDCard == 2||resultMessage.ReturnRecogIDCard==3) {
            recogResultString = recogResultString + "是否是复印件:" + resultMessage.IsIDCopy;
        }
        if(resultMessage.ReturnRecogIDCard ==2011) {
            recogResultString=recogResultString+"\n"+"特征点位置:";
            for (int i = 0; i < 6; i++) {
                if (i == 5) {
                    recogResultString = recogResultString + resultMessage.xpos[i] + ":" + resultMessage.ypos[i];
                } else {
                    recogResultString = recogResultString + resultMessage.xpos[i] + ":" + resultMessage.ypos[i] + "---";
                }

            }
        }
       return recogResultString;
    }

    /**
     * 识别失败返回错误值
     * @param resultMessage
     * @return
     */
    public static String managerErrorRecogResult(Context context, ResultMessage resultMessage){

        String string = "";
        if (resultMessage.ReturnAuthority == -100000) {
            string = context.getString(R.string.exception)
                    + resultMessage.ReturnAuthority;
        } else if (resultMessage.ReturnAuthority != 0) {
            string = context.getString(R.string.exception1)
                    + resultMessage.ReturnAuthority;
        } else if (resultMessage.ReturnInitIDCard != 0) {
            string = context.getString(R.string.exception2)
                    + resultMessage.ReturnInitIDCard;
        } else if (resultMessage.ReturnLoadImageToMemory != 0) {
            if (resultMessage.ReturnLoadImageToMemory == 3) {
                string = context.getString(R.string.exception3)
                        + resultMessage.ReturnLoadImageToMemory;
            } else if (resultMessage.ReturnLoadImageToMemory == 1) {
                string = context.getString(R.string.exception4)
                        + resultMessage.ReturnLoadImageToMemory;
            } else {
                string = context.getString(R.string.exception5)
                        + resultMessage.ReturnLoadImageToMemory;
            }
        } else if (resultMessage.ReturnRecogIDCard <= 0) {
            if (resultMessage.ReturnRecogIDCard == -6) {
                string = context.getString(R.string.exception9);
            } else {
                string = context.getString(R.string.exception6)
                        + resultMessage.ReturnRecogIDCard;
            }
        }
        return string;
    }

}
