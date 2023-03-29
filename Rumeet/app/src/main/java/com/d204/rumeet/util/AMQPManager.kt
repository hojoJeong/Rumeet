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

    var chattingQueueName: String = ""
    var userQueueName : String = ""
    var chattingChanel: Channel? = null
    var userChannel: Channel? = null

    fun initChannel() {
        CoroutineScope(Dispatchers.Default).launch {
            chattingChanel = factory.newConnection().createChannel()
            userChannel = factory.newConnection().createChannel()
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            chattingChanel?.basicPublish("", chattingQueueName, null, message.toByteArray())
        }
    }

    fun setReceiveMessage(callback: DefaultConsumer) {
        CoroutineScope(Dispatchers.IO).launch {
            chattingChanel?.basicConsume(chattingQueueName, true, callback)
        }
    }

    fun setChattingListReceive(callback : DefaultConsumer){
        CoroutineScope(Dispatchers.IO).launch {
            userChannel?.basicConsume(userQueueName,true, callback)
        }
    }
}
