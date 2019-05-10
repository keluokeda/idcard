package com.ke.idcard

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import com.ke.idcard.bean.*
import com.ke.idcard.utils.DefaultPicSavePath
import com.ke.idcard.utils.Devcode
import com.ke.idcard.utils.ImportRecog
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kernal.idcard.android.ResultMessage
import kernal.idcard.camera.CardOcrRecogConfigure
import kernal.idcard.camera.IBaseReturnMessage
import kernal.idcard.camera.UritoPathUtil

class DelegateFragment : Fragment() {


    private val request_permission_camera = 10001
    private val request_permission_storage = 10002
    private val request_code_camera = 10003
    private val request_code_gallery = 10004

    lateinit var resultSubject: PublishSubject<RecognitionResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val devCode = getString(R.string.dev_code)

        Devcode.devcode = devCode


        retainInstance = true
    }


    var type = CardsCameraActivity.RECOGNITION_TYPE_ID_CARD

    var sourceType = SourceType.Camera

    fun start(type: Int, sourceType: SourceType) {

        this.type = type
        this.sourceType = sourceType

        resultSubject = PublishSubject.create()

        if (sourceType == SourceType.Camera) {
            val permission = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)

            if (permission == PackageManager.PERMISSION_GRANTED) {
                startRecognition()
            } else {
                requestCameraPermission()
            }
        } else {
            val permission = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permission == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                requestStoragePermission()
            }

        }


    }

    private fun pickImage() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val activityInfo = intent.resolveActivityInfo(context!!.packageManager, intent.flags)

        if (activityInfo != null) {
            startActivityForResult(intent, request_code_gallery)
        } else {
            resultSubject.onNext(RecognitionError("无法打开相册软件"))
            resultSubject.onComplete()
        }

    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), request_permission_camera)
    }


    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), request_permission_storage)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val grantResult = grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (grantResult) {

            if (requestCode == request_permission_camera) {
                startRecognition()
            } else {
                pickImage()
            }
        } else {

            resultSubject.onNext(RecognitionError("用户没有授权"))
            resultSubject.onComplete()
        }
    }

    private fun startRecognition() {

        val intent = CardsCameraActivity.createIntent(type, activity!!)

        startActivityForResult(intent, request_code_camera)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == request_code_camera) {

            if (resultCode == Activity.RESULT_OK) {

                val recognitionResult = data?.getSerializableExtra("result")
                        as? RecognitionResult ?: RecognitionError("识别出错")

                resultSubject.onNext(recognitionResult)
                resultSubject.onComplete()


            } else {
                resultSubject.onNext(RecognitionError("识别取消"))
                resultSubject.onComplete()
            }
        } else if (requestCode == request_code_gallery) {

            if (resultCode == Activity.RESULT_OK) {

                getResultFromGallery(data)

            } else {
                resultSubject.onNext(RecognitionError("识别取消"))
                resultSubject.onComplete()
            }
        }
    }

    private fun getResultFromGallery(data: Intent?) {
        val uri = data?.data

        if (uri == null) {
            resultSubject.onNext(RecognitionError("识别取消"))
            resultSubject.onComplete()

            return
        }

        val imagePath = UritoPathUtil.getImageAbsolutePath(activity!!, uri)


        val dialog = ProgressDialog(activity!!)


        dialog.setMessage("正在获取数据")

        dialog.show()



        CardOcrRecogConfigure.getInstance()
            .initLanguage(activity!!.applicationContext)
            .setSaveCut(false)
            .setSaveHeadPic(false)
            .setSaveFullPic(false)
            .setnMainId(type)
            .setnSubID(0)
            .setFlag(0)
            .setnCropType(0)
            .setSavePath(DefaultPicSavePath())

        val importRecog = ImportRecog(activity!!.applicationContext, object : IBaseReturnMessage {
            override fun scanOCRIdCardSuccess(p0: ResultMessage, p1: Array<out String>) {

                dialog.dismiss()
                val recognitionResult =
                    if (type == CardsCameraActivity.RECOGNITION_TYPE_ID_CARD) IDCardResult.fromResultMessage(p0)
                    else VehicleLicenseResult.fromResultMessage(p0)

                resultSubject.onNext(recognitionResult)
                resultSubject.onComplete()

            }

            override fun scanOCRIdCardError(p0: String) {
                dialog.dismiss()

                resultSubject.onNext(RecognitionError(p0))
                resultSubject.onComplete()

            }

            override fun authOCRIdCardSuccess(p0: String?) {

            }

            override fun authOCRIdCardError(p0: String?) {

            }

        })


        Observable.just(imagePath)
            .observeOn(Schedulers.io())
            .map {
                importRecog.startImportRecogService(imagePath)
                it
            }
            .subscribe()
    }

}