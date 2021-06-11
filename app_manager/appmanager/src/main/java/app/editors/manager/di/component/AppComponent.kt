package app.editors.manager.di.component

import android.content.Context
import app.documents.core.account.AccountDao
import app.documents.core.di.module.AccountModule
import app.documents.core.di.module.RecentModule
import app.documents.core.di.module.SettingsModule
import app.documents.core.settings.NetworkSettings
import app.documents.core.settings.WebDavInterceptor
import app.editors.manager.app.MigrateDb
import app.editors.manager.di.module.AppModule
import app.editors.manager.di.module.ToolModule
import app.editors.manager.managers.providers.AccountProvider
import app.editors.manager.managers.tools.CacheTool
import app.editors.manager.managers.tools.CountriesCodesTool
import app.editors.manager.managers.tools.PreferenceTool
import app.editors.manager.mvp.models.states.OperationsState
import app.editors.manager.mvp.presenters.login.*
import app.editors.manager.mvp.presenters.main.*
import app.editors.manager.mvp.presenters.share.AddPresenter
import app.editors.manager.mvp.presenters.share.SettingsPresenter
import app.editors.manager.mvp.presenters.storage.ConnectPresenter
import app.editors.manager.mvp.presenters.storage.SelectPresenter
import app.editors.manager.ui.activities.login.PortalsActivity
import app.editors.manager.ui.activities.main.OperationActivity
import app.editors.manager.ui.adapters.ExplorerAdapter
import app.editors.manager.ui.adapters.MediaAdapter
import app.editors.manager.ui.adapters.ShareAddAdapter
import app.editors.manager.ui.dialogs.AccountBottomDialog
import app.editors.manager.ui.fragments.login.*
import app.editors.manager.ui.fragments.main.DocsBaseFragment
import app.editors.manager.ui.fragments.main.WebViewerFragment
import app.editors.manager.ui.fragments.media.MediaImageFragment
import app.editors.manager.ui.fragments.media.MediaVideoFragment
import app.editors.manager.ui.fragments.onboarding.OnBoardingPagerFragment
import app.editors.manager.ui.fragments.operations.DocsOperationSectionFragment
import app.editors.manager.ui.fragments.storage.ConnectFragment
import app.editors.manager.ui.fragments.storage.SelectFragment
import app.editors.manager.ui.fragments.storage.WebDavFragment
import app.editors.manager.ui.fragments.storage.WebTokenFragment
import dagger.Component
import lib.toolkit.base.managers.tools.GlideTool
import lib.toolkit.base.managers.tools.LocalContentTools
import javax.inject.Singleton

@Component(modules = [AppModule::class, ToolModule::class, SettingsModule::class, AccountModule::class, RecentModule::class])
@Singleton
interface AppComponent {
    /*
    * TODO scopes!
    * */
    val context: Context
    val preference: PreferenceTool
    val countriesCodes: CountriesCodesTool
    val cacheTool: CacheTool
    val sectionsState: OperationsState
    val contentTools: LocalContentTools
    val glideTools: GlideTool
    val networkSettings: NetworkSettings
    val accountsDao: AccountDao

    /*
   * Login
   * */
    fun inject(enterprisePortalPresenter: EnterprisePortalPresenter?)
    fun inject(enterpriseSignInPresenter: EnterpriseLoginPresenter?)
    fun inject(enterpriseSmsPresenter: EnterpriseSmsPresenter?)
    fun inject(enterprisePhonePresenter: EnterprisePhonePresenter?)
    fun inject(enterpriseCreateValidatePresenter: EnterpriseCreateValidatePresenter?)
    fun inject(enterpriseCreateSignInPresenter: EnterpriseCreateLoginPresenter?)
    fun inject(personalSignInPresenter: PersonalLoginPresenter?)
    fun inject(personalSignUpPresenter: PersonalSignUpPresenter?)
    fun inject(enterpriseSSOPresenter: EnterpriseSSOPresenter?)
    fun inject(migrateDb: MigrateDb?)
    fun inject(codesFragment: CountriesCodesFragment?)
    fun inject(phoneFragment: EnterprisePhoneFragment?)
    fun inject(portalFragment: EnterprisePortalFragment?)
    fun inject(signInFragment: EnterpriseSignInFragment?)
    fun inject(enterpriseSmsFragment: EnterpriseSmsFragment?)
    fun inject(personalPortalFragment: PersonalPortalFragment?)
    fun inject(webDavInterceptor: WebDavInterceptor?)
    fun inject(passwordRecoveryPresenter: PasswordRecoveryPresenter)

    /*
    * Main
    * */
    fun inject(accountsPresenter: AccountsPresenter?)
    fun inject(mainActivityPresenter: MainActivityPresenter?)
    fun inject(onlyOfficePresenter: DocsCloudPresenter?)
    fun inject(webDavPresenter: DocsWebDavPresenter?)
    fun inject(docsOnDevicePresenter: DocsOnDevicePresenter?)
    fun inject(operationActivity: OperationActivity?)
    fun inject(webViewerFragment: WebViewerFragment?)
    fun inject(docsBaseFragment: DocsBaseFragment?)
    fun inject(docsOperationSectionFragment: DocsOperationSectionFragment?)
    fun inject(explorerAdapter: ExplorerAdapter?)
    fun inject(mediaAdapter: MediaAdapter?)
    fun inject(settingsPresenter: AppSettingsPresenter?)
    fun inject(accountsPresenter: CloudAccountPresenter?)
    fun inject(mainPagerPresenter: MainPagerPresenter?)

    /*
    * Media
    * */
    fun inject(mediaVideoFragment: MediaVideoFragment?)
    fun inject(mediaImageFragment: MediaImageFragment?)

    /*
    * Share
    * */
    fun inject(settingsPresenter: SettingsPresenter?)
    fun inject(addPresenter: AddPresenter?)
    fun inject(shareAddAdapter: ShareAddAdapter?)

    /*
    * Storage
    * */
    fun inject(selectFragment: SelectFragment?)
    fun inject(webTokenFragment: WebTokenFragment?)
    fun inject(connectFragment: ConnectFragment?)
    fun inject(settingsFragment: ConnectPresenter?)
    fun inject(webDavFragment: WebDavFragment?)

    /*
    * On boarding
    * */
    fun inject(onBoardingPagerFragment: OnBoardingPagerFragment?)
    fun inject(portalsActivity: PortalsActivity?)

    /*
    * Content provider
    * */
    fun inject(accountProvider: AccountProvider?)
    fun inject(settingsPresenter: ProfilePresenter?)
    fun inject(docsRecentPresenter: DocsRecentPresenter?)
    fun inject(authPagerFragment: AuthPagerFragment?)
    fun inject(enterpriseAppAuthPresenter: EnterpriseAppAuthPresenter?)
    fun inject(selectPresenter: SelectPresenter?)
    fun inject(accountsBottomFragment: AccountBottomDialog?)
    fun inject(webDavSignInPresenter: WebDavSignInPresenter?)
}