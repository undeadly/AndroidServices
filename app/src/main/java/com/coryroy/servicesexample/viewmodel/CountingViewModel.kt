package com.coryroy.servicesexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object CountingViewModel : ViewModel() {
    var count = MutableLiveData(0)
}