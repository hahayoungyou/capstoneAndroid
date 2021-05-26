package com.example.androidcapstone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // 글 목록에 글 번호, 제목, 질문, 날짜+시각 보여주게 하는 리사이클러뷰

    private Context c;
    private List<BoardData> dataList;

    String datetime;

    public RecyclerViewAdapter(Context c, List<BoardData> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    public void search(String charText) {
        // recyclerview에 있는 내용 검색
        charText = charText.toLowerCase(Locale.getDefault());

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.goodnum.setText(dataList.get(position).getBoard_like().toString());
        holder.commentnum.setText(dataList.get(position).getCommentcount().toString());

        String str = dataList.get(position).getBoard_date();
        String date = str.substring(0, str.indexOf("T"));
        String time = str.substring(11, str.indexOf("."));

        holder.board_date.setText(date + " " + time);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView board_date;
        TextView goodnum;
        TextView commentnum;

        TextView tag1;
        TextView tag2;
        TextView tag3;
        TextView tag4;
        TextView tag5;

        ImageView filepath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            board_date = (TextView)itemView.findViewById(R.id.board_date);
            goodnum = (TextView)itemView.findViewById(R.id.goodnum);
            commentnum = (TextView)itemView.findViewById(R.id.commentnum);

            tag1 = (TextView)itemView.findViewById(R.id.tag1);
            tag2 = (TextView)itemView.findViewById(R.id.tag2);
            tag3 = (TextView)itemView.findViewById(R.id.tag3);
            tag4 = (TextView)itemView.findViewById(R.id.tag4);
            tag5 = (TextView)itemView.findViewById(R.id.tag5);

            filepath = (ImageView)itemView.findViewById(R.id.boardImage);

            // item click 시 ArticleDetail로 title, question, ..., tag 모두 보내줌
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        // recyclerview의 선택된 item의 모든 것을 ArticleDetail로 넘겨주기
                        Intent intent = new Intent(v.getContext(), ArticleDetail.class);

                        intent.putExtra("category", dataList.get(pos).getCategory());
                        intent.putExtra("title", dataList.get(pos).getTitle());
                        intent.putExtra("question", dataList.get(pos).getQuestion());
                        intent.putExtra("num", dataList.get(pos).getBoard_no());
                        intent.putExtra("board_id", dataList.get(pos).getBoard_id());
                        intent.putExtra("goodcount", dataList.get(pos).getBoard_like().toString());
                        intent.putExtra("commentcount", dataList.get(pos).getCommentcount().toString());

                        intent.putExtra("tag1", dataList.get(pos).getTag1());
                        intent.putExtra("tag2", dataList.get(pos).getTag2());
                        intent.putExtra("tag3", dataList.get(pos).getTag3());
                        intent.putExtra("tag4", dataList.get(pos).getTag4());
                        intent.putExtra("tag5", dataList.get(pos).getTag5());

                        intent.putExtra("filepath", dataList.get(pos).getFilepath());

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
