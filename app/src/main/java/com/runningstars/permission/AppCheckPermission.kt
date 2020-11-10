package com.laposte.bscc.vigik3.loading_badge.permission

//import android.app.Activity
//import com.laposte.bscc.tools.log.logger.permission.PermissionUtil
//import com.laposte.bscc.vigik3.common.permission.ICheckPermission
//
//class AppCheckPermission(private val listener: AppPermissionAskListener) : ICheckPermission {
//    private var doCheckPermission = true
//
//    override fun checkPermission(activity: Activity?) {
//        if (doCheckPermission) {
//            doCheckPermission = false
//            PermissionUtil.checkPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE", listener)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == AppPermissionAskListener.PERMISSION_REQUEST_CODE ) {
//            if (grantResults.isNotEmpty() && grantResults[0] == 0) {
//                doCheckPermission = false
//                listener.onPermissionGranted()
//            } else {
//                doCheckPermission = true
//            }
//        }
//    }
//
//    override fun continueCheckPermission() {
//        doCheckPermission = true
//    }
//}
