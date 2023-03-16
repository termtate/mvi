package com.example.myapplicationg.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<State : UiState, Intent : UiIntent> constructor(
    private val ioDispatcher: CoroutineContext
) : ViewModel() {

    private val initialState: State by lazy { createInitialState() }

    protected abstract fun createInitialState(): State

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)

    val uiState = _uiState.asStateFlow()


    protected abstract suspend fun handleIntent(intent: Intent)

    fun sendIntent(intent: Intent) {
        viewModelScope.launch(ioDispatcher) {
            handleIntent(intent)
        }
    }

    protected fun setState(reduce: State.() -> State) {
        _uiState.update { it.reduce() }
    }

}


interface UiState

interface UiIntent
