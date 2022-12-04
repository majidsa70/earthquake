package ir.sadeghi.earthquake.data.repository

import javax.inject.Inject

class DateRepositoryImpl @Inject constructor(private val calendarDataSource: CalendarDataSource) : DateRepository {
    override fun getHumanRead(timestamp: Long): String {
        return calendarDataSource.getDate(timestamp)
    }

    override fun getDateInPast(days: Int): String? {
        return calendarDataSource.getCalculatedDate(days)
    }

    override fun convertToTimeAgo(timestamp: Long): String? {
        return calendarDataSource.getTimeAgo(calendarDataSource.getDate(timestamp))
    }

    override fun getDifferenceUntilNow(timestamp: Long): Long {
        return calendarDataSource.getDifferenceUntilNow(timestamp)
    }


}