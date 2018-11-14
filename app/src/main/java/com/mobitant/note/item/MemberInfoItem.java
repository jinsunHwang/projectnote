package com.mobitant.note.item;

import com.google.gson.annotations.SerializedName;

public class MemberInfoItem {
    public int seq;
    public String phone;
    public String name;
    @SerializedName("member_icon_filename") public String memberIconFilename;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "MemberInfoItem{" +
                "seq=" + seq +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", memberIconFilename='" + memberIconFilename + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}