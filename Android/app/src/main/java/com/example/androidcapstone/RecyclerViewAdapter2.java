package com.example.androidcapstone;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.androidcapstone.Login.token;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {
    // 댓글 recyclerview 관리

    private Context c;
    private List<CommentData> dataList;

    Button buttonCommentDelete;
    Button buttonCommentLike;

    Retrofit retrofit;
    JsonApi jsonApi;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.129:8080";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static int num;
    int comment_no;
    String comment_id;
    int comment_like;

    public RecyclerViewAdapter2(Context c, List<CommentData> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(c).inflate(R.layout.list_item2, parent, false);
        return new ViewHolder(view2);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter2.ViewHolder holder, int position) {

        holder.answer.setText(dataList.get(position).getAnswer());
        holder.comment_id.setText(" " + dataList.get(position).getComment_id());
        holder.comment_like_count.setText(String.valueOf(dataList.get(position).getComment_like()));

        String str = dataList.get(position).getComment_date().toString();
        String date = str.substring(0, str.indexOf("T"));
        String time = str.substring(11, str.indexOf("."));
        holder.comment_date.setText(date + " " + time);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView answer;
        TextView comment_date;
        TextView comment_id;
        TextView comment_like_count;

        String username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jsonApi = ServiceGenerator.createService(JsonApi.class, token);

            username = loadUsername();

            answer = (TextView)itemView.findViewById(R.id.answer);
            comment_date = (TextView)itemView.findViewById(R.id.comment_date);
            comment_id = (TextView)itemView.findViewById(R.id.comment_id);
            comment_like_count = (TextView)itemView.findViewById(R.id.comment_like_count);

            // 댓글 삭제 버튼 기능
            buttonCommentDelete = (Button)itemView.findViewById(R.id.comment_delete);
            buttonCommentDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : process click event.
                    String str1 = comment_id.getText().toString();
                    String id = str1.substring(str1.indexOf(" ") +1);
                    Log.i("id", id);

                    // login된 username이랑 댓글 작성자의 id랑 같다면 삭제 가능
                    if(username.equals(id)) {
                        comment_no = dataList.get(getAdapterPosition()).getComment_no();
                        Log.i("comment_no", String.valueOf(comment_no));
                        Call<Void> calls = jsonApi.deleteComment(comment_no);
                        calls.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (!response.isSuccessful()) {
                                    Log.i("board delete", "comment_no=" + comment_no);

                                    //1번누르면 DB에서 삭제,2번 누르면 datalist에서 삭제
                                    dataList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), getItemCount());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.i("board delete fail", String.valueOf(num));
                            }

                        });
                    } else {
                        Log.d("Can't commentDelete - username & id", username + " & " + id);
                    }
                }
            });

            // 좋아요 버튼 누르면 숫자 1 올라가도록.
            buttonCommentLike = (Button)itemView.findViewById(R.id.comment_like);
            buttonCommentLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 댓글 수정 이용
                    CommentData cd = new CommentData();
                    cd.comment_like = dataList.get(getAdapterPosition()).getComment_like();
                    cd.comment_like++;
                    updateComment(cd);

                }
            });
        }

        private void updateComment(CommentData cd) {
            comment_no = dataList.get(getAdapterPosition()).getComment_no();
            Call<Void> calls = jsonApi.updateComment(comment_no, cd);
            calls.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("Like - Comment_no", String.valueOf(comment_no));
                    Toast.makeText(c, "Like updated successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.i("comment like fail", String.valueOf(num));
                }
            });
        }

        // token 보내고 username 받아오기
        private String loadUsername(){
            Callback<Username> call = new Callback<Username>(){
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Username> call, Response<Username> response) {
                    if(response.isSuccessful()) {
                        Log.i("PromotionDetail - username", response.body().getName());
                        username = response.body().getName();
                    } else {
                        Log.e("PromotionDetail - getUsername", "Status Code " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<Username> call, Throwable t) {
                    Log.e("PromotionDetail - getUsername fail", t.getMessage());
                }
            };
            jsonApi.getUsername().enqueue(call);

            return username;
        }

    }

}
