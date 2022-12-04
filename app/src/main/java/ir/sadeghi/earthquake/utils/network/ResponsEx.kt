package ir.sadeghi.earthquake.utils.network

import ir.sadeghi.earthquake.utils.State


fun <T : Any> NetworkResponse<T, APIErrorResponse<ErrorBody>>.convertToState(): State<T> {

    return when (this@convertToState) {
        is NetworkResponse.Success -> State.DataState(body)
        is NetworkResponse.Empty -> State.DataState(null)
        is NetworkResponse.APIError -> apiErrorResponse.convertError()
        is NetworkResponse.NetworkError -> State.ErrorState(exception.message)
        is NetworkResponse.ProtocolError -> State.ErrorState(exception.message)
        is NetworkResponse.UnknownError -> State.ErrorState(throwable?.message)
    }
}



private fun APIErrorResponse<ErrorBody>.convertError(): State.ErrorState {
    return when (this) {
        is APIErrorResponse.Unauthenticated -> State.ErrorState(error.error)

        is APIErrorResponse.ClientErrorResponse -> State.ErrorState(error.error)
        is APIErrorResponse.ServerErrorResponse -> State.ErrorState(error.error)
        is APIErrorResponse.UnexpectedErrorResponse -> State.ErrorState(error.error)
        is APIErrorResponse.NotFoundResponse -> State.ErrorState(error.error)
    }
}

