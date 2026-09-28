package com.paisaflow.app.core

import android.content.Context
import com.paisaflow.app.core.i18n.AppLanguage

/** Tiny persisted settings (language, first-run). No secrets here. */
class Prefs(context: Context) {
    private val sp = context.applicationContext.getSharedPreferences("paisaflow", Context.MODE_PRIVATE)
    var language: AppLanguage
        get() = AppLanguage.fromCode(sp.getString("lang", AppLanguage.HI.code) ?: AppLanguage.HI.code)
        set(v) = sp.edit().putString("lang", v.code).apply()
    var onboarded: Boolean
        get() = sp.getBoolean("onboarded", false)
        set(v) = sp.edit().putBoolean("onboarded", v).apply()
}
