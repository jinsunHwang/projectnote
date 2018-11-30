package com.mobitant.note;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitant.note.adapter.ShareListAdapter;
import com.mobitant.note.custom.EndlessRecyclerViewScrollListener;
import com.mobitant.note.item.NoteInfoItem;
import com.mobitant.note.lib.MyLog;
import com.mobitant.note.remote.RemoteService;
import com.mobitant.note.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteShareFragment extends Fragment implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    int memberSeq;

    RecyclerView noteList;
    TextView noDataText;

    TextView orderDefault;
    TextView orderShare;

    ImageView listType;

    ShareListAdapter shareListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 1;
    String orderType;


    public static NoteShareFragment newinstace() {
        NoteShareFragment f = new NoteShareFragment();
        System.out.println("프래그먼트 테스트 1");
        return f;
    }


    /**
     * fragment_note_list.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("프래그먼트 oncreateview");
        context = this.getActivity();

        memberSeq = ((MyApp)this.getActivity().getApplication()).getMemberSeq();

        View layout = inflater.inflate(R.layout.fragment_note_share, container, false);

        return layout;
    }

    /**
     * 프래그먼트가 일시 중지 상태가 되었다가 다시 보여질 때 호출된다.
     * NoteInfoActivity가 실행된 후,
     * 즐겨찾기 상태가 변경되었을 경우 이를 반영하는 용도로 사용한다.
     */
    @Override
    public void onResume() {
        System.out.println("프래그먼트 onresume");
        super.onResume();


        MyApp myApp = ((MyApp) getActivity().getApplication());
        NoteInfoItem currentInfoItem = myApp.getNoteInfoItem();

        if (shareListAdapter != null && currentInfoItem != null) {
            shareListAdapter.setItem(currentInfoItem);
            myApp.setNoteInfoItem(null);
        }
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 화면 뷰들을 설정한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        System.out.println("프래그먼트 onviewCreated");
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_keep);

        orderType = Constant.ORDER_TYPE_DEFAULT;

        noteList = (RecyclerView) view.findViewById(R.id.list);
        noDataText = (TextView) view.findViewById(R.id.no_data);
//        listType = (ImageView) view.findViewById(R.id.list_type);

//        orderDefault = (TextView) view.findViewById(R.id.order_default);
//        orderShare = (TextView) view.findViewById(R.id.order_share);
//
//
//        orderDefault.setOnClickListener(this);
//        orderShare.setOnClickListener(this);
//        listType.setOnClickListener(this);

        setRecyclerView();

        listInfo(orderType, 0);
    }

    /**
     * 맛집 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        System.out.println("프래그먼트 setLayout");
        layoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        noteList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        System.out.println("프래그먼트 setrecyclerview");
        setLayoutManager(listTypeValue);

        shareListAdapter = new ShareListAdapter(context,
                R.layout.row_note_share, new ArrayList<NoteInfoItem>());
        noteList.setAdapter(shareListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(orderType, page);
            }
        };
        noteList.addOnScrollListener(scrollListener);
    }

    /**
     * 서버에서 맛집 정보를 조회한다.
     * @param orderType 맛집 정보 정렬 순서
     * @param currentPage 현재 페이지
     */
    private void listInfo(String orderType, final int currentPage) {
        System.out.println("프래그먼트 테스트 listinfo");
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<NoteInfoItem>> call = remoteService.listShareInfo(orderType, currentPage);
        call.enqueue(new Callback<ArrayList<NoteInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<NoteInfoItem>> call,
                                   Response<ArrayList<NoteInfoItem>> response) {
                ArrayList<NoteInfoItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    shareListAdapter.addItemList(list);

                    if (shareListAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        shareListAdapter.setItemList(list);
                        noDataText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NoteInfoItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }


    /**
     * 각종 버튼에 대한 클릭 처리를 정의한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        System.out.println("프래그먼트 테스트 2");
//        if (v.getId() == R.id.list_type) {
//            changeListType();
//
//        } else {
//            if (v.getId() == R.id.order_default) {
//                orderType = Constant.ORDER_TYPE_DEFAULT;
//
//                setOrderTextColor(R.color.text_color_green,
//                        R.color.text_color_black, R.color.text_color_black);
//
//            } else if (v.getId() == R.id.order_share) {
//                orderType = Constant.ORDER_TYPE_SHARE;
//
//                setOrderTextColor(R.color.text_color_black,
//                        R.color.text_color_green, R.color.text_color_black);
//
//            }
//
//            setRecyclerView();
//            listInfo(memberSeq, orderType, 0);
//        }
    }

    private void setOrderTextColor(int color1, int color2, int color3) {
        System.out.println("프래그먼트 테스트 2");
        orderDefault.setTextColor(ContextCompat.getColor(context, color1));
        orderShare.setTextColor(ContextCompat.getColor(context, color2));
    }

    /**
     * 리사이클러뷰의 리스트 형태를 변경한다.
     */
    private void changeListType() {
        System.out.println("프래그먼트 테스트 2");
        if (listTypeValue == 1) {
            listTypeValue = 2;
            listType.setImageResource(R.drawable.ic_list2);
        } else {
            listTypeValue = 1;
            listType.setImageResource(R.drawable.ic_list);

        }
        setLayoutManager(listTypeValue);
    }

}
