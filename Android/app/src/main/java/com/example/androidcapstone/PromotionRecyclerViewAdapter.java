package com.example.androidcapstone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.example.androidcapstone.Login.token;

public class PromotionRecyclerViewAdapter extends RecyclerView.Adapter<PromotionRecyclerViewAdapter.ViewHolder>{
    // 홍보게시판 recyclerview 관리

    private Context c;
    private List<BoardData> dataList;

    JsonApi jsonApi;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.129:8080";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public PromotionRecyclerViewAdapter(Context c, List<BoardData> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public PromotionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewPro = LayoutInflater.from(c).inflate(R.layout.promotion_item, parent, false);
        return new ViewHolder(viewPro);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());

        String str = dataList.get(position).getBoard_date();
        String date = str.substring(0, str.indexOf("T"));
        String time = str.substring(11, str.indexOf("."));
        holder.promotion_date.setText(date + " " + time);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no;
        TextView title;
        TextView promotion_date;
        ImageView filepath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jsonApi = ServiceGenerator.createService(JsonApi.class, token);

            title = (TextView)itemView.findViewById(R.id.promotion_title);
            promotion_date = (TextView)itemView.findViewById(R.id.promotion_date);
            filepath = (ImageView)itemView.findViewById(R.id.promotionImage);

            // itme click 시 BoardDetail로 title, question, ..., filepath 모두 보내줌
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        // recyclerview의 선택된 item의 모든 것을 PromotionDetail로 넘겨주기
                        Intent intent = new Intent(v.getContext(), PromotionDetail.class);

                        intent.putExtra("category", dataList.get(pos).getCategory());
                        intent.putExtra("title", dataList.get(pos).getTitle());
                        intent.putExtra("question", dataList.get(pos).getQuestion());
                        intent.putExtra("board_id", dataList.get(pos).getBoard_id());
                        intent.putExtra("num", dataList.get(pos).getBoard_no());
                        intent.putExtra("filepath", dataList.get(pos).getFilepath());

                        String str = dataList.get(pos).getBoard_date();
                        String date = str.substring(0, str.indexOf("T"));
                        String time = str.substring(11, str.indexOf("."));
                        String datetime = date + " " + time;
                        intent.putExtra("datetime", datetime);
                        Log.i("datetime", datetime);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        v.getContext().startActivity(intent);

                    }
                }
            });
        }
    }
}
