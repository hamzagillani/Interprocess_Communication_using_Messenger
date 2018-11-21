package com.digicon_valley.interprocess_communication_using_messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MyService extends Service {

    private final int JOB_1=1;
    private final int JOB_2=2;
    private final int JOB_1_RESPONSE=3;
    private final int JOB_2_RESPONSE=4;

    Messenger messanger=new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent) {
        return messanger.getBinder();
    }
    class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
        Message MSG;
        String Message;
            Bundle bundle=new Bundle();
            switch (msg.what){
                case JOB_1:
                    Message="This is the First Message From Service";
                    MSG=android.os.Message.obtain(null,JOB_1_RESPONSE);
                 bundle.putString("response_message",Message);
                 MSG.setData(bundle);
                 try{
                     msg.replyTo.send(MSG);
                 }catch (RemoteException e){
                     e.printStackTrace();
                 }
                 break;
                case JOB_2:
                    Message="This is the Second Message From Service";
                    MSG=android.os.Message.obtain(null,JOB_2_RESPONSE);
                    bundle.putString("response_message",Message);
                    MSG.setData(bundle);
                    try{
                        msg.replyTo.send(MSG);
                    }catch (RemoteException e){
                        e.printStackTrace();
            }break;
            default:
            super.handleMessage(msg);
        }
    }}
}
