package com.example.childtaskmanager.home

import androidx.lifecycle.viewModelScope
import com.example.childtaskmanager.core.domain.Result
import com.example.childtaskmanager.core.presentation.BaseViewModel
import com.example.childtaskmanager.core.utils.Constants
import com.example.childtaskmanager.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel<Any, Any>(initialState = HomeScreenViewState()) {

    private fun getViewState() = viewState.value as HomeScreenViewState

    fun fetchTasks() {

        viewModelScope.launch {

            when(val result = homeRepository.loadTaskFromLocalDB()){
                is Result.Fail->{
                    loadTaskFromServer()
                }
                is Result.Ok->{
                    setState(getViewState().copy(isLoading = false))
                    setState(getViewState().copy(tasks = result.data))
                }
            }

        }
    }

    private fun loadTaskFromServer(){
        viewModelScope.launch {

            setState(getViewState().copy(isLoading = true))

            when (val resultLive = homeRepository.loadTasks()) {
                is Result.Fail -> {
                    setState(getViewState().copy(isLoading = false))
                    sendEvent(HomeScreenViewEvents.HomeScreenRequestFailed(resultLive.error))
                }
                is Result.Ok -> {
                    setState(getViewState().copy(isLoading = false))
                    setState(getViewState().copy(tasks = resultLive.data))

                }
            }
            setState(getViewState().copy(isLoading = false))
        }
    }

    fun loadTaskByIDFromDb(taskId:String){
        viewModelScope.launch {

            setState(getViewState().copy(isLoading = true))

            when (val resultLive = homeRepository.loadTaskByIDFromDb(taskId)) {
                is Result.Fail -> {
                    setState(getViewState().copy(isLoading = false))
                    sendEvent(HomeScreenViewEvents.HomeScreenRequestFailed(resultLive.error))
                }
                is Result.Ok -> {
                    setState(getViewState().copy(isLoading = false))
                    setState(getViewState().copy(task = resultLive.data))

                }
            }
            setState(getViewState().copy(isLoading = false))
        }
    }

    fun loadTodayDayTime(){
        viewModelScope.launch {
            val calendar: Calendar = Calendar.getInstance()
            val day: Int = calendar.get(Calendar.DAY_OF_WEEK)

            when (day) {
                Calendar.SUNDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Sun))
                }
                Calendar.MONDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Mon))
                }
                Calendar.TUESDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Tue))
                }
                Calendar.WEDNESDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Wed))
                }
                Calendar.THURSDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Thu))
                }
                Calendar.FRIDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Fri))
                }
                Calendar.SATURDAY -> {
                    setState(getViewState().copy(day = Constants.DayOfTheWeek.Sat))
                }
            }
        }
    }


}