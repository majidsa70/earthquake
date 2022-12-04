package ir.sadeghi.earthquake.data.repository

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CalendarDataSource @Inject constructor() {

    fun getDifferenceUntilNow(timestamp: Long): Long {
        return kotlin.math.abs(timestamp - Date().time)
        /*val currentDate = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = timestamp
        cal.add(Calendar.MINUTE,5)
        val beforeThisDate: Date = cal.getTime()
        cal.add(GregorianCalendar.MINUTE, -10) //set a time 5 minutes before the timestamp

        val afterThisDate: Date = cal.getTime()
        currentDate.time = System.currentTimeMillis()

        if ((currentDate.before(beforeThisDate))&&(currentDate.after(afterThisDate))){
            //do stuff, current time is within the two dates (5 mins either side of the server timestamp)
        } else {
            //current time is not within the two dates
        }*/
    }

    fun getCalculatedDate(days: Int): String? {
        val cal: Calendar = Calendar.getInstance()
        val s = SimpleDateFormat("yyyy-MM-dd")
        cal.add(Calendar.DAY_OF_YEAR, -days)
        return s.format(Date(cal.timeInMillis))
    }

    fun getDate(timestamp: Long): String {
        val c = Calendar.getInstance()
        c.timeInMillis = timestamp
        val d = c.time
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(d)
    }


    fun getTimeAgo(date: String): String? {

        var convTime: String? = null

        val suffix = "ago"

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val pasTime = dateFormat.parse(date)
            pasTime?.time ?: return null
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second Seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            // Log.e("ConvTimeE", e.message)
        }

        return convTime
    }
}