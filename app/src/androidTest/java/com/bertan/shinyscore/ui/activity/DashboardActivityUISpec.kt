package com.bertan.shinyscore.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.bertan.shinyscore.R
import com.bertan.shinyscore.presentation.model.UserView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.vm.UserViewModel
import com.bertan.shinyscore.ui.dashboard.DashboardActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

class DashboardActivityUISpec {
    private val liveData: MutableLiveData<ViewState<UserView>> = MutableLiveData()
    private val userViewModel: UserViewModel = spyk(UserViewModel(mockk(relaxed = true), mockk(relaxed = true))) {
        every { getState() } returns liveData
        every { onCreate() } returns Unit

        every { getUser(any()) } returns Unit
    }

    private val testModule: Module = module(override = true) {
        viewModel { userViewModel }
    }

    @get:Rule
    val activityRule: ActivityTestRule<DashboardActivity> = ActivityTestRule(DashboardActivity::class.java, true, false)

    @Before
    fun setup() {
        loadKoinModules(listOf(testModule))
    }

    private fun launchActivity() = activityRule.launchActivity(null)

    private fun assertGroupVisibility(
        loadingVisibility: Visibility,
        successVisibility: Visibility,
        errorVisibility: Visibility
    ) {
        onView(withId(R.id.groupLoading))
            .check(matches(withEffectiveVisibility(loadingVisibility)))
        onView(withId(R.id.groupSuccess))
            .check(matches(withEffectiveVisibility(successVisibility)))
        onView(withId(R.id.groupError))
            .check(matches(withEffectiveVisibility(errorVisibility)))
    }

    @Test
    fun given_activity_when_created_it_should_hide_all_groups() {
        launchActivity()

        assertGroupVisibility(
            loadingVisibility = Visibility.GONE,
            successVisibility = Visibility.GONE,
            errorVisibility = Visibility.GONE
        )
    }

    @Test
    fun given_loading_when_fetching_the_user_it_should_display_loading_group_and_hide_other_groups() {
        launchActivity()

        liveData.postValue(ViewState.Loading)

        assertGroupVisibility(
            loadingVisibility = Visibility.VISIBLE,
            successVisibility = Visibility.GONE,
            errorVisibility = Visibility.GONE
        )
    }

    @Test
    fun given_success_when_fetching_the_user_it_should_display_success_group_populating_the_user_fields_and_hide_other_groups() {
        launchActivity()

        liveData.postValue(ViewState.Success(UserView("dummyId", "dummyName", "http://dummy.avatar")))

        assertGroupVisibility(
            loadingVisibility = Visibility.GONE,
            successVisibility = Visibility.VISIBLE,
            errorVisibility = Visibility.GONE
        )
        onView(withId(R.id.userName))
            .check(matches(withText("dummyName")))
    }

    @Test
    fun given_error_when_fetching_the_user_it_should_display_error_group_with_the_error_message_and_hide_other_groups() {
        launchActivity()

        liveData.postValue(ViewState.Error("dummyMessage", Exception("dummyException")))

        assertGroupVisibility(
            loadingVisibility = Visibility.GONE,
            successVisibility = Visibility.GONE,
            errorVisibility = Visibility.VISIBLE
        )
        onView(withId(R.id.errorView))
            .check(matches(withText("dummyMessage")))
    }
}