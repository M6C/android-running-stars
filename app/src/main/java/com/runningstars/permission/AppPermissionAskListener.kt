package com.laposte.bscc.vigik3.loading_badge.permission

//import android.app.Activity
//import androidx.core.app.ActivityCompat
//import com.laposte.bscc.tools.log.logger.permission.PermissionUtil
//import com.laposte.bscc.tools.log.logger.permission.listener.PermissionAskListener
//import com.laposte.bscc.vigik3.loading_badge.R
//
//abstract class AppPermissionAskListener(private val activity: Activity) : PermissionAskListener {
//
//    companion object {
//        const val PERMISSION_REQUEST_CODE = 101
//        var permissions =
//            arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")
//    }
//
//    override fun onPermissionAsk() {ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)}
//    override fun onPermissionPreviouslyDenied() {PermissionUtil.showDialogRequestPermission( activity, permissions, PERMISSION_REQUEST_CODE, activity.getString(R.string.msg_sms_permission))}
//    override fun onPermissionDisabled() {PermissionUtil.showDialogSetting(activity, activity.getString(R.string.msg_error_sms_permission))}
//}
