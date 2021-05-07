package com.example.androidcapstone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HotRecyclerViewAdapter extends RecyclerView.Adapter<HotRecyclerViewAdapter.ViewHolder>{
    // hot 게시글 recyclerview 관리

    private Context c;
    private List<BoardData> dataList;

    public HotRecyclerViewAdapter(Context c, List<BoardData> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public HotRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View hotView = LayoutInflater.from(c).inflate(R.layout.hot_list_item, parent, false);
        return new ViewHolder(hotView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.hotBoard_no.setText(dataList.get(position).getBoard_no().toString());
        holder.hotTitle.setText(dataList.get(position).getTitle());
        holder.hotGoodnum.setText(dataList.get(position).getBoard_like().toString());
        holder.hotCommentnum.setText(dataList.get(position).getCommentcount().toString());

        String str = dataList.get(position).getBoard_date();
        String date = str.substring(0, str.indexOf("T"));
        String time = str.substring(11, str.indexOf("."));

        holder.hotBoard_date.setText(date + " " + time);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hotBoard_no;
        TextView hotTitle;
        TextView hotBoard_date;
        TextView hotGoodnum;
        TextView hotCommentnum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotBoard_no = (TextView)itemView.findViewById(R.id.hotBoard_no);
            hotTitle = (TextView)itemView.findViewById(R.id.hotTitle);
            hotBoard_date = (TextView)itemView.findViewById(R.id.hotBoard_date);
            hotGoodnum = (TextView)itemView.findViewById(R.id.hotGoodnum);
            hotCommentnum = (TextView)itemView.findViewById(R.id.hotCommentnum);


            // item click 시 ArticleDetail로 title, question, ..., tag 모두 보내줌
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), ArticleDetail.class);

                        intent.putExtra("title", dataList.get(pos).getTitle());
                        intent.putExtra("question", dataList.get(pos).getQuestion());
                        intent.putExtra("num", dataList.get(pos).getBoard_no());
                        intent.putExtra("board_id", dataList.get(pos).getBoard_id());
                        intent.putExtra("board_id", dataList.get(pos).getBoard_id());
                        intent.putExtra("goodcount", dataList.get(pos).getBoard_like().toString());
                        intent.putExtra("commentcount", dataList.get(pos).getCommentcount().toString());

                        intent.putExtra("tag1", dataList.get(pos).getTag1());
                        intent.putExtra("tag2", dataList.get(pos).getTag2());
                        intent.putExtra("tag3", dataList.get(pos).getTag3());
                        intent.putExtra("tag4", dataList.get(pos).getTag4());
                        intent.putExtra("tag5", dataList.get(pos).getTag5());

                        String str = dataList.get(pos).getBoard_date();
                        String date = str.substring(0, str.indexOf("T"));
                        String time = str.substring(11, str.indexOf("."));
                        String datetime = date + " " + time;
                        intent.putExtra("datetime", datetime);
                        Log.i("datetime", datetime);

                        v.getContext().startActivity(intent);

                    }
                }
            });
        }
    }
}
