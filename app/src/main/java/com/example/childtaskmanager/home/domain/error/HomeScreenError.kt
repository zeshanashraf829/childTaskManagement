package com.example.childtaskmanager.home.domain.error


sealed class HomeScreenError(){
    object NetworkError : HomeScreenError()
    object UnknownError : HomeScreenError()
    object TaskNotLoaded : HomeScreenError()
}