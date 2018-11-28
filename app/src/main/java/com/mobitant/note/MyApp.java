package com.mobitant.note;

import android.app.Application;
import android.os.StrictMode;

import com.mobitant.note.item.MemberInfoItem;
import com.mobitant.note.item.NoteInfoItem;

public class MyApp extends Application {
    private MemberInfoItem memberInfoItem;
    private NoteInfoItem noteInfoItem;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public MemberInfoItem getMemberInfoItem() {
        if (memberInfoItem == null) memberInfoItem = new MemberInfoItem();

        return memberInfoItem;
    }

    public void setMemberInfoItem(MemberInfoItem item) {
        this.memberInfoItem = item;
    }

    public int getMemberSeq() {
        return memberInfoItem.seq;
    }

    public void setNoteInfoItem(NoteInfoItem noteInfoItem){
        this.noteInfoItem = noteInfoItem;
    }

    public NoteInfoItem getNoteInfoItem() {
        return noteInfoItem;
    }
}