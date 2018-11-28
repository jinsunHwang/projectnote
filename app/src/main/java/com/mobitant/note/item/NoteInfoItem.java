package com.mobitant.note.item;

import com.google.gson.annotations.SerializedName;

/**
 * 노트 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class NoteInfoItem {
    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    public String title;
    public String content;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("mod_date") public String modDate;
    @SerializedName("is_keep") public boolean isKeep;
    @SerializedName("image_filename") public String imageFilename;

    @Override
    public String toString() {
        return "NoteInfoItem{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", isKeep=" + isKeep +
                ", imageFilename='" + imageFilename + '\'' +
                '}';
    }
}
