package com.ke.idcard.bean

import kernal.idcard.android.ResultMessage

/**
 * 行驶证识别结果
 */
data class VehicleLicenseResult(
    /**
     * 号牌号码
     */
    val plateNo: String,

    /**
     * 小型普通客车
     */
    val vehicleType: String,

    /**
     * 所有人
     */
    val owner: String,

    /**
     * 住址
     */
    val address: String,

    /**
     * 使用性质
     */
    val useCharacter: String,

    /**
     * 使用型号
     */
    val model: String,

    /**
     * 车辆识别代号
     */
    val vin: String,

    /**
     * 发动机号码
     */
    val engineNo: String,

    /**
     * 注册日期
     */
    val registerDate: String,

    /**
     * 发证日期
     */
    val issueDate: String
) : RecognitionResult {
    companion object {
        fun fromResultMessage(resultMessage: ResultMessage): VehicleLicenseResult {

            val array = resultMessage.GetRecogResult

            return VehicleLicenseResult(
                plateNo = array[1],
                vehicleType = array[2],
                owner = array[3],
                address = array[4],
                useCharacter = array[10],
                model = array[5],
                vin = array[6],
                engineNo = array[7],
                registerDate = array[8],
                issueDate = array[9]
            )
        }
    }
}