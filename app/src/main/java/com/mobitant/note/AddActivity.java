package com.mobitant.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText add_editText_title;
    EditText add_editText_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add Notes");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Intent intent = new Intent(getApplicationContext(),ShareActivity.class);

            add_editText_title = (EditText) findViewById(R.id.add_EditText_Title);
            add_editText_content = (EditText) findViewById(R.id.add_EditText_Content);

            intent.putExtra("title",add_editText_title.getText().toString());
            intent.putExtra("content",add_editText_content.getText().toString());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}