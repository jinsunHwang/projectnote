package com.mobitant.note;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.mobitant.note.item.NoteInfoItem;
import com.mobitant.note.lib.DialogLib;
import com.mobitant.note.lib.MyLog;
import com.mobitant.note.lib.StringLib;
import com.mobitant.note.remote.RemoteService;
import com.mobitant.note.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 맛집 정보를 보는 액티비티이다.
 */
public class NoteInfoActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final String INFO_SEQ = "INFO_SEQ";

    Context context;

    int memberSeq;
    int noteInfoSeq;

    NoteInfoItem item;

    View loadingText;
    ScrollView scrollView;

    /**
     * 맛집 정보를 보여주기 위해 사용자 시퀀스와 맛집 정보 시퀀스를 얻고
     * 이를 기반으로 서버에서 맛집 정보를 조회하는 메소드를 호출한다.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);

        context = this;

        loadingText = findViewById(R.id.loading_layout);

        memberSeq = ((MyApp)getApplication()).getMemberSeq();
        noteInfoSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        selectNoteInfo(noteInfoSeq, memberSeq);

        setToolbar();
    }

    /**
     * 툴바를 설정한다.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    /**
     * 왼쪽 화살표 메뉴(android.R.id.home)를 클릭했을 때와
     * 오른쪽 상단 닫기 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.action_close :
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param noteInfoSeq 맛집 정보 시퀀스
     * @param memberSeq 사용자 시퀀스
     */
    private void selectNoteInfo(int noteInfoSeq, int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<NoteInfoItem> call = remoteService.selectNoteInfo(noteInfoSeq, memberSeq);

        call.enqueue(new Callback<NoteInfoItem>() {
            @Override
            public void onResponse(Call<NoteInfoItem> call, Response<NoteInfoItem> response) {
                NoteInfoItem infoItem = response.body();

                if (response.isSuccessful() && infoItem != null && infoItem.seq > 0) {
                    item = infoItem;
                    setView();
                    loadingText.setVisibility(View.GONE);
                } else {
                    loadingText.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.loading_text)).setText(R.string.loading_not);
                }
            }

            @Override
            public void onFailure(Call<NoteInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    /**
     * 서버에서 조회한 맛집 정보를 화면에 설정한다.
     */
    private void setView() {
        getSupportActionBar().setTitle(item.title);

        ImageView infoImage = (ImageView) findViewById(R.id.info_image);
        setImage(infoImage, item.imageFilename);



        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        FragmentManager fm = getSupportFragmentManager();


        TextView nameText = (TextView) findViewById(R.id.title);
        if (!StringLib.getInstance().isBlank(item.title)) {
            nameText.setText(item.title);
        }



        TextView content = (TextView) findViewById(R.id.content);
        if (!StringLib.getInstance().isBlank(item.content)) {
            content.setText(item.content);
        }
//        else {
//            content.setText(R.string.no_text);
//        }
    }




    /**
     * 맛집 이미지를 화면에 보여준다.
     * @param imageView 맛집 이미지를 보여줄 이미지뷰
     * @param fileName 서버에 저장된 맛집 이미지의 파일 이름
     */
    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.bg_note_drawer).into(imageView);
        } else {
            Picasso.with(context).load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        ((MyApp) getApplication()).setNoteInfoItem(item);
    }
}
