package com.mobitant.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mobitant.note.item.MemberInfoItem;
import com.mobitant.note.lib.GoLib;
import com.mobitant.note.lib.StringLib;
import com.mobitant.note.remote.RemoteService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = getClass().getSimpleName();

    MemberInfoItem memberInfoItem;
    DrawerLayout drawer;
    View headerLayout;

    CircleImageView profileIconImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memberInfoItem = ((MyApp)getApplication()).getMemberInfoItem();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerLayout = navigationView.getHeaderView(0);

/*
        GoLib.getInstance()
                .goFragment(getSupportFragmentManager(), R.id.content_main,
                        BestFoodListFragment.newInstance());
*/

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
//                startActivity(intent);
//            }
//        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        setProfileView();
    }

    private void setProfileView() {
        profileIconImage = (CircleImageView) headerLayout.findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                GoLib.getInstance().goProfileActivity(MainActivity.this);
            }
        });

        if (StringLib.getInstance().isBlank(memberInfoItem.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + memberInfoItem.memberIconFilename)
                    .into(profileIconImage);
        }

        TextView nameText = (TextView) headerLayout.findViewById(R.id.name);

        if (memberInfoItem.name == null || memberInfoItem.name.equals("")) {
            nameText.setText(R.string.name_need);
        } else {
            nameText.setText(memberInfoItem.name);
        }
    }


    /**
     * 폰에서 뒤로가기 버튼을 클릭했을 때 호출하는 메소드이며
     * 네비게이션 메뉴가 보여진 상태일 경우, 네비게이션 메뉴를 닫는다.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



//    /**
//     * 네비게이션 메뉴를 클릭했을 때 호출되는 메소드
//     * @param item 메뉴 아이템 객체
//     * @return 메뉴 클릭 이벤트의 처리 여부
//     */
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_list) {
//            GoLib.getInstance().goFragment(getSupportFragmentManager(),
//                    R.id.content_main, BestFoodListFragment.newInstance());
//
//        } else if (id == R.id.nav_map) {
//            GoLib.getInstance().goFragment(getSupportFragmentManager(),
//                    R.id.content_main, BestFoodMapFragment.newInstance());
//
//        } else if (id == R.id.nav_keep) {
//            GoLib.getInstance().goFragment(getSupportFragmentManager(),
//                    R.id.content_main, BestFoodKeepFragment.newInstance());
//
//        } else if (id == R.id.nav_register) {
//            GoLib.getInstance().goBestFoodRegisterActivity(this);
//
//        } else if (id == R.id.nav_profile) {
//            GoLib.getInstance().goProfileActivity(this);
//        }
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mainpage) {
            // Handle the camera action
        } else if (id == R.id.nav_note) {

        } else if (id == R.id.nav_profile) {
            GoLib.getInstance().goProfileActivity(this);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
