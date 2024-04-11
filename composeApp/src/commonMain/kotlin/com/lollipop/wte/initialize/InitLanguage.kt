package com.lollipop.wte.initialize

import com.lollipop.wte.Initialize
import com.lollipop.wte.local.LocalStrings
import com.lollipop.wte.local.Strings
import com.lollipop.wte.local.impl.LocalStringsCn
import com.lollipop.wte.preferences.Settings

object InitLanguage : Initialize.InitTask {

    override fun run() {
        val language = Settings.localLanguage
        Strings.current = findLocal(language) ?: LocalStringsCn
    }

    private fun findLocal(id: String): LocalStrings? {
        Strings.localList.forEach { local ->
            if (local.localId == id) {
                return local
            }
        }
        return null
    }

}