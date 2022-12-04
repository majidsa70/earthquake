package ir.sadeghi.earthquake.ui.reports.components

data class HorizontalListHeader(
    val title: String,
    val moreTitle: String,
    val titleClick: () -> Unit,
    val moreTitleClick: () -> Unit
)
