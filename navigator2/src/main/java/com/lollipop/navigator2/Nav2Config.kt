package com.lollipop.navigator2

object Nav2Config {

    var logger: (String) -> Unit = { println(it) }

    fun log(value: String) {
        logger(value)
    }

}