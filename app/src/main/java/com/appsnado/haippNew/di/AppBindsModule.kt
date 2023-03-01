package com.appsnado.haippNew.di

import com.appsnado.haippNew.adblock.allowlist.AllowListModel
import com.appsnado.haippNew.adblock.allowlist.SessionAllowListModel
import com.appsnado.haippNew.adblock.source.AssetsHostsDataSource
import com.appsnado.haippNew.adblock.source.HostsDataSource
import com.appsnado.haippNew.adblock.source.HostsDataSourceProvider
import com.appsnado.haippNew.adblock.source.PreferencesHostsDataSourceProvider
import com.appsnado.haippNew.browser.cleanup.DelegatingExitCleanup
import com.appsnado.haippNew.browser.cleanup.ExitCleanup
import com.appsnado.haippNew.database.adblock.HostsDatabase
import com.appsnado.haippNew.database.adblock.HostsRepository
import com.appsnado.haippNew.database.allowlist.AdBlockAllowListDatabase
import com.appsnado.haippNew.database.allowlist.AdBlockAllowListRepository
import com.appsnado.haippNew.database.bookmark.BookmarkDatabase
import com.appsnado.haippNew.database.bookmark.BookmarkRepository
import com.appsnado.haippNew.database.downloads.DownloadsDatabase
import com.appsnado.haippNew.database.downloads.DownloadsRepository
import com.appsnado.haippNew.database.history.HistoryDatabase
import com.appsnado.haippNew.database.history.HistoryRepository
import com.appsnado.haippNew.ssl.SessionSslWarningPreferences
import com.appsnado.haippNew.ssl.SslWarningPreferences
import dagger.Binds
import dagger.Module

/**
 * Dependency injection module used to bind implementations to interfaces.
 */
@Module
interface AppBindsModule {

    @Binds
    fun bindsExitCleanup(delegatingExitCleanup: DelegatingExitCleanup): ExitCleanup

    @Binds
    fun bindsBookmarkModel(bookmarkDatabase: BookmarkDatabase): BookmarkRepository

    @Binds
    fun bindsDownloadsModel(downloadsDatabase: DownloadsDatabase): DownloadsRepository

    @Binds
    fun bindsHistoryModel(historyDatabase: HistoryDatabase): HistoryRepository

    @Binds
    fun bindsAdBlockAllowListModel(adBlockAllowListDatabase: AdBlockAllowListDatabase): AdBlockAllowListRepository

    @Binds
    fun bindsAllowListModel(sessionAllowListModel: SessionAllowListModel): AllowListModel

    @Binds
    fun bindsSslWarningPreferences(sessionSslWarningPreferences: SessionSslWarningPreferences): SslWarningPreferences

    @Binds
    fun bindsHostsDataSource(assetsHostsDataSource: AssetsHostsDataSource): HostsDataSource

    @Binds
    fun bindsHostsRepository(hostsDatabase: HostsDatabase): HostsRepository

    @Binds
    fun bindsHostsDataSourceProvider(preferencesHostsDataSourceProvider: PreferencesHostsDataSourceProvider): HostsDataSourceProvider
}
