package com.mobitant.note.remote;

import com.mobitant.note.item.KeepItem;
import com.mobitant.note.item.MemberInfoItem;
import com.mobitant.note.item.NoteInfoItem;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteService {

    String BASE_URL = "http://115.86.19.118:3000";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //사용자 정보
    @GET("/member/{phone}")
    Call<MemberInfoItem> selectMemberInfo(@Path("phone") String phone);

    @FormUrlEncoded
    @POST("/member/phone")
    Call<String> insertMemberPhone(@Field("phone") String phone);

    @POST("/member/info")
    Call<String> insertMemberInfo(@Body MemberInfoItem memberInfoItem);

    @Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);

    //노트 정보
    @GET("/note/info/{info_seq}")
    Call<NoteInfoItem> selectNoteInfo(@Path("info_seq") int noteInfoSeq,
                                      @Query("member_seq") int memberSeq);

    @POST("/note/info")
    Call<String> insertNoteInfo(@Body NoteInfoItem infoItem);

    @Multipart
    @POST("/note/info/image")
    Call<ResponseBody> uploadNoteImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part MultipartBody.Part file);

    @GET("/note/list")
    Call<ArrayList<NoteInfoItem>> listNoteInfo(@Query("member_seq") int memberSeq,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);

    @GET("/note/share")
    Call<ArrayList<NoteInfoItem>> listShareInfo(@Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);


    //즐겨찾기
    @POST("/keep/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @GET("/keep/list")
    Call<ArrayList<KeepItem>> listKeep(@Query("member_seq") int memberSeq);

//    @Multipart
//    @POST("/member/info/image")
//    Call<ResponseBody> uploadNoteImage(@Part("info_seq") RequestBody infoSeq,
//                                       @Part MultipartBody.Part file);
}
