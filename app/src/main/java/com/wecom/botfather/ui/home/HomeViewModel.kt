package com.wecom.botfather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wecom.botfather.sdk.BotBean
import com.wecom.botfather.sdk.WeComBotHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel(private val sdk: WeComBotHelper) : ViewModel() {

    private val _bots: MutableLiveData<List<BotBean>> = MutableLiveData()
    val bots: LiveData<List<BotBean>>
        get() = _bots

    fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _bots.postValue(sdk.bots.toMutableList())
            }
        }
    }
}