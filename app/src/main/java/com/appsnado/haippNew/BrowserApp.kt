package com.appsnado.haippNew


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import com.appsnado.haippNew.Activities.GestureServicesact
import com.appsnado.haippNew.Applocakpacakges.LockApplication
import com.appsnado.haippNew.Monitorapp.AppService
import com.appsnado.haippNew.Monitorapp.data.AppItem
import com.appsnado.haippNew.Monitorapp.data.DataManager
import com.appsnado.haippNew.Monitorapp.db.DbHistoryExecutor
import com.appsnado.haippNew.Monitorapp.db.DbIgnoreExecutor
import com.appsnado.haippNew.Monitorapp.util.PreferenceManager
import com.appsnado.haippNew.baseactivity.BaseActivity

import com.appsnado.haippNew.database.bookmark.BookmarkExporter
import com.appsnado.haippNew.database.bookmark.BookmarkRepository
import com.appsnado.haippNew.device.BuildInfo
import com.appsnado.haippNew.device.BuildType
import com.appsnado.haippNew.di.AppComponent
import com.appsnado.haippNew.di.DaggerAppComponent
import com.appsnado.haippNew.di.DatabaseScheduler
import com.appsnado.haippNew.di.injector
import com.appsnado.haippNew.log.Logger
import com.appsnado.haippNew.preference.DeveloperPreferences
import com.appsnado.haippNew.utils.FileUtils
import com.appsnado.haippNew.utils.MemoryLeakUtils
import com.appsnado.haippNew.utils.SpUtil
import com.appsnado.haippNew.utils.installMultiDex
import com.squareup.leakcanary.LeakCanary
import io.github.subhamtyagi.crashreporter.CrashReporter
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import org.litepal.LitePalApplication
import java.util.*
import javax.inject.Inject
import kotlin.system.exitProcess


class BrowserApp : LitePalApplication() {

    @Inject internal lateinit var developerPreferences: DeveloperPreferences
    @Inject internal lateinit var bookmarkModel: BookmarkRepository
    @Inject @field:DatabaseScheduler internal lateinit var databaseScheduler: Scheduler
    @Inject internal lateinit var logger: Logger
    @Inject internal lateinit var buildInfo: BuildInfo



    lateinit var applicationComponent: AppComponent

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT < 21) {
            installMultiDex(context = base)
        }
    }


    override fun onCreate() {
        super.onCreate()

        application = this


        //Crash reporter utility
        //  CrashReporter.initialize(this, getCacheDir().getPath());
        com.appsnado.haippNew.Applocakpacakges.utils.SpUtil.getInstance()
            .init(application)
        activityList = ArrayList()


        PreferenceManager.init(this)
        applicationContext.startService(
            Intent(
                applicationContext,
                AppService::class.java
            )
        )
        DbIgnoreExecutor.init(applicationContext)
        DbHistoryExecutor.init(applicationContext)
        DataManager.init()
        addDefaultIgnoreAppsToDB()
        //Crash reporter utility

        //Crash reporter utility
        CrashReporter.initialize(this, cacheDir.path)

        SpUtil.getInstance().init(application)

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())
        }

        if (Build.VERSION.SDK_INT >= 28) {
            if (getProcessName() == "$packageName:incognito") {
                WebView.setDataDirectorySuffix("incognito")
            }
        }

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            if (BuildConfig.DEBUG) {
                FileUtils.writeCrashToStorage(ex)
            }

            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, ex)
            } else {
                exitProcess(2)
            }
        }

        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            if (BuildConfig.DEBUG && throwable != null) {
                FileUtils.writeCrashToStorage(throwable)
                throw throwable
            }
        }

        applicationComponent = DaggerAppComponent.builder()
            .application(this)
            .buildInfo(createBuildInfo())
            .build()
        injector.inject(this)

        Single.fromCallable(bookmarkModel::count)
            .filter { it == 0L }
            .flatMapCompletable {
                val assetsBookmarks = BookmarkExporter.importBookmarksFromAssets(this@BrowserApp)
                bookmarkModel.addBookmarkList(assetsBookmarks)
            }
            .subscribeOn(databaseScheduler)
            .subscribe()

        if (developerPreferences.useLeakCanary && buildInfo.buildType == BuildType.DEBUG) {
            LeakCanary.install(this)
        }
        if (buildInfo.buildType == BuildType.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        registerActivityLifecycleCallbacks(object : MemoryLeakUtils.LifecycleAdapter() {
            override fun onActivityDestroyed(activity: Activity) {
                logger.log(TAG, "Cleaning up after the Android framework")
                MemoryLeakUtils.clearNextServedView(activity, this@BrowserApp)
            }
        })
    }


    private fun addDefaultIgnoreAppsToDB() {
        Thread {
            val mDefaults: MutableList<String> =
                ArrayList()
            mDefaults.add("com.android.settings")
            mDefaults.add(BuildConfig.APPLICATION_ID)
            for (packageName in mDefaults) {
                val item = AppItem()
                item.mPackageName = packageName
                item.mEventTime = System.currentTimeMillis()
                DbIgnoreExecutor.getInstance().insertItem(item)
            }
        }.run()
    }

    fun doForCreate(activity: BaseActivity?) {
        activityList!!.add(activity!!)
    }

    fun doForFinish(activity: BaseActivity?) {
        activityList!!.remove(activity)
    }

    fun clearAllActivity() {
        try {
            for (activity in activityList!!) {
                if (activity != null && !clearAllWhiteList(activity)) activity.clear()
            }
            activityList!!.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearAllWhiteList(activity: BaseActivity): Boolean {
        return activity is GestureServicesact
    }





    /**
     * Create the [BuildType] from the [BuildConfig].
     */
    private fun createBuildInfo() = BuildInfo(when {
        BuildConfig.DEBUG -> BuildType.DEBUG
        else -> BuildType.RELEASE
    })

    companion object {

        @JvmField
        var application: BrowserApp? = null
        private const val TAG = "BrowserApp"
        private var activityList: MutableList<BaseActivity>? = null
        init {
            activityList = ArrayList()
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
        }

        fun getInstance(): BrowserApp? {
            return application
        }
    }


    ////








}
