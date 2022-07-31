package com.github.foodiestudio.botfather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.foodiestudio.botfather.sdk.BotBean
import com.github.foodiestudio.botfather.sdk.helper.WeComBotHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel : ViewModel() {

    private val sdk: WeComBotHelper by inject(WeComBotHelper::class.java)

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