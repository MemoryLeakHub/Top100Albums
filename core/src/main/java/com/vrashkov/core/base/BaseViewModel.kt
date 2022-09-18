package com.vrashkov.core.base

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel<ViewState: BaseViewState, ViewEvent: BaseViewEvent>: ViewModel() {
    abstract val viewState: MutableState<ViewState>

    protected val _navigationEventFlow: MutableSharedFlow<NavigationEvent> = MutableSharedFlow(replay = 0)
    val navigationEventFlow: SharedFlow<NavigationEvent> = _navigationEventFlow

    protected val _actionsEventFlow: MutableSharedFlow<ActionsEvent> = MutableSharedFlow(replay = 0)
    val actionsEventFlow: SharedFlow<ActionsEvent> = _actionsEventFlow

    open fun onTriggerEvent(event: ViewEvent) {}
}