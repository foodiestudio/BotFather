package com.github.foodiestudio.botfather.ui.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * 企业微信消息模版编辑页面
 * TODO
 * - 支持给部分文字改颜色（企业微信目前只支持三种模式）
 * - 支持预览（形式上要尽量和企业微信一致）
 */
class MessageTemplateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        }
    }
}
