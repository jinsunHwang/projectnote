package com.mobitant.note;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobitant.note.item.MemberInfoItem;
import com.mobitant.note.item.NoteInfoItem;
import com.mobitant.note.lib.DialogLib;
import com.mobitant.note.lib.GoLib;
import com.mobitant.note.lib.MyLog;
import com.mobitant.note.lib.MyToast;
import com.mobitant.note.lib.StringLib;
import com.mobitant.note.remote.RemoteService;
import com.mobitant.note.remote.ServiceGenerator;

import org.parceler.Parcels;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoteUpdateInputFragment extends Fragment implements View.OnClickListener {
    public static final String INFO_ITEM = "INFO_ITEM";
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    NoteInfoItem infoItem;
    NoteInfoItem saveItem = new NoteInfoItem();
    Address address;

    EditText titleEdit;
    EditText contentEdit;
    TextView currentLength;

//    /**
//     * NoteInfoItem 객체를 인자로 저장하는
//     * NoteRegisterInputFragment 인스턴스를 생성해서 반환한다.
//     * @param infoItem 노트 정보를 저장하는 객체
//     * @return NoteRegisterInputFragment 인스턴스
//     */
    public static NoteUpdateInputFragment newInstance(NoteInfoItem infoItem) {
        System.out.println("등록 newinstance");
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_ITEM, Parcels.wrap(infoItem));

        NoteUpdateInputFragment fragment = new NoteUpdateInputFragment();
        fragment.setArguments(bundle);
//        System.out.println("instance ㅁㄴㅇㄻㄴㅇㄻㄴㅇㄻ"+fragment);
        return fragment;
    }

    /**
     * 프래그먼트가 생성될 때 호출되며 인자에 저장된 NoteInfoItem를
     * NoteRegisterActivity에 currentItem를 저장한다.
     * @param savedInstanceState 프래그먼트가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("등록 onCreate-----------");
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            infoItem = Parcels.unwrap(getArguments().getParcelable(INFO_ITEM));
            saveItem=infoItem;

            if (infoItem.seq != 0) {

                //selectinfonote
                NoteUpdateActivity.currentItem = infoItem;
            }
            MyLog.d(TAG, "infoItem asdfasdfasdf" + infoItem);
        }
    }

    /**
     * fragment_Note_register_input.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("등록 onCreateView");
        context = this.getActivity();
        View layout =
                inflater.inflate(R.layout.fragment_note_register_input,container,false);

        return layout;
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 노트 정보를 입력할 뷰들을 생성한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        System.out.println("등록 onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        currentLength = (TextView) view.findViewById(R.id.current_length);

        titleEdit = (EditText) view.findViewById(R.id.note_title);
        titleEdit.setText(saveItem.title);

        contentEdit = (EditText) view.findViewById(R.id.note_content);
        contentEdit.setText(saveItem.content);


        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentLength.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button nextButton = (Button) view.findViewById(R.id.next);
        Button deleteButton = (Button) view.findViewById(R.id.delete);
        nextButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private NoteInfoItem getNoteInfoItem(){
        System.out.println("등록 getNoteInfoItem");
        NoteInfoItem item = new NoteInfoItem();
        item.content=contentEdit.getText().toString();
        item.title=titleEdit.getText().toString();

        return item;
    }
    private  boolean isChanged(NoteInfoItem newItem){
        System.out.println("등록 is Changed");
        if(newItem.title.trim().equals(infoItem.title)
                && newItem.content.trim().equals(infoItem.content)){
            Log.d(TAG,"return "+false);
            return false;
        } else {
            return true;
        }
    }


    /**
     * 클릭이벤트를 처리한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        System.out.println("등록 onClick");
        infoItem.title = titleEdit.getText().toString();
        infoItem.content = contentEdit.getText().toString();
        MyLog.d(TAG, "onClick imageItem " + infoItem);

        if (v.getId() == R.id.next) {
            save();
        } else if (v.getId() == R.id.delete){
            DialogLib.getInstance().showNoteDeleteDialog(context,NoteDeleteHandler,infoItem.seq);
        }

    }

    /**
     * 사용자가 입력한 정보를 확인하고 저장한다.
     */
    private void save() {
        System.out.println("등록 save");
        if (StringLib.getInstance().isBlank(infoItem.title)) {
            MyToast.s(context, context.getResources().getString(R.string.input_note_title));
            return;
        }

        if (StringLib.getInstance().isBlank(infoItem.content)) {
            MyToast.s(context, context.getResources().getString(R.string.input_note_input_content));
            return;
        }

        insertNoteInfo();
    }

    /**
     * 사용자가 입력한 정보를 서버에 저장한다.
     * member추가할때 사용해도될듯
     */
    private void insertNoteInfo() {
        System.out.println("등록 insertNoteInfo");
        final NoteInfoItem newItem = getNoteInfoItem();

        if(!isChanged(newItem)){
            goNextPage();
        }

        MyLog.d(TAG, infoItem.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertNoteInfo(infoItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();

                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }

                    if (seq == 0) {
                        //등록 실패

                    } else {
                        infoItem.seq = seq;
                        infoItem.content=newItem.content;
                        infoItem.title=newItem.title;
                        goNextPage();
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    /**
     * 노트 이미지를 등록할 수 있는 프래그먼트로 이동한다.
     */
    private void goNextPage() {
        System.out.println("등록 goNextPage");
        GoLib.getInstance().goFragmentBack(getFragmentManager(),
                R.id.content_main, NoteUpdateImageFragment.newInstance(infoItem.seq));
    }

    private void selectNoteInfo(int noteInfoSeq,int memberSeq){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<NoteInfoItem> call = remoteService.selectNoteInfo(noteInfoSeq, memberSeq);

        call.enqueue(new Callback<NoteInfoItem>() {
            @Override
            public void onResponse(Call<NoteInfoItem> call, Response<NoteInfoItem> response) {
                NoteInfoItem infoItem = response.body();

                if (response.isSuccessful() && infoItem != null && infoItem.seq > 0) {
                    NoteUpdateActivity.currentItem = infoItem;
                    setView();
                }
            }

            @Override
            public void onFailure(Call<NoteInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void setView(){
        titleEdit.setText(NoteUpdateActivity.currentItem.title);
        contentEdit.setText(NoteUpdateActivity.currentItem.content);
    }


    Handler NoteDeleteHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        }
    };


}
