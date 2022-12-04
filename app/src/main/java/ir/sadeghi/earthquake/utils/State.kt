package ir.sadeghi.earthquake.utils

sealed class State<out T> {
    object LoadingState : State<Nothing>()
    data class ErrorState(var exception: String?) : State<Nothing>()
    data class DataState<T>(var data: T?) : State<T>()
}