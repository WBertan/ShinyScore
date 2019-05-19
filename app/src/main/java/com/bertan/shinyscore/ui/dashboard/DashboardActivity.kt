package com.bertan.shinyscore.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bertan.shinyscore.R
import com.bertan.shinyscore.extension.loadUrl
import com.bertan.shinyscore.presentation.model.UserView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.vm.UserViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {
    companion object {
        //TODO(We need to recover the real userId after the Login/Register)
        private const val USER_ID = "someUserId"
    }

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        lifecycle.addObserver(userViewModel)
        userViewModel.getState().observe(this, Observer { it?.let(::handleUserState) })
    }

    override fun onStart() {
        super.onStart()
        userViewModel.getUser(USER_ID)
    }

    private fun handleUserState(viewState: ViewState<UserView>) =
        when (viewState) {
            is ViewState.Loading -> {
                groupLoading.visibility = View.VISIBLE
                groupSuccess.visibility = View.GONE
                groupError.visibility = View.GONE
            }
            is ViewState.Success -> {
                groupLoading.visibility = View.GONE
                groupSuccess.visibility = View.VISIBLE
                groupError.visibility = View.GONE

                populateUserView(viewState.data)
            }
            is ViewState.Error -> {
                groupLoading.visibility = View.GONE
                groupSuccess.visibility = View.GONE
                groupError.visibility = View.VISIBLE

                populateErrorView(viewState.message)
            }
        }

    private fun populateUserView(userView: UserView) {
        userName.text = userView.name
        userAvatar.loadUrl(userView.avatar)

        supportFragmentManager
            .takeIf { it.findFragmentByTag("user_score") == null }
            ?.beginTransaction()
            ?.add(R.id.frameUserScore, ScoreFragment.newInstance(USER_ID), "user_score")
            ?.commit()
    }

    private fun populateErrorView(errorMessage: String) {
        errorView.text = errorMessage
    }
}