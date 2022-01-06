package com.wecom.botfather.mock

import com.wecom.botfather.sdk.BotBean
import com.wecom.botfather.sdk.Platform

/**
 * TODO 待删除
 */
object MockData {
    //https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=b7e5e59b-a838-4908-82e9-c330ebccaad8
    val bot = BotBean(
        "b7e5e59b-a838-4908-82e9-c330ebccaad8",
        Platform.WeCom,
        "28号",
        "https://wework.qpic.cn/wwhead/duc2TvpEgSSWiaVLaJnssaWaTMWYYweWJLicI2BQLCOdKRWltMHBNQ7HJgJP4Lcqano8Y2WObhRibw/100"
    )
}