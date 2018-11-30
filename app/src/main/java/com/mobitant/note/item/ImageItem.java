package com.mobitant.note.item;

import com.google.gson.annotations.SerializedName;

/**
 * 노트 이미지 정보를 저장하는 객체
 */
public class ImageItem {
    @SerializedName("seq") public int seq;
    @SerializedName("info_seq") public int infoSeq;
    @SerializedName("file_name") public String fileName;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "ImageItem{" +
                "seq=" + seq +
                ", infoSeq=" + infoSeq +
                ", fileName='" + fileName + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
