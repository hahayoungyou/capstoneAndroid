package com.example.androidcapstone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.board_no.setText(dataList.get(position).getBoard_no().toString());
        holder.title.setText(dataList.get(position).getTitle());
        //holder.board_date.setText(dataList.get(position).getBoard_date().toString());

        String str = dataList.get(position).getBoard_date().toString();
        String date = str.substring(0, str.indexOf("T"));
        String time = str.substring(12, str.indexOf("."));

        holder.board_date.setText(date + " " + time);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        TextView board_no;
        TextView title;
        TextView board_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            board_no = (TextView)itemView.findViewById(R.id.board_no);
            title = (TextView)itemView.findViewById(R.id.title);
            board_date = (TextView)itemView.findViewById(R.id.board_date);

            // item click 시 ArticleDetail로 title, question 보내줌
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
                        v.getContext().startActivity(intent);
                    }
                }
            });


        }

    }
}
