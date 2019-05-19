package com.bertan.shinyscore.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.MutableLiveData
import com.bertan.shinyscore.R
import com.bertan.shinyscore.TestApplication
import com.bertan.shinyscore.presentation.model.UserView
import com.bertan.shinyscore.presentation.state.ViewState
import com.bertan.shinyscore.presentation.vm.UserViewModel
import com.squareup.picasso.Picasso
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class DashboardActivitySpec {
    object MockScoreFragment : ScoreFragment() {
        val scoreFragmentInnerId: Int = View.generateViewId()
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            View(container?.context).apply {
                id = scoreFragmentInnerId
            }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) = Unit
    }

    private val liveData: MutableLiveData<ViewState<UserView>> = MutableLiveData()
    private val userViewModel: UserViewModel = mockk {
        every { getState() } returns liveData
        every { onCreate() } returns Unit

        every { getUser(any()) } returns Unit
    }

    private val testModule: Module = module {
        viewModel { userViewModel }
    }

    private lateinit var activityController: ActivityController<DashboardActivity>

    @Before
    fun setup() {
        startKoin(listOf(testModule))
        mockkObject(ScoreFragment)
        every { ScoreFragment.newInstance(any()) } returns MockScoreFragment
    }

    @After
    fun tearDown() {
        stopKoin()
        unmockkObject(ScoreFragment)
    }

    private fun createActivity(): ActivityController<DashboardActivity> =
        Robolectric.buildActivity(DashboardActivity::class.java)
            .also { activityController = it }

    private fun assertGroupVisibility(
        loadingVisibility: Int,
        successVisibility: Int,
        errorVisibility: Int
    ) {
        val groupLoading = activityController.get().findViewById<Group>(R.id.groupLoading)
        val groupSuccess = activityController.get().findViewById<Group>(R.id.groupSuccess)
        val groupError = activityController.get().findViewById<Group>(R.id.groupError)

        assertEquals(loadingVisibility, groupLoading.visibility)
        assertEquals(successVisibility, groupSuccess.visibility)
        assertEquals(errorVisibility, groupError.visibility)
    }

    @Test
    fun `given activity when created it should hide all groups`() {
        createActivity().setup()

        assertGroupVisibility(
            loadingVisibility = GONE,
            successVisibility = GONE,
            errorVisibility = GONE
        )
    }

    @Test
    fun `given activity when started it should fetch the user`() {
        createActivity().setup()

        verify { userViewModel.getUser(any()) }
    }

    @Test
    fun `given loading when fetching the user it should display loading group and hide other groups`() {
        createActivity().setup()

        liveData.postValue(ViewState.Loading)

        assertGroupVisibility(
            loadingVisibility = VISIBLE,
            successVisibility = GONE,
            errorVisibility = GONE
        )
    }

    @Test
    fun `given success when fetching the user it should display success group populating the user fields and hide other groups`() {
        val picasso: Picasso = mockk(relaxed = true)
        Picasso.setSingletonInstance(picasso)

        createActivity().setup()

        liveData.postValue(ViewState.Success(UserView("dummyId", "dummyName", "http://dummy.avatar")))

        assertGroupVisibility(
            loadingVisibility = GONE,
            successVisibility = VISIBLE,
            errorVisibility = GONE
        )

        verify { picasso.load("http://dummy.avatar") }
        val userName = activityController.get().findViewById<TextView>(R.id.userName)
        assertEquals("dummyName", userName.text)

        verify { ScoreFragment.newInstance(any()) }
        val scoreFragmentInnerView = activityController.get().findViewById<View>(MockScoreFragment.scoreFragmentInnerId)
        assertNotNull(scoreFragmentInnerView)
    }

    @Test
    fun `given error when fetching the user it should display error group with the error message and hide other groups`() {
        createActivity().setup()

        liveData.postValue(ViewState.Error("dummyMessage", Exception("dummyException")))

        assertGroupVisibility(
            loadingVisibility = GONE,
            successVisibility = GONE,
            errorVisibility = VISIBLE
        )

        val errorView = activityController.get().findViewById<TextView>(R.id.errorView)
        assertEquals("dummyMessage", errorView.text)
    }
}