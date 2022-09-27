package com.example.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val deleteMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val proMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val copyMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}