package com.coryroy.servicesexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object CountingViewModel : ViewModel() {
    var count = MutableLiveData(0)
}