package com.lollipop.wte.initialize

import com.lollipop.navigator.Navigator
import com.lollipop.wte.Initialize
import com.lollipop.wte.router.Router

object InitPageRouter : Initialize.InitTask {

    override fun run() {
        Navigator.init {
            Navigator.register(Router.Main)
            Navigator.register(Router.Manager)
            Navigator.register(Router.ItemAdd)
            Navigator.register(Router.MenuInput)
            Navigator.register(Router.MenuOutput)
        }
    }

}