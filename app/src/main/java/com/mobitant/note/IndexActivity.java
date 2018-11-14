package com.mobitant.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.mobitant.note.item.MemberInfoItem;
import com.mobitant.note.lib.EtcLib;
import com.mobitant.note.lib.MyLog;
import com.mobitant.note.lib.RemoteLib;
import com.mobitant.note.lib.StringLib;
import com.mobitant.note.remote.RemoteService;
import com.mobitant.note.remote.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//시작 액티비티이며, 이 액티비티에서 사용자 정보를 조회해서
//메인 액티비티를 실행할지, 프로필 액티비티를 실행할지를 결정한다.
public class IndexActivity extends AppCompatActivity {
    private  final String TAG = this.getClass().getSimpleName();

    Context context;

    //    레이아웃을 설정하고 인터넷에 연결되어 있는지를 확인한다.
//    만약 인터넷에 연결되어 있지 않다면 showNoService() 메소드를 호출한다.
//    @param savedInstanceState 액티비티가 새로 생성되었을 경우에 이전 상태 값을 가지는 객체
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        System.out.println("test11111111");
        context=this;

        if(!RemoteLib.getInstance().isConnected(context)){

            showNoService();
            return;
        }

    }

    //    일정 시간(1.2초) 이후에 startTask() 메소드를 호출해서
//    서버에 사용자 정보를 조회한다.
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("test222222");
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTask();
            }
        },1200);
    }

    //    현재 인터넷에 접속할 수 없기 때문에 서비스를 사용할 수 없다는 메시지와
//    화면 종료 버튼을 보여준다.
    private void showNoService(){

        TextView messageText = (TextView) findViewById(R.id.message);
        messageText.setVisibility(View.VISIBLE);

        Button closeButton = (Button) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        closeButton.setVisibility(View.VISIBLE);
    }

    //    현재 폰의 전화번호와 동일한 사용자 정보를 조회할 수 있도록
//    selectMemberInfo() 메소드를 호출한다.
//    그리고 setLastKnowLocation() 메소드를 호출해서 현재 위치 정보를 설정한다.
    public void startTask(){
        System.out.println("test333333");
        String phone = EtcLib.getInstance().getPhoneNumber(this);

        selectMemberInfo(phone);
//        GeoLib.getInstance().setLastKnownLocation(this);
    }

    //    레트로핏을 활용해서 서버로부터 사용자 정보를 조회한다.
//    사용자 정보를 조회했다면 setMemberInfoItem() 메소드를 호출하고,
//    그렇지 않다면 goProfileActivity() 메소드를 호출한다.
//    @param phone 폰의 전화번호
    public void selectMemberInfo(String phone){
        System.out.println("test3333-2");
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        System.out.println("--------000000---------");
        Call<MemberInfoItem> call = remoteService.selectMemberInfo(phone);
        System.out.println("--------000001---------");
        call.enqueue(new Callback<MemberInfoItem>() {
            @Override
            public void onResponse(Call<MemberInfoItem> call, Response<MemberInfoItem> response) {
                MemberInfoItem item= response.body();
                System.out.println("--------000002---------");
                if(response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)){
                    System.out.println("test3333-333 ok");
                    MyLog.d(TAG,"success"+response.body().toString());
                    setMemberInfoItem(item);
                }
                else {
                    System.out.println("test3333-333fail");
                    MyLog.d(TAG,"not success");
                    goProfileActivity(item);
                }
            }

            @Override
            public  void onFailure(Call<MemberInfoItem> call, Throwable t){
                MyLog.d(TAG,"no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }


    //    전달받은 MemberInfoItem을 Application 객체에 저장한다.
//    그리고 startMain() 메소드를 호출한다.
//    Application 클래스는 애플리케이션의 상태를 저장하고 이를 어디서나 호출해서 사용할수 있도록 해주는 클래스다
//    @param item 사용자 정보
    private  void  setMemberInfoItem(MemberInfoItem item){
        System.out.println("test4444 OK");
        ((MyApp) getApplicationContext()).setMemberInfoItem(item);

        startMain();
    }

    //    MainActivity를 실행하고 현재 액티비티를 종료한다.
    public void startMain(){
        Intent intent = new Intent(IndexActivity.this,MainActivity.class);
        startActivity(intent);

        finish();
    }

    //    사용자 정보를 조회하지 못했다면 insertMemberPhone() 메소드를 통해
//    전화번호를 서버에 저장하고, MainActivity를 실행한 후에 ProfileActivity를 실행한다.
//    그리고 현재 액티비티를 종료한다.
//    @param item 사용자 정보
    private void goProfileActivity(MemberInfoItem item){
        System.out.println("test4444 fail");
        if(item == null || item.seq<=0){
            insertMemberPhone();
        }

        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        startActivity(intent);
//        Intent intent2 = new Intent(this,ProfileActivity.class);
//        startActivity(intent2);

        finish();
    }

    //    폰의 전화번호를 서버에 저장한다.
//    레트로핏을 활용하여 서버에 전화번호를 저장하고 있다.
//    노드가 서버에 수신하여 데이터베이스 저장
//    response.isSuccessful()이 true 인지 파악해서 저장 성공여부 확인
    private void insertMemberPhone(){
        String phone= EtcLib.getInstance().getPhoneNumber(context);
        RemoteService remoteService= ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertMemberPhone(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    MyLog.d(TAG,"success insert id"+response.body().toString());
                }
                else{
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();

                    MyLog.d(TAG,"fail"+statusCode+errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG,"no internet connectivity");

            }
        });
    }


}


