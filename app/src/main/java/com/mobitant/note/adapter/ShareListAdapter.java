package com.mobitant.note.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitant.note.Constant;
import com.mobitant.note.MyApp;
import com.mobitant.note.R;
import com.mobitant.note.item.MemberInfoItem;
import com.mobitant.note.item.NoteInfoItem;
import com.mobitant.note.lib.DialogLib;
import com.mobitant.note.lib.GoLib;
import com.mobitant.note.lib.MyLog;
import com.mobitant.note.lib.StringLib;
import com.mobitant.note.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 맛집 정보 리스트의 아이템을 처리하는 어댑터
 */
public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<NoteInfoItem> itemList;
    private MemberInfoItem memberInfoItem;

    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public ShareListAdapter(Context context, int resource, ArrayList<NoteInfoItem> itemList) {
        System.out.println("쉐어 ShareListAdapter");
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;

        memberInfoItem = ((MyApp) context.getApplicationContext()).getMemberInfoItem();
    }

    public void setItemList(ArrayList<NoteInfoItem> itemList){
        System.out.println("쉐어 setItemList");
        this.itemList = itemList;
        notifyDataSetChanged();

    }

    /**
     * 특정 아이템의 변경사항을 적용하기 위해 기본 아이템을 새로운 아이템으로 변경한다.
     * @param newItem 새로운 아이템
     */
    public void setItem(NoteInfoItem newItem) {
        System.out.println("쉐어 setItem");
        for (int i=0; i < itemList.size(); i++) {
            NoteInfoItem item = itemList.get(i);

            if (item.seq == newItem.seq) {
                itemList.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<NoteInfoItem> itemList) {
        System.out.println("쉐어 addItemList");
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }


    /**
     * 아이템 크기를 반환한다.
     * @return 아이템 크기
     */
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    /**
     * 뷰홀더(ViewHolder)를 생성하기 위해 자동으로 호출된다.
     * @param parent 부모 뷰그룹
     * @param viewType 새로운 뷰의 뷰타입
     * @return 뷰홀더 객체
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("쉐어 onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(v);
    }

    /**
     * 뷰홀더(ViewHolder)와 아이템을 리스트 위치에 따라 연동한다.
     * @param holder 뷰홀더 객체
     * @param position 리스트 위치
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("쉐어 onBindViewHolder");
        final NoteInfoItem item = itemList.get(position);
        MyLog.d(TAG, "getView " + item);


        holder.title.setText(item.title);
        holder.content.setText(StringLib.getInstance().getSubString(context,
                item.content, Constant.MAX_LENGTH_DESCRIPTION));

        setImage(holder.image, item.imageFilename);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goNoteShareActivity(context, item.seq);
            }
        });

    }

    /**
     * 이미지를 설정한다.
     * @param imageView  이미지를 설정할 뷰
     * @param fileName 이미지 파일이름
     */
    private void setImage(ImageView imageView, String fileName) {
        System.out.println("쉐어 setImage");
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.bg_note_drawer).into(imageView);
        } else {
            Picasso.with(context).load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }


    /**
     * 아이템을 보여주기 위한 뷰홀더 클래스
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            System.out.println("쉐어 ViewHolder");
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
