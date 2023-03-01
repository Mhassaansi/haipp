/*
 * Copyright 2014 A.C.R. Development
 */
package com.appsnado.haippNew.settings.fragment

import android.os.Bundle
import com.appsnado.haippNew.BuildConfig
import com.appsnado.haippNew.R

class AboutSettingsFragment : AbstractSettingsFragment() {

    override fun providePreferencesXmlResource() = R.xml.preference_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clickablePreference(
            preference = SETTINGS_VERSION,
            summary = BuildConfig.VERSION_NAME,
            onClick = { }
        )
    }

    companion object {
        private const val SETTINGS_VERSION = "pref_version"
    }
}
