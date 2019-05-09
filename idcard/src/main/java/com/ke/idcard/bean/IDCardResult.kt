package com.ke.idcard.bean

import kernal.idcard.android.ResultMessage

/**
 * 身份证识别结果
 */
data class IDCardResult(
    /**
     * 姓名
     */
    val name: String,
    /**
     * 性别
     */
    val gender: String,
    /**
     * 民族
     */
    val nationality: String,
    /**
     * 出生日期
     */
    val birthday: String,
    /**
     * 住址
     */
    val address: String,

    /**
     * 身份证号码
     */
    val IDNumber: String
) : RecognitionResult {

    companion object {
        fun fromResultMessage(resultMessage: ResultMessage): IDCardResult {


            return IDCardResult(
                name = resultMessage.GetRecogResult[1],
                gender = resultMessage.GetRecogResult[2],
                nationality = resultMessage.GetRecogResult[3],
                birthday = resultMessage.GetRecogResult[4],
                address = resultMessage.GetRecogResult[5],
                IDNumber = resultMessage.GetRecogResult[6]
            )
        }
    }
}