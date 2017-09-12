package com.example.sea.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    final String[] item= {"Friend1","Friend2","Friend3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable t = new th();
        new Thread(t).start();
        final ListView listview = (ListView)findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);//listview를 위한 배열어댑터 선언 및 리스트뷰 아이템 할당
        listview.setAdapter(adapter);//리스트뷰 아이템 추가
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("item", item[position]);
                startActivity(intent);
            }
        });
    }
    public class th implements Runnable{
        public void run(){
            try {
                Thread.sleep(5000);//5초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendNotif();
        }
    }
    public void sendNotif(){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra("item", item[0]);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);//인위적 스택 조작을 위함 객체생성
        stackBuilder.addParentStack(MainActivity.class);//부모 스택 추가
        stackBuilder.addNextIntent(intent); //ChatActivity의 백을 누르면 부모 액티비티가 나옴
        PendingIntent pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);//알림으로 인한 일반액티비티 이동 발생시 사용. FLAG_UPDATE_CURRENT:동일 세컨액티비티 시작시 그것을 그래로 두고 필요 인텐트만 업데이트함
        //pIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);//알림전용 액티비티이동시 사용

        NotificationCompat.Builder mBuileder = new NotificationCompat.Builder(context);//알림 선언
        mBuileder.setContentIntent(pIntent);//인텐트 이동
        mBuileder.setSmallIcon(R.mipmap.ic_launcher);//알림 아이콘
        mBuileder.setContentTitle(getResources().getString(R.string.title));//알림타이틀
        mBuileder.setContentText("Frend1");
        mBuileder.setAutoCancel(true); //알림 선택시 사라짐

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);//매니저 선언
        mNotificationManager.notify(1,mBuileder.build());//mBileder를 알림
    }

}
