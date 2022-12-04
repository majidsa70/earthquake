package ir.sadeghi.earthquake.ext

import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ir.sadeghi.earthquake.R


fun Context.isPermittedLocationAccess(): Boolean {
    return (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
            )
}


fun Activity.showRequestLocationPermission(requestCode: Int) {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        requestCode
    )
}

fun Context.gpsIsOn(): Boolean {
    val manager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    return (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))

}

fun Activity.buildAlertMessageNoGps() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
        .setCancelable(false)
        .setPositiveButton(
            "Yes"
        ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        .setNegativeButton(
            "No"
        ) { dialog, _ -> dialog.cancel() }
    val alert: AlertDialog = builder.create()
    alert.setCancelable(true)
    alert.setCanceledOnTouchOutside(true)
    alert.show()
}


fun Activity.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.showToast(message: String?) {
    if (null != activity && null != message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}

fun Boolean?.iF(onTrue: () -> Unit, onFalse: (() -> Unit)? = null) {
    if (this == true) {
        onTrue()
    } else {
        onFalse?.invoke()
    }
}



fun TextView.setTwoPartText(
    boldText: String?, otherText: String?,
    @ColorRes boldColor: Int = R.color.primary
) {
    val wordBold: Spannable = SpannableString(boldText)

    wordBold.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, boldColor)),
        0,
        wordBold.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    val wordTwo: Spannable =
        SpannableString(otherText)

    this.text = wordTwo
    this.append(wordBold)

}

fun String?.getPlaceFromEQ(): String? {
    return this?.split(", ")?.let { split ->
        split[split.size - 1]
    }
}