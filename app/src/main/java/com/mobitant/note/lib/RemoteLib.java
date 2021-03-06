package com.mobitant.note.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.mobitant.note.remote.RemoteService;
import com.mobitant.note.remote.ServiceGenerator;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteLib {
    public static final String TAG = RemoteLib.class.getSimpleName();

    private volatile static RemoteLib instance;

    public static RemoteLib getInstance() {
        if (instance == null) {
            synchronized (RemoteLib.class) {
                if (instance == null) {
                    instance = new RemoteLib();
                }
            }
        }
        return instance;
    }

    /**
     * 네트워크 연결 여부를 반환한다.
     *
     * @param context 컨텍스트
     * @return 네트워크 연결여부
     */
    public boolean isConnected(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 사용자 프로필 아이콘을 서버에 업로드한다.
     * @param memberSeq 사용자 일련번호
     * @param file 파일 객체
     */
    public void uploadMemberIcon(int memberSeq, File file) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody memberSeqBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "" + memberSeq);

        Call<ResponseBody> call =
                remoteService.uploadMemberIcon(memberSeqBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadMemberIcon success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadMemberIcon fail");
            }
        });
    }

    /**
     * 노트 이미지를 서버에 업로드한다.
     * @param infoSeq 노트 정보 일련번호
     * @param file 파일 객체
     * @param handler 처리 결과를 응답할 핸들러
     */
    public void uploadNoteImage(int infoSeq, File file, final Handler handler) {
        System.out.println("RemoteLib 이미지 uploadNoteImage");
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody infoSeqBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "" + infoSeq);

        Call<ResponseBody> call =
                remoteService.uploadNoteImage(infoSeqBody,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadNoteImage success");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadNoteImage fail");
            }
        });
    }


}