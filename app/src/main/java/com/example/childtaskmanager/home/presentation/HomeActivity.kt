package com.example.childtaskmanager.home.presentation

import TasksListResponse
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.childtaskmanager.R
import com.example.childtaskmanager.core.utils.AlertManager
import com.example.childtaskmanager.databinding.ActivityHomeBinding
import com.example.childtaskmanager.home.HomeActivityViewModel
import com.example.childtaskmanager.home.HomeScreenViewEvents
import com.example.childtaskmanager.home.HomeScreenViewState
import com.example.childtaskmanager.home.domain.error.HomeScreenError
import com.example.childtaskmanager.home.presentation.adapter.TaskListAdapter
import com.google.gson.Gson
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hideKeyboard

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val taskItems: ArrayList<TasksListResponse> = ArrayList<TasksListResponse>()

    private lateinit var viewModel: HomeActivityViewModel

    private val hideHandler = Handler(Looper.getMainLooper())

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            binding.rlParent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            binding.rlParent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private lateinit var taskAdapter:TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]

        hide()

        taskAdapter = TaskListAdapter(this@HomeActivity)
        binding.rvCarousel.apply {
            this.adapter = taskAdapter
            set3DItem(false)
            setAlpha(false)
            setInfinite(true)
            setFlat(false)
            setIntervalRatio(0.8f)
            setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                override fun onItemSelected(position: Int) {
                    binding.tvTitle.text = taskItems.get(position).name

                }
            })

        }
        setupViewStateObserver()
        setupViewEventObserver()

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTasks()
    }
    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        hideHandler.post(hidePart2Runnable)
    }

    private fun setupViewStateObserver() {
        viewModel.viewState.observe(this as LifecycleOwner, Observer { viewState->

            if((viewState as HomeScreenViewState).isLoading){
                showLoadingState()
            }else{
                hideLoadingState()
            }

            viewState.tasks?.let {
                loadTasksData(it)
            }


        })
    }

    private fun setupViewEventObserver() {
        viewModel.viewEvents.observe(this as LifecycleOwner) { viewEvent ->
            when (viewEvent) {
                is HomeScreenViewEvents.HomeScreenRequestFailed -> {

                    when (viewEvent.error) {
                        HomeScreenError.NetworkError -> AlertManager.showBadNetworkConnectionAlert(this)
                        HomeScreenError.UnknownError -> AlertManager.showUnExpectedErrorAlert(this)
                    }
                }
                is HomeScreenViewEvents.HomeScreenRequestSuccess -> {
                    Log.e("response", "${Gson().toJson(viewEvent.response)}")
                    hideLoadingState()
                }
            }
        }
    }


    private fun loadTasksData(tasks: List<TasksListResponse>) {
        taskAdapter.setListItems(tasks)
        taskItems.clear()
        taskItems.addAll(tasks)
        binding.tvTitle.text = taskItems.get(0).name
    }


    private fun showLoadingState() {
        showFullScreenLoading(R.string.please_wait_dots)
        hideKeyboard(window.currentFocus)
    }

    private fun hideLoadingState() {
        hideFullScreenLoading()
    }


    private fun showFullScreenLoading(@StringRes progressText: Int? = null) {
        binding.progressLayout.apply {
            progressTextView.text = if (progressText == null) null else getString(progressText)

            root.animate().setDuration(
                resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()
            ).alpha(1f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    root.isVisible = true
                }
            })
        }
    }

    private fun hideFullScreenLoading() {
        binding.progressLayout.apply {
            root.animate().setDuration(
                resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()
            ).alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        root.isVisible = false
                    }
                })
        }
    }



}