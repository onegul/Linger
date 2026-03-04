package app.linger.core.util

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error(val throwable: Throwable, val message: String? = throwable.message) :
        Result<Nothing>()

    data object Loading : Result<Nothing>()
}