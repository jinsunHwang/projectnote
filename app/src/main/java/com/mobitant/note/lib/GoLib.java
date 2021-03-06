package com.mobitant.note.lib;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

import com.mobitant.note.MainActivity;
import com.mobitant.note.NoteInfoActivity;
//import com.mobitant.note.NoteListActivity;
import com.mobitant.note.NoteRegisterActivity;
import com.mobitant.note.NoteUpdateActivity;
import com.mobitant.note.ProfileActivity;

public class GoLib {
    public final String TAG = GoLib.class.getSimpleName();
    private volatile static GoLib instance;

    public static GoLib getInstance() {
        if (instance == null) {
            synchronized (GoLib.class) {
                if (instance == null) {
                    instance = new GoLib();
                }
            }
        }
        return instance;
    }

    /**
     * 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragment(FragmentManager fragmentManager, int containerViewId,
                           Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
        System.out.println("프래그먼트 테스트 ");
    }

    /**
     * 뒤로가기를 할 수 있는 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragmentBack(FragmentManager fragmentManager, int containerViewId,
                               Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 이전 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     */
    public void goBackFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    /**
     * 프로파일 액티비티를 실행한다.
     * @param context 컨텍스트
     */
    public void goProfileActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 노트 정보 등록 액티비티를 실행한다.
     * @param context 컨텍스트
     */
    public void goNoteRegisterActivity(Context context) {
        Intent intent = new Intent(context, NoteRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
//
//    리스트 액티비티 실행
    public void goNoteListActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }



    /**
     * 노트 정보 액티비티를 실행한다.
     * @param context 컨텍스트
     * @param infoSeq 노트 정보 일련번호
     */
    public void goNoteInfoActivity(Context context, int infoSeq) {
        System.out.println("노트 정보 gogo");
        Intent intent = new Intent(context, NoteUpdateActivity.class);
        intent.putExtra(NoteUpdateActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }

    /**
     * 노트 정보 액티비티를 실행한다.
     * @param context 컨텍스트
     * @param infoSeq 노트 정보 일련번호
     */
    public void goNoteShareActivity(Context context, int infoSeq) {
        System.out.println("노트 정보 gogo");
        Intent intent = new Intent(context, NoteInfoActivity.class);
        intent.putExtra(NoteInfoActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }


}

