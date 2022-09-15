package com.example.childtaskmanager.home.presentation

import Schedule
import TasksListResponse
import android.R.color
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import calculateTheDifferenceInTime
import com.example.childtaskmanager.R
import com.example.childtaskmanager.core.utils.AlertManager
import com.example.childtaskmanager.core.utils.Constants
import com.example.childtaskmanager.databinding.ActivityTaskDetailsBinding
import com.example.childtaskmanager.home.HomeActivityViewModel
import com.example.childtaskmanager.home.HomeScreenViewEvents
import com.example.childtaskmanager.home.HomeScreenViewState
import com.example.childtaskmanager.home.domain.error.HomeScreenError
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import getValueForProgressFromTimeDifference
import hideKeyboard
import setProgressColor


@AndroidEntryPoint
class TaskDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var task: TasksListResponse

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]

        intent.extras?.let {
            if(it.containsKey("taskId")){
                viewModel.loadTaskByIDFromDb(it.getString("taskId")!!)
            }
        }

        hide()

        binding.ivBack.setOnClickListener { onBackPressed() }


        setupViewStateObserver()
        setupViewEventObserver()
    }

    private fun setupViewStateObserver() {
        viewModel.viewState.observe(this as LifecycleOwner, Observer { viewState->

            if((viewState as HomeScreenViewState).isLoading){
                showLoadingState()
            }else{
                hideLoadingState()
            }

            viewState.task?.let {
                task=it
                loadTaskData(task)
            }

            viewState.day?.let {
                loadSchedule(task,it)
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

    private fun loadTaskData(task: TasksListResponse) {
        with(binding){
            tvTitle.text = task.name
            task.visualAidUrl?.let {
                Picasso.get().load(it).error(R.drawable.place_holder_image_square).placeholder(R.drawable.place_holder_image_square).into(sivBanner)
            } ?: {
                Picasso.get().load(R.drawable.place_holder_image_square).into(sivBanner)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.loadTodayDayTime()
        },2000)

    }

    private fun loadSchedule(task: TasksListResponse,today:Constants.DayOfTheWeek){
        with(binding){
            tvStartTime.setText(getTimeIfNull(task.schedule!!, today))
            val timeOfDay=getValidTimeFromOtherDayString(getTimeIfNull(task.schedule!!, today))
            ctvbTimer.setText(calculateTheDifferenceInTime(dcTime.text.toString(),timeOfDay))

            val progress=getValueForProgressFromTimeDifference(dcTime.text.toString(),timeOfDay)

            if(progress<=10){
                pbRemainingTime.max=10
            }else{
                pbRemainingTime.max=100
            }

            pbRemainingTime.setProgressColor(this@TaskDetailsActivity,progress)
            pbRemainingTime.progress=progress
        }
    }

    private fun getValidTimeFromOtherDayString(time:String): String {
        return if (!time.equals("N/A",true)){
            val timeArr = time.split(":")
            if(timeArr.size>=3){
                "${timeArr[1]}:${timeArr[2]}"
            }else{
                time
            }
        }else{
            ""
        }

    }
    private fun getTimeIfNull(schedule: Schedule,day:Constants.DayOfTheWeek):String{
        return when(day){
            Constants.DayOfTheWeek.Mon->{
                if(schedule.mon.isNullOrEmpty()){
                        if(!schedule.tue.isNullOrEmpty()){
                            "Tue:"+schedule.tue
                        }else if(!schedule.wed.isNullOrEmpty()){
                            "Wed:"+schedule.wed
                        }else if(!schedule.thu.isNullOrEmpty()){
                            "Thu:"+schedule.thu
                        }else if(!schedule.fri.isNullOrEmpty()){
                            "Fri:"+schedule.fri
                        }else if(!schedule.sat.isNullOrEmpty()){
                            "Sat:"+schedule.sat
                        }else if(!schedule.sun.isNullOrEmpty()){
                            "Sun:"+schedule.sun
                        }else {
                            "N/A"
                        }
                }else{
                    schedule.mon
                }
            }
            Constants.DayOfTheWeek.Tue->{
                if(schedule.tue.isNullOrEmpty()){
                    if(!schedule.mon.isNullOrEmpty()){
                        "Mon:"+schedule.mon
                    }else if(!schedule.wed.isNullOrEmpty()){
                        "Wed:"+schedule.wed
                    }else if(!schedule.thu.isNullOrEmpty()){
                        "Thu:"+schedule.thu
                    }else if(!schedule.fri.isNullOrEmpty()){
                        "Fri:"+schedule.fri
                    }else if(!schedule.sat.isNullOrEmpty()){
                        "Sat:"+schedule.sat
                    }else if(!schedule.sun.isNullOrEmpty()){
                        "Sun:"+schedule.sun
                    }else {
                        "N/A"
                    }
                }else{
                    schedule.tue
                }
            }
            Constants.DayOfTheWeek.Wed->{
                if(schedule.wed.isNullOrEmpty()){
                    if(!schedule.mon.isNullOrEmpty()){
                        "Mon:"+schedule.mon
                    }else if(!schedule.tue.isNullOrEmpty()){
                        "Tue:"+schedule.tue
                    }else if(!schedule.thu.isNullOrEmpty()){
                        "Thu:"+schedule.thu
                    }else if(!schedule.fri.isNullOrEmpty()){
                        "Fri:"+schedule.fri
                    }else if(!schedule.sat.isNullOrEmpty()){
                        "Sat:"+schedule.sat
                    }else if(!schedule.sun.isNullOrEmpty()){
                        "Sun:"+schedule.sun
                    }else {
                        "N/A"
                    }
                }else{
                    schedule.wed
                }
            }
            Constants.DayOfTheWeek.Thu->{
                if(schedule.thu.isNullOrEmpty()){
                    if(!schedule.mon.isNullOrEmpty()){
                        "Mon:"+schedule.mon
                    }else if(!schedule.tue.isNullOrEmpty()){
                        "Tue:"+schedule.tue
                    }else if(!schedule.wed.isNullOrEmpty()){
                        "Wed:"+schedule.wed
                    }else if(!schedule.fri.isNullOrEmpty()){
                        "Fri:"+schedule.fri
                    }else if(!schedule.sat.isNullOrEmpty()){
                        "Sat:"+schedule.sat
                    }else if(!schedule.sun.isNullOrEmpty()){
                        "Sun:"+schedule.sun
                    }else {
                        "N/A"
                    }
                }else{
                    schedule.thu
                }
            }
            Constants.DayOfTheWeek.Fri->{
                if(schedule.fri.isNullOrEmpty()){
                    if(!schedule.mon.isNullOrEmpty()){
                        "Mon:"+schedule.mon
                    }else if(!schedule.tue.isNullOrEmpty()){
                        "Tue:"+schedule.tue
                    }else if(!schedule.wed.isNullOrEmpty()){
                        "Wed:"+schedule.wed
                    }else if(!schedule.thu.isNullOrEmpty()){
                        "Thu:"+schedule.thu
                    }else if(!schedule.sat.isNullOrEmpty()){
                        "Sat:"+schedule.sat
                    }else if(!schedule.sun.isNullOrEmpty()){
                        "Sun:"+schedule.sun
                    }else {
                        "N/A"
                    }
                }else{
                    schedule.fri
                }
            }
            Constants.DayOfTheWeek.Sat->{
                if(schedule.sat.isNullOrEmpty()){
                    if(!schedule.mon.isNullOrEmpty()){
                        "Mon:"+schedule.mon
                    }else if(!schedule.tue.isNullOrEmpty()){
                        "Tue:"+schedule.tue
                    }else if(!schedule.wed.isNullOrEmpty()){
                        "Wed:"+schedule.wed
                    }else if(!schedule.thu.isNullOrEmpty()){
                        "Thu:"+schedule.thu
                    }else if(!schedule.fri.isNullOrEmpty()){
                        "Fri:"+schedule.fri
                    }else if(!schedule.sun.isNullOrEmpty()){
                        "Sun:"+schedule.sun
                    }else {
                        "N/A"
                    }
                }else{
                    schedule.sat
                }
            }
            Constants.DayOfTheWeek.Sun->{
                if(schedule.sun.isNullOrEmpty()){
                    if(!schedule.mon.isNullOrEmpty()){
                        "Mon:"+schedule.mon
                    }else if(!schedule.tue.isNullOrEmpty()){
                        "Tue:"+schedule.tue
                    }else if(!schedule.wed.isNullOrEmpty()){
                        "Wed:"+schedule.wed
                    }else if(!schedule.thu.isNullOrEmpty()){
                        "Thu:"+schedule.thu
                    }else if(!schedule.fri.isNullOrEmpty()){
                        "Fri:"+schedule.fri
                    }else if(!schedule.sat.isNullOrEmpty()){
                        "Sat:"+schedule.sat
                    }else {
                        "N/A"
                    }
                }else{
                    schedule.sun
                }
            }else-> {
                "N/A"
            }
        }
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

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()

        hideHandler.post(hidePart2Runnable)
    }

}