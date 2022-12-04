package ir.sadeghi.earthquake.utils.network


import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("data")
    val errorList: ErrorList? = null,
    @SerializedName("error")
    val error: String? = null,
)

data class ErrorList(
    @SerializedName("result")
    val list: List<ErrorBodyItem>? = null,
)

data class ErrorBodyItem(
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("field")
    val errorField: String? = null
)