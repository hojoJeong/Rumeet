package com.d204.rumeet.util.amqp

import android.util.Log
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "RunningAMQPManager"

object RunningAMQPManager {
    val factory = ConnectionFactory().apply {
        host = "j8d204.p.ssafy.io"
        port = 5672
        username = "guest"
        password = "guest"
    }

    var runningChannel: Channel? = null
    var runningTag : String = ""

    fun initChannel() {
        CoroutineScope(Dispatchers.IO).launch {
            runningChannel = factory.newConnection().createChannel()
        }
    }

    // 내아이디와, mode를 보냄
    fun startMatching(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runningChannel?.basicPublish("game.exchange", "matching", null, data.toByteArray())
        }
    }

    // 실패를 보냄
    fun failMatching(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                runningChannel?.basicPublish("game.exchange", "cancel", null, data.toByteArray())
            } catch (e: Exception) {
                Log.e(TAG, "sendMessage: ${e.message}")
            }
        }
    }

    // 구독 시작, 연결이 되면 callback 발생
    fun subscribeMatching(userId: Int, callback: DefaultConsumer) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                runningChannel?.basicConsume("matching.queue.${userId}", true, callback)
            } catch (e: Exception) {
                Log.e(TAG, "subscribeMatching: ${e.message}")
            }
        }
    }
    fun subscribeFriendMatching(userId: Int, callback: DefaultConsumer){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "subscribeFriendMatching: 레빗엠큐 메니저 userID: $userId")
                Log.d(TAG, "subscribeFriendMatching: $runningChannel")
                runningChannel?.basicConsume("friend.user.${userId}", true, callback)
            } catch (e: Exception) {
                Log.e(TAG, "subscribeMatching: ${e.message}")
            }
        }
    }

    // 게임 시작, 보내기
    fun sendRunning(partnerId: Int, roomId: Int, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runningChannel?.basicPublish(
                "",
                "game.${roomId}.${partnerId}",
                null,
                message.toByteArray()
            )
        }
    }

    // 게임 관련 데이터 받기
    fun receiveRunning(roomId: Int, userId: Int, callback: DefaultConsumer) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "receiveRunning: ${roomId}.${userId}")
            runningTag = runningChannel?.basicConsume("game.${roomId}.${userId}", callback) ?: ""
        }
    }

    // 게임 종료 보내기
    fun sendEndGame(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runningChannel?.basicPublish("", "end.queue", null, message.toByteArray())
            runningChannel?.basicCancel(runningTag)
        }
    }
}