package app.socketlib.com.minasocektdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.socketlib.com.library.ContentServiceHelper;
import app.socketlib.com.library.listener.SocketResponseListener;
import app.socketlib.com.library.socket.SessionManager;
import app.socketlib.com.library.socket.SocketConfig;

public class MainActivity extends AppCompatActivity implements SocketResponseListener, View.OnClickListener {

    private EditText editText;
    private Button send;
    private TextView textView;
    private static final String HOST = "192.168.1.174";
    private static final int PORT = 2345;
    public static final byte[] HEART_BEAT_=new byte[]{ 0x51 ,0x00 ,0x2E , 0x10, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00,  0x01, 0x05, 0x00, (byte)0x00, (byte)0x00, (byte)0x01, 0x00, 0x00, 0x00,0x00, 0x00,0x00, 0x00, 0x00,0x00,0x00, 0x00, 0x00,0x00,0x00, 0x00,0x00,0x00,0x00, 0x00,0x00,0x00, 0x00, 0x00, 0x00, 0x00, 0x00,0x00, 0x00,(byte)0x22,(byte)0xE9 };
    public static final byte[] HEART_BEAT_RESPONSE=new byte[]{0x51, 0x00, 0x0E, 0x10, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x02, 0x05, (byte)0x82, (byte)0x80 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.et_input);
        send = (Button) findViewById(R.id.btn_send);
        textView = (TextView) findViewById(R.id.tv_server);
        SocketConfig socketConfig = new SocketConfig.Builder(getApplicationContext())
                .setIp("120.25.195.173")//ip
                .setPort(16080)//端口
                .setReadBufferSize(10240)//readBuffer
                .setIdleTimeOut(30)//客户端空闲时间,客户端在超过此时间内不向服务器发送数据,则视为idle状态,则进入心跳状态
                .setTimeOutCheckInterval(10)//客户端连接超时时间,超过此时间则视为连接超时
                .setRequestInterval(10)//请求超时间隔时间
                .setHeartbeatRequest("(1,1)\r\n")//与服务端约定的发送过去的心跳包
                .setHeartbeatResponse("(10,10)\r\n") //与服务端约定的接收到的心跳包
                .builder();
        ContentServiceHelper.bindService(this, socketConfig);
        send.setOnClickListener(this);
        SessionManager.getInstance().setReceivedResponseListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContentServiceHelper.unBindService(this);
    }

    @Override
    public void socketMessageReceived(String msg) {
        textView.setText(msg);
    }

    @Override
    public void onClick(View view) {
        ContentServiceHelper.sendClientMsg(editText.getText().toString() + "\n");
    }


}
