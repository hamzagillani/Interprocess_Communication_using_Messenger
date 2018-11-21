package com.digicon_valley.interprocess_communication_using_messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private final int JOB_1=1;
    private final int JOB_2=2;
    private final int JOB_1_RESPONSE=3;
    private final int JOB_2_RESPONSE=4;
    boolean inBind=false;
    Messenger messenger=null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.text_view);

        Intent intent=new Intent(this,MyService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            messenger=new Messenger(service);
            inBind=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger=null;
            inBind=false;


        }
    };

    public void getFirstServiceMessage(View view) {

        Message meg;
        String button_text=(String) ((Button)view).getText();
        if (button_text.equals("Get First Message")){

            meg=Message.obtain(null,JOB_1);
            meg.replyTo=new Messenger(new ResponseHandler());
            try{
                messenger.send(meg);
            }catch (RemoteException e){
                e.printStackTrace();
            }


        }else if (button_text.equals("Get Second Message")){

            meg=Message.obtain(null,JOB_2);
            meg.replyTo=new Messenger(new ResponseHandler());
            try{
                messenger.send(meg);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void getSecondServiceMessage(View view) {
    }

    @Override
    protected void onStop() {

        unbindService(serviceConnection);
        inBind=false;
        messenger=null;
        super.onStop();
    }

    class ResponseHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {

            String message;
            switch (msg.what){

                case JOB_1_RESPONSE:
                    message=msg.getData().getString("response_message");
                    textView.setText(message);
                    break;
                case JOB_2_RESPONSE:
                    message=msg.getData().getString("response_message");
                    textView.setText(message);
                    break;

            }

            super.handleMessage(msg);
        }
    }
}
