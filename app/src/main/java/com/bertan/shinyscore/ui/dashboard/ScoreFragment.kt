package com.bertan.shinyscore.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bertan.shinyscore.R
import com.bertan.shinyscore.presentation.model.ReportView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.vm.ReportViewModel
import kotlinx.android.synthetic.main.score_fragment.*
import org.koin.android.ext.android.setProperty
import org.koin.androidx.viewmodel.ext.android.viewModel

open class ScoreFragment : Fragment() {
    companion object {
        private const val USER_ID_KEY = "userId"

        fun newInstance(userId: String): ScoreFragment =
            ScoreFragment()
                .apply {
                    arguments = Bundle().apply { putString(USER_ID_KEY, userId) }
                }

        internal fun Bundle.getUserId(): String? = this.getString(USER_ID_KEY)
    }

    private val reportViewModel: ReportViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.score_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val userId = requireNotNull(arguments?.getUserId())

            setProperty("userId", userId)

            lifecycle.addObserver(reportViewModel)
            reportViewModel.getState().observe(this, Observer { it?.let(::handleReportState) })
        } catch (_: IllegalArgumentException) {
            populateErrorView("Failed to retrieve report!")
        }
    }

    private fun handleReportState(viewState: ViewState<ReportView?>) =
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

                when (val reportView = viewState.data) {
                    null -> populateEmptyReportView()
                    else -> populateReportView(reportView)
                }
            }
            is ViewState.Error -> {
                groupLoading.visibility = View.GONE
                groupSuccess.visibility = View.GONE
                groupError.visibility = View.VISIBLE

                populateErrorView(viewState.message)
            }
        }

    private fun populateEmptyReportView() {
        currentScore.text = "-"
    }

    private fun populateReportView(reportView: ReportView) {
        currentScore.text = reportView.currentScore.toString()
    }

    private fun populateErrorView(errorMessage: String) {
        errorView.text = errorMessage
    }
}