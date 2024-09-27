package com.frost.lab01regularexpression

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyReceiver : BroadcastReceiver() {
    //val pattern1 = Regex("^\\d{2}")
    //^로 시작함을 의미. d는 숫자 {2}는 2자리 의미
    val pattern1 = Regex("""^\d{2}/\d{2}\s\d{2}:\d{2}\s+.+원$""")//이런식으로 표현도 가능 """~"""
    //정규식 객체로 패턴
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        //이 함수가 finish되기 전까지는 시스템에 의해서 강제 종료되지 않음
        scope.launch {
            //문자가 오면 해당 메서드가 호출됨 이때 intent에 관련된 정보를 Callback
            if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                val message = msg[0].messageBody
                //해당 메세지에 내가 원하는 문자열 있는지 비교하면 된다.

                if (message.contains("건국카드")) {
                    val tempstr = message.split("\n")
                    //줄단위로 split
                    //for(str in tempstr.subList(1, tempstr.size)){
                    //일부 줄만 갖고 올 수도 있음
                    var result = false
                    for (str in tempstr) {
                        if (pattern1.containsMatchIn(str)) {//패턴 비교
                            result = true
                            break
                        }
                    }
                    if (result) {
                        val newIntent = Intent(context, MainActivity::class.java)
                        newIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        //launch mode 지정
                        newIntent.putExtra("msgSender", msg[0].originatingAddress)
                        newIntent.putExtra("msgBody", msg[0].messageBody)
                        context.startActivity(newIntent)
                    }
                }
            }
        }
        pendingResult.finish()
        //코루틴 스코프가 끝났으니 종료 보장
    }
}