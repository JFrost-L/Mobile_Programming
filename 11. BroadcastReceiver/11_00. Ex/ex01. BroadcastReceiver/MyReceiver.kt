package com.frost.ex01broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //문자가 오면 해당 메서드가 호출됨 이때 intent에 관련된 정보를 갖고 옴
        if(intent.action.equals("android.provider.Telephony.SMS_RECEIVED")){
            val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            /*
            //intent로부터 메세지 정보 가져오기
            for(smsMessage in msg){
                Log.i("msg", smsMessage.displayMessageBody)
            }
            */
            val newIntent = Intent(context, MainActivity::class.java)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            //launch mode 지정
            newIntent.putExtra("msgSender", msg[0].originatingAddress)
            newIntent.putExtra("msgBody",msg[0].messageBody)
            context.startActivity(newIntent)
        }
    }
}