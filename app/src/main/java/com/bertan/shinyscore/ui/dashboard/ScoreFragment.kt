package com.bertan.shinyscore.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bertan.shinyscore.R
import com.bertan.shinyscore.presentation.model.ReportView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.vm.ReportViewModel
import kotlinx.android.synthetic.main.score_component.*
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
                groupFragmentLoading.visibility = View.VISIBLE
                groupFragmentSuccess.visibility = View.GONE
                groupFragmentError.visibility = View.GONE
            }
            is ViewState.Success -> {
                groupFragmentLoading.visibility = View.GONE
                groupFragmentSuccess.visibility = View.VISIBLE
                groupFragmentError.visibility = View.GONE

                when (val reportView = viewState.data) {
                    null -> populateEmptyReportView()
                    else -> populateReportView(reportView)
                }
            }
            is ViewState.Error -> {
                groupFragmentLoading.visibility = View.GONE
                groupFragmentSuccess.visibility = View.GONE
                groupFragmentError.visibility = View.VISIBLE

                populateErrorView(viewState.message)
            }
        }

    private fun populateEmptyReportView() {
        currentScore.text = getString(R.string.score_fragment_empty_current_score)
        scoreProgress.progress = 0
        scoreBottomText.text = getString(R.string.score_fragment_empty_current_score_description)
    }

    private fun populateReportView(reportView: ReportView) {
        val (current, max, min) =
            Triple(
                reportView.currentScore.toInt(),
                reportView.maxScore.toInt(),
                reportView.minScore.toInt()
            )
        val scoreProgress = ((current - min) * 100) / (max - min)

        currentScore.apply {
            text = reportView.currentScore.toString()
            setTextColor(getScoreTextColor(scoreProgress))
        }
        scoreBottomText.text = getString(R.string.score_component_bottom_text, reportView.maxScore)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setScore(current, max, min)
        } else {
            setScoreLegacy(scoreProgress)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setScore(current: Int, max: Int, min: Int) {
        scoreProgress.max = max
        scoreProgress.min = min
        scoreProgress.progress = current
    }

    private fun setScoreLegacy(progress: Int) {
        scoreProgress.progress = progress
    }

    private fun getScoreTextColor(progressValue: Int): Int {
        val progressPercentage: Float = progressValue / 100f

        val (colorStart, colorMiddle, colorEnd) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getScoreComponentColors()
            } else {
                getScoreComponentColorsLegacy()
            }

        return if (progressPercentage < 0.5) {
            val factor = progressPercentage / 0.5f
            ColorUtils.blendARGB(colorStart, colorMiddle, factor)
        } else {
            val factor = (progressPercentage - 0.5f) / 0.5f
            ColorUtils.blendARGB(colorMiddle, colorEnd, factor)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getScoreComponentColors(): Triple<Int, Int, Int> =
        Triple(
            resources.getColorStateList(R.color.score_component_start_gradient, activity?.theme).defaultColor,
            resources.getColorStateList(R.color.score_component_middle_gradient, activity?.theme).defaultColor,
            resources.getColorStateList(R.color.score_component_end_gradient, activity?.theme).defaultColor
        )

    private fun getScoreComponentColorsLegacy(): Triple<Int, Int, Int> =
        Triple(
            resources.getColorStateList(R.color.score_component_start_gradient).defaultColor,
            resources.getColorStateList(R.color.score_component_middle_gradient).defaultColor,
            resources.getColorStateList(R.color.score_component_end_gradient).defaultColor
        )

    private fun populateErrorView(errorMessage: String) {
        errorView.text = errorMessage
    }
}