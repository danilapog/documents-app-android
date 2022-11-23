package app.editors.manager.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Process
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.InvalidationTracker
import app.documents.core.network.login.ILoginServiceProvider
import app.documents.core.network.manager.ManagerService
import app.documents.core.network.room.RoomService
import app.documents.core.network.share.ShareService
import app.documents.core.network.webdav.WebDavService
import app.documents.core.providers.CloudFileProvider
import app.documents.core.providers.LocalFileProvider
import app.documents.core.providers.RoomProvider
import app.documents.core.providers.WebDavFileProvider
import app.documents.core.storage.account.CloudAccount
import app.editors.manager.BuildConfig
import app.editors.manager.di.component.AppComponent
import app.editors.manager.di.component.CoreComponent
import app.editors.manager.di.component.DaggerAppComponent
import app.editors.manager.di.component.DaggerCoreComponent
import app.editors.manager.managers.utils.KeyStoreUtils
import app.editors.manager.storages.dropbox.di.component.DaggerDropboxComponent
import app.editors.manager.storages.dropbox.dropbox.api.IDropboxServiceProvider
import app.editors.manager.storages.dropbox.dropbox.login.IDropboxLoginServiceProvider
import app.editors.manager.storages.googledrive.di.component.DaggerGoogleDriveComponent
import app.editors.manager.storages.googledrive.googledrive.api.IGoogleDriveServiceProvider
import app.editors.manager.storages.googledrive.googledrive.login.IGoogleDriveLoginServiceProvider
import app.editors.manager.storages.onedrive.di.component.DaggerOneDriveComponent
import app.editors.manager.storages.onedrive.onedrive.api.IOneDriveServiceProvider
import app.editors.manager.storages.onedrive.onedrive.login.IOneDriveLoginServiceProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import lib.toolkit.base.managers.tools.ThemePreferencesTools
import lib.toolkit.base.managers.utils.ActivitiesUtils
import java.util.*

class App : Application() {

    companion object {

        val TAG: String = App::class.java.simpleName

        private lateinit var sApp: App
        private var currentDesktopMode = false
        private var isDesktop = false

        @JvmStatic
        fun getApp(): App {
            return sApp
        }

        @JvmStatic
        fun getLocale(): String {
            return Locale.getDefault().language
        }

    }

    var isAnalyticEnable = true
        set(value) {
            field = value
            initCrashlytics()
        }

    var isKeyStore: Boolean = true

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = checkNotNull(_appComponent) {
            "App component can't be null"
        }

    val coreComponent: CoreComponent by lazy  {
        DaggerCoreComponent.builder()
            .appComponent(appComponent)
            .build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        sApp = this
        initDagger()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        currentDesktopMode = checkDeXEnabled()
        if (isDesktop != currentDesktopMode) {
            isDesktop = currentDesktopMode
        }
    }

    fun checkDeXEnabled(): Boolean {
        val enabled: Boolean
        val config: Configuration = resources.configuration
        try {
            val configClass: Class<*> = config.javaClass
            enabled = (configClass.getField("SEM_DESKTOP_MODE_ENABLED").getInt(configClass)
                    == configClass.getField("semDesktopModeEnabled").getInt(config))
            return enabled
        } catch (ignored: NoSuchFieldException) {
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: IllegalArgumentException) {
        }
        return false
    }

    override fun onCreate() {
        super.onCreate()
        ThemePreferencesTools(this).also { pref ->
            AppCompatDelegate.setDefaultNightMode(pref.mode)
        }
        init()
    }

