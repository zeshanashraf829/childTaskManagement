package com.example.childtaskmanager.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

/**
 * @param ViewState This represents the current state of the view, that will be exposed by the ViewModel
 * @param ViewEvent This is used to send one-off or one-shot events/effects when we do not want to change the state
 */
abstract class BaseViewModel<ViewState : Any, ViewEvent: Any> constructor(
    initialState: ViewState
) : ViewModel() {

    private val _viewState = MutableLiveData(initialState)
    private val _viewEvents = LiveEvent<ViewEvent>()
    val viewState: LiveData<ViewState> = _viewState
    val viewEvents: LiveEvent<ViewEvent> = _viewEvents

    protected fun setState(newState: ViewState) {
        _viewState.value = newState
    }

    protected fun sendEvent(newEvent: ViewEvent){
        _viewEvents.value = newEvent
    }

}