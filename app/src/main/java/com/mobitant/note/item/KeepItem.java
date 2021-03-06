package com.mobitant.note.item;

import com.google.gson.annotations.SerializedName;

/**
 * 공유 정보를 저장하는 객체
 */
public class KeepItem extends NoteInfoItem{
    @SerializedName("keep_seq") public String keepSeq;
    @SerializedName("keep_member_seq") public String keepMemberSeq;
    @SerializedName("keep_date") public String keepDate;

    @Override
    public String toString() {
        return "KeepItem{" +
                "keepSeq='" + keepSeq + '\'' +
                ", keepMemberSeq='" + keepMemberSeq + '\'' +
                ", keepDate='" + keepDate + '\'' +
                '}';
    }
}
