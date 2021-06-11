package app.editors.manager.ui.fragments.main

import android.os.Bundle
import android.view.View
import app.editors.manager.managers.providers.CloudFileProvider

class DocsCommonFragment : DocsCloudFragment() {

    companion object {
        val ID = CloudFileProvider.Section.Common.path

        fun newInstance(account: String): DocsCommonFragment {
            return DocsCommonFragment().apply {
                arguments = Bundle(1).apply {
                    putString(KEY_ACCOUNT, account)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onSwipeRefresh(): Boolean {
        if (!super.onSwipeRefresh()) {
            mCloudPresenter.getItemsById(ID)
            return true
        }
        return false
    }

    override fun onScrollPage() {
        super.onScrollPage()
        if (mCloudPresenter.stack == null) {
            mCloudPresenter.getItemsById(ID)
        }
    }

    override fun onStateEmptyBackStack() {
        super.onStateEmptyBackStack()
        if (mSwipeRefresh != null) {
            mSwipeRefresh.isRefreshing = true
        }
        mCloudPresenter.getItemsById(ID)
    }

    override fun onRemoveItemFromFavorites() {}

    private fun init() {
        mCloudPresenter.checkBackStack()
    }

}