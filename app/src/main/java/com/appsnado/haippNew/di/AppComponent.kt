package com.appsnado.haippNew.di

import com.appsnado.haippNew.BrowserApp
import com.appsnado.haippNew.adblock.BloomFilterAdBlocker
import com.appsnado.haippNew.adblock.NoOpAdBlocker
import com.appsnado.haippNew.browser.SearchBoxModel
import com.appsnado.haippNew.browser.activity.BrowserActivity
import com.appsnado.haippNew.browser.activity.ThemableBrowserActivity
import com.appsnado.haippNew.browser.bookmarks.BookmarksDrawerView
import com.appsnado.haippNew.device.BuildInfo
import com.appsnado.haippNew.dialog.LightningDialogBuilder

import com.appsnado.haippNew.search.SuggestionsAdapter
import com.appsnado.haippNew.settings.activity.SettingsActivity
import com.appsnado.haippNew.settings.activity.ThemableSettingsActivity
import com.appsnado.haippNew.settings.fragment.*
import com.appsnado.haippNew.view.LightningChromeClient
import com.appsnado.haippNew.view.LightningView
import com.appsnado.haippNew.view.LightningWebClient
import android.app.Application
import com.appsnado.haippNew.download.LightningDownloadListener
import com.appsnado.haippNew.reading.activity.ReadingActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (AppBindsModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun buildInfo(buildInfo: BuildInfo): Builder

        fun build(): AppComponent
    }

    fun inject(activity: BrowserActivity)

    fun inject(fragment: BookmarkSettingsFragment)

    fun inject(builder: LightningDialogBuilder)

    fun inject(lightningView: LightningView)

    fun inject(activity: ThemableBrowserActivity)

    fun inject(advancedSettingsFragment: AdvancedSettingsFragment)

    fun inject(app: BrowserApp)

    fun inject(activity: ReadingActivity)

    fun inject(webClient: LightningWebClient)

    fun inject(activity: SettingsActivity)

    fun inject(activity: ThemableSettingsActivity)

    fun inject(listener: LightningDownloadListener)

    fun inject(fragment: PrivacySettingsFragment)

    fun inject(fragment: DebugSettingsFragment)

    fun inject(suggestionsAdapter: SuggestionsAdapter)

    fun inject(chromeClient: LightningChromeClient)

    fun inject(searchBoxModel: SearchBoxModel)

    fun inject(generalSettingsFragment: GeneralSettingsFragment)

    fun inject(displaySettingsFragment: DisplaySettingsFragment)

    fun inject(adBlockSettingsFragment: AdBlockSettingsFragment)

    fun inject(bookmarksView: BookmarksDrawerView)

    fun provideBloomFilterAdBlocker(): BloomFilterAdBlocker

    fun provideNoOpAdBlocker(): NoOpAdBlocker

}
