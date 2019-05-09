package com.ke.idcard_demo

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ke.idcard.RxIDCardRecognition
import com.ke.idcard.bean.SourceType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)


        button.setOnClickListener {
            RxIDCardRecognition(this)
                .start(RxIDCardRecognition.TYPE_ID_CARD, SourceType.Camera)
                .subscribe {
                    logMessage(it.toString())
                }

        }


        button2.setOnClickListener {
            RxIDCardRecognition(this)
                .start(RxIDCardRecognition.TYPE_ID_CARD, SourceType.Gallery)
                .subscribe {
                    logMessage(it.toString())
                }
        }


        button3.setOnClickListener {
            RxIDCardRecognition(this)
                .start(RxIDCardRecognition.TYPE_VEHICLE_LICENSE, SourceType.Camera)
                .subscribe {
                    logMessage(it.toString())
                }
        }

        button4.setOnClickListener {
            RxIDCardRecognition(this)
                .start(RxIDCardRecognition.TYPE_VEHICLE_LICENSE, SourceType.Gallery)
                .subscribe {
                    logMessage(it.toString())
                }
        }




    }


    private fun logMessage(message: String) {

        Log.e("TAG", message)
    }
}
