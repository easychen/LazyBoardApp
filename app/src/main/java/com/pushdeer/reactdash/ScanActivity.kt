package com.pushdeer.reactdash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zbar.ZBarView
import com.pushdeer.reactdash.util.DisplayHelper
import com.pushdeer.reactdash.util.SnackbarUtils
import permissions.dispatcher.*

/**
 * Description here.
 *
 * Created on: 2021/12/9 11:48 上午
 * @author lty <a href="mailto:lty81372860@gmail.com">Contact me.</a>
 */
@RuntimePermissions
class ScanActivity: AppCompatActivity() , QRCodeView.Delegate{

  private lateinit var scanner: ZBarView
  private lateinit var frameBack: View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan)

    scanner = findViewById(R.id.scanner)
    frameBack = findViewById(R.id.frame_back)

    scanner.setDelegate(this)

    val min = Math.min(DisplayHelper.getScreenWidth(this), DisplayHelper.getScreenHeight(this))
    val size = min - DisplayHelper.dp2px(this, 96)
    scanner.scanBoxView.rectWidth = size
    scanner.scanBoxView.rectHeight = size
    frameBack.setOnClickListener{ finish() }
  }

  override fun onStart() {
    super.onStart()
    frameBack.postDelayed({
      doScanWithPermissionCheck()
    },1000)
  }

  override fun onStop() {
    super.onStop()
    scanner.onDestroy()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    onRequestPermissionsResult(requestCode, grantResults)
  }

  @NeedsPermission(Manifest.permission.CAMERA)
  fun doScan() {
      scanner.startCamera()
      scanner.startSpot()
  }

  @OnPermissionDenied(Manifest.permission.CAMERA)
  fun onCameraDenied() {
    SnackbarUtils.with(frameBack)
      .setMessage("请授权使用摄像头以使用扫码功能")
      .setAction("去设置") {
        val mIntent = Intent()
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        mIntent.data = Uri.fromParts("package", packageName, null)
        startActivity(mIntent)
      }.showError()
  }

  @OnShowRationale(Manifest.permission.CAMERA)
  fun showRationaleForCamera(request: PermissionRequest) {
    Toast.makeText(this,"请授权使用摄像头以使用扫码功能", Toast.LENGTH_SHORT).show()
  }

  @OnNeverAskAgain(Manifest.permission.CAMERA)
  fun onCameraNeverAskAgain() {
    Toast.makeText(this, "禁用摄像头权限会导致无法使用扫码功能，您可在应用设置中重新开启", Toast.LENGTH_SHORT).show()
  }

  override fun onScanQRCodeSuccess(result: String) {
    scanner.stopSpot()

    val intent = Intent()
    intent.putExtra("code", result)
    setResult(Activity.RESULT_OK, intent)
    finish()
  }

  override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

  }

  override fun onScanQRCodeOpenCameraError() {

  }

}