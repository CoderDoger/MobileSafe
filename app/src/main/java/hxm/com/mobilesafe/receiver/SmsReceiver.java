package hxm.com.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import hxm.com.mobilesafe.R;

public class SmsReceiver extends BroadcastReceiver {
    public final String TAG = "hxmReceiver";
    private SharedPreferences sp ;
    public static MediaPlayer mediaPlayer = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
      Object[] objs = (Object[]) intent.getExtras().get("pdus");
      for (Object obj:objs){
         SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
         //得到发送者
         String sender = sms.getOriginatingAddress();
         String safeNum = sp.getString("safeNum",null);

         if(sender.contains(safeNum)){
             String body = sms.getMessageBody();
             switch (body){
                 case "#*location*#":
                     Log.i(TAG,"location");
                     break;
                 case "#*alarm*#":
                     Log.i(TAG,"alarm");
                     mediaPlayer = MediaPlayer.create(context,R.raw.iphone);
                     mediaPlayer.setLooping(true);
                     //设置播放音量最大
                     mediaPlayer.setVolume(1.0f,1.0f);
                     //设置系统媒体音量为最大
                     AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                     int max = audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC);
                     audioManager.setStreamVolume(audioManager.STREAM_MUSIC,max,audioManager.FLAG_SHOW_UI);
                     Log.i(TAG,"最大音量"+ max);
                     mediaPlayer.start();

                     abortBroadcast();
                     break;
                 case "#*stop*#":  //测试用
                     Log.i(TAG,"stop");
                     if (mediaPlayer!=null) {
                         mediaPlayer.stop();
                         mediaPlayer.release();
                         mediaPlayer = null;
                     }
                     abortBroadcast();
                     break;
                 case "#*wipedata*#":
                     Log.i(TAG,"wipedata");
                     break;
                 case "#*lockscreen*#":
                     Log.i(TAG,"lockscreen");
                     break;
             }
         }
      }
    }
}