    private fun init() {
        /*
         Only android >= pie.
         https://bugs.chromium.org/p/chromium/issues/detail?id=558377
         https://stackoverflow.com/questions/51843546/android-pie-9-0-webview-in-multi-process

         For Android Pie is a separate directory for the process with WebView
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (getProcess() == "com.onlyoffice.documents:WebViewerActivity") {
                WebView.setDataDirectorySuffix("cacheWebView")
            }
        }
        if (ActivitiesUtils.isPackageExist(this, "com.onlyoffice.projects")) {
            AddAccountHelper(this).copyData()
        }
        isAnalyticEnable = appComponent.preference.isAnalyticEnable
        initCrashlytics()
        KeyStoreUtils.init()
        addDataBaseObserver()
    }

    private fun addDataBaseObserver() {
        appComponent.accountsDataBase.invalidationTracker.addObserver(object :
            InvalidationTracker.Observer(arrayOf(CloudAccount::class.java.simpleName)) {
            override fun onInvalidated(tables: MutableSet<String>) {
                appComponent.preference.dbTimestamp = System.currentTimeMillis()
            }
        })
    }

    private fun getProcess(): String {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (info in manager.runningAppProcesses) {
            if (info.pid == Process.myPid()) {
                return info.processName
            }
        }
        return ""
    }

    private fun initDagger() {
        _appComponent = DaggerAppComponent.builder()
            .context(context = this)
            .build()
    }

    private fun initCrashlytics() {
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG || !isAnalyticEnable) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }
    }

    fun getOneDriveComponent(): IOneDriveServiceProvider {
        return DaggerOneDriveComponent.builder().appComponent(appComponent)
            .build()
            .oneDriveServiceProvider
    }

    fun getDropboxComponent(): IDropboxServiceProvider {
        return DaggerDropboxComponent.builder().appComponent(appComponent)
            .build()
            .dropboxServiceProvider
    }

    fun getGoogleDriveComponent(): IGoogleDriveServiceProvider {
        return DaggerGoogleDriveComponent.builder().appComponent(appComponent)
            .build()
            .googleDriveServiceProvider
    }

}

val Context.accountOnline: CloudAccount?
    get() = when (this) {
        is App -> this.appComponent.accountOnline
        else -> this.applicationContext.appComponent.accountOnline
    }

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> this.appComponent
        else -> this.applicationContext.appComponent
    }

val Context.coreComponent: CoreComponent
    get() = when (this) {
        is App -> this.coreComponent
        else -> this.applicationContext.coreComponent
    }

val Context.loginService: ILoginServiceProvider
    get() = when (this) {
        is App -> this.coreComponent.loginService
        else -> this.applicationContext.loginService
    }

val Context.oneDriveLoginService: IOneDriveLoginServiceProvider
    get() = when (this) {
        is App -> this.coreComponent.oneDriveLoginService
        else -> applicationContext.coreComponent.oneDriveLoginService
    }

val Context.dropboxLoginService: IDropboxLoginServiceProvider
    get() = when (this) {
        is App -> this.coreComponent.dropboxLoginService
        else -> applicationContext.coreComponent.dropboxLoginService
    }

val Context.googleDriveLoginService: IGoogleDriveLoginServiceProvider
    get() = when(this) {
        is App -> this.coreComponent.googleDriveLoginService
        else -> applicationContext.coreComponent.googleDriveLoginService
    }

val Context.api: ManagerService
    get() = when (this) {
        is App -> coreComponent.managerService
        else -> applicationContext.api
    }

val Context.roomApi: RoomService
    get() = when (this) {
        is App -> coreComponent.roomService
        else -> applicationContext.roomApi
    }

val Context.webDavApi: WebDavService
    get() = when (this) {
        is App -> coreComponent.webDavService
        else -> applicationContext.webDavApi
    }

val Context.shareApi: ShareService
    get() = when (this) {
        is App -> coreComponent.shareService
        else -> applicationContext.shareApi
    }

val Context.cloudFileProvider: CloudFileProvider
    get() = when (this) {
        is App -> coreComponent.cloudFileProvider
        else -> applicationContext.cloudFileProvider
    }

val Context.localFileProvider: LocalFileProvider
    get() = when (this) {
        is App -> coreComponent.localFileProvider
        else -> applicationContext.localFileProvider
    }

val Context.webDavFileProvider: WebDavFileProvider
    get() = when (this) {
        is App -> coreComponent.webDavFileProvider
        else -> applicationContext.webDavFileProvider
    }

val Context.roomProvider: RoomProvider
    get() = when (this) {
        is App -> coreComponent.roomProvider
        else -> applicationContext.roomProvider
    }

fun Context.getOneDriveServiceProvider(): IOneDriveServiceProvider {
    return when (this) {
        is App -> this.getOneDriveComponent()
        else -> this.applicationContext.getOneDriveServiceProvider()
    }
}

fun Context.getDropboxServiceProvider(): IDropboxServiceProvider {
    return when(this) {
        is App -> this.getDropboxComponent()
        else -> this.applicationContext.getDropboxServiceProvider()
    }
}
fun Context.getGoogleDriveServiceProvider(): IGoogleDriveServiceProvider {
    return when(this) {
        is App -> this.getGoogleDriveComponent()
        else -> this.applicationContext.getGoogleDriveServiceProvider()
    }
}