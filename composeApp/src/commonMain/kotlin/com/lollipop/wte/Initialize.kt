package com.lollipop.wte

import com.lollipop.wte.initialize.InitLanguage

object Initialize {

    private var isInit = false

    private val initTask = arrayOf<InitTask>(
        InitLanguage
    )

    fun init(firstCallback: () -> Unit) {
        if (isInit) {
            return
        }
        initTask.forEach { task ->
            safeInit {
                task.run()
            }
        }
        isInit = true
        firstCallback()
    }

    private fun safeInit(block: () -> Unit) {
        try {
            block()
        } catch (e: Throwable) {
            if (Config.DEBUG) {
                throw e
            } else {
                e.printStackTrace()
            }
        }
    }

    interface InitTask {
        fun run()
    }

}