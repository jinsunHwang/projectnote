package com.mobitant.note;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitant.note.adapter.KeepListAdapter;
import com.mobitant.note.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.note.item.KeepItem;
import com.mobitant.note.item.NoteInfoItem;
import com.mobitant.note.remote.RemoteService;
import com.mobitant.note.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;

public class NoteKeepFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    int memberSeq;

    RecyclerView keepRecyclerView;
    TextView noDataText;

    /*  다름
    TextView orderDefault;
    TextView orderShare;

    ImageView listType;
    */

    KeepListAdapter keepListAdapter;

    //생략됨
//    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
//    int listTypeValue = 1;

    String orderType;

    //추가됨
    ArrayList<KeepItem> keepList = new ArrayList<>();

    public static NoteKeepFragment newInstance(){
        System.out.println("공유 newInstance");
        NoteKeepFragment f = new NoteKeepFragment();
        return f;

    }

    /**
     * fragment_bestfood_keep.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        System.out.println("공유 onCreateView");
        context = this.getActivity();

        memberSeq = ((MyApp)getActivity().getApplication()).getMemberSeq();

        View layout = inflater.inflate(R.layout.fragment_note_keep,container,false);

        return layout;
    }

    /**
     * 프래그먼트가 일시 중지 상태가 되었다가 다시 보여질 때 호출된다.
     * BestFoodInfoActivity가 실행된 후,
     * 공유 상태가 변경되었을 경우 이를 반영하는 용도로 사용한다.
     */
    @Override
    public void onResume() {
        super.onResume();

        MyApp myApp = ((MyApp)getActivity().getApplication());
        NoteInfoItem currentInfoItem = myApp.getNoteInfoItem();


        if(keepListAdapter != null && currentInfoItem != null) {
            keepListAdapter.setItem(currentInfoItem);
            myApp.setNoteInfoItem(null);

            //추가됨
            if(keepListAdapter.getItemCount() == 0){
                noDataText.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("공유 onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_keep);

        orderType = Constant.ORDER_TYPE_DEFAULT;

        keepRecyclerView = (RecyclerView) view.findViewById(R.id.keep_list);
        noDataText = (TextView) view.findViewById(R.id.no_keep);

//        setLayoutManager
        StaggeredGridLayoutManager layoutManager
                =new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        keepRecyclerView.setLayoutManager(layoutManager);

        //        setrecycleview , setlayoutmanager
        /*
        infoListAdapter = new InfoListAdapter(context,
                R.layout.row_note_list, new ArrayList<NoteInfoItem>());
        noteList.setAdapter(infoListAdapter);
        */
        keepListAdapter = new KeepListAdapter(context,
                R.layout.row_note_keep,keepList,memberSeq);
        keepRecyclerView.setAdapter(keepListAdapter);
        scrollListener= new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listKeep(memberSeq,orderType,page);
            }
        };
        keepRecyclerView.addOnScrollListener(scrollListener);

        listKeep(memberSeq,orderType,0);
    }

    /**
     * 서버에서 노트 정보를 조회한다.
     * @param memberSeq 사용자 시퀀스
     * @param orderType 노트 정보 정렬 순서
     * @param currentPage 현재 페이지
     */
    private void listKeep(int memberSeq,String orderType, int currentPage){
        System.out.println("공유 listKeep");
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);


    }



}
