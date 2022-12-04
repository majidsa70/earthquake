package ir.sadeghi.earthquake.data.repository

interface DateRepository {
    fun getHumanRead(timestamp: Long): String
    fun getDateInPast(days:Int): String?
    fun convertToTimeAgo(timestamp: Long): String?
    fun getDifferenceUntilNow(timestamp: Long): Long
}