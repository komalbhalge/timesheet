package com.bnm.timesheet.ui.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InputHoursSharedViewModel: ViewModel() {
    val name = MutableLiveData<String>()

    fun sendName(text: String) {
        name.value = text
    }
}