package com.vrashkov.core.base

sealed class ProgressState{
    
    object Loading: ProgressState()
    
    object Gone: ProgressState()
}