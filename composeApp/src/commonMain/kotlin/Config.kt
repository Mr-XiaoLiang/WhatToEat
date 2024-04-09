object Config {

    const val APP_NAME = "WhatToEat"

    val platform: Platform by lazy {
        getPlatform()
    }

}