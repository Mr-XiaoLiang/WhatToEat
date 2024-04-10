package com.lollipop.wte.info

sealed class LResult<T> {

    companion object {
        fun <T> tryDo(block: () -> T): LResult<T> {
            try {
                return Success(block())
            } catch (e: Throwable) {
                e.printStackTrace()
                return Failure(e)
            }
        }
    }

    class Success<T>(val value: T) : LResult<T>()

    class Failure<T>(val error: Throwable) : LResult<T>()

}

inline fun <reified I, reified O> LResult<I>.map(crossinline block: (I) -> O): LResult<O> {
    return when (val info = this) {
        is LResult.Failure -> {
            LResult.Failure(info.error)
        }

        is LResult.Success -> {
            LResult.tryDo { block(info.value) }
        }
    }
}
