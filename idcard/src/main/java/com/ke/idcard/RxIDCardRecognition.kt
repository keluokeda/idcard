package com.ke.idcard

import android.support.v4.app.FragmentActivity
import com.ke.idcard.bean.RecognitionResult
import com.ke.idcard.bean.SourceType
import io.reactivex.Observable

class RxIDCardRecognition(activity: FragmentActivity) {

    private val tag = BuildConfig.APPLICATION_ID + RxIDCardRecognition::class.java.name


    private val delegateFragment: DelegateFragment

    init {
        val fragment = activity.supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            delegateFragment = DelegateFragment()
            activity.supportFragmentManager.beginTransaction().add(delegateFragment, tag).commitNow()
        } else {
            delegateFragment = fragment as DelegateFragment
        }
    }

    fun start(type: Int, sourceType: SourceType): Observable<RecognitionResult> {
        delegateFragment.start(type, sourceType)

        return Observable.just(1)
            .flatMap {
                delegateFragment.resultSubject
            }

    }


    companion object{
        const val TYPE_ID_CARD = CardsCameraActivity.RECOGNITION_TYPE_ID_CARD

        const val TYPE_VEHICLE_LICENSE = CardsCameraActivity.RECOGNITION_TYPE_VEHICLE_LICENSE

    }
}