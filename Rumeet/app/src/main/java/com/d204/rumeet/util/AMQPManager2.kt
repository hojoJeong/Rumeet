package com.d204.rumeet.util

import com.rabbitmq.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object AMQPManager {

    val factory = ConnectionFactory().apply {
        host = "j8d204.p.ssafy.io"
        port = 5672
        username = "guest"
        password = "guest"
    }

    var queueName: String = ""
    var channel: Channel? = null

    fun initChannel() {
        CoroutineScope(Dispatchers.Default).launch {
            channel = factory.newConnection().createChannel()
        }
    }

    fun sendMessage(message: String) {
        channel?.basicPublish("", queueName, null, message.toByteArray())
    }

    fun setReceiveMessage(callback: DefaultConsumer) {
        channel?.basicConsume(queueName, true, callback)
    }
}
