package com.d204.rumeet.util.amqp

import android.util.Log
import com.rabbitmq.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ChattingAMQPMananer {

    val factory = ConnectionFactory().apply {
        host = "j8d204.p.ssafy.io"
        port = 5672
        username = "guest"
        password = "guest"
    }

    var chattingQueueName: String = ""
    var userQueueName : String = ""
    var chattingChannelTag = ""
    var chattingChanel: Channel? = null

    fun initChannel() {
        CoroutineScope(Dispatchers.IO).launch {
            chattingChanel = factory.newConnection().createChannel()
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                chattingChanel?.basicPublish("", "chat.queue", null, message.toByteArray())
            }catch (e : Exception){
                Log.e("TAG", "sendMessage: ${e.message}", )
            }
        }
    }

    fun setReceiveMessage(callback: DefaultConsumer) {
        CoroutineScope(Dispatchers.IO).launch {
            chattingChannelTag = chattingChanel?.basicConsume(chattingQueueName, true, callback) ?: ""
        }
    }

    fun setChattingListReceive(callback : DefaultConsumer){
        CoroutineScope(Dispatchers.IO).launch {
            chattingChanel?.basicConsume(userQueueName,true, callback)
        }
    }

    fun unSubscribeChatting(){
        CoroutineScope(Dispatchers.IO).launch {
            chattingChanel?.basicCancel(chattingChannelTag)
        }
    }
}
