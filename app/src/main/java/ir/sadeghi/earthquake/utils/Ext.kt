package ir.sadeghi.earthquake.utils

import android.widget.TextView
import java.math.BigDecimal

fun TextView.setMagnitude(mag: Double?) {
    this.text = mag.convertMagnitude()
}

fun Double?.convertMagnitude(): String {
    return String.format(
        "%.1f", this
    )
}
