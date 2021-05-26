package com.example.androidcapstone;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ArticleMenuFragment extends Fragment {
    // 홈 화면에서 다루어주는 메뉴 목록

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_article_menu, container, false);

        String[] values = {"자유게시판", "소아과", "내과", "정형외과", "신경외과", "이비인후과", "한방과",
                "안과", "치과", "피부과", "산부인과", "비뇨기과", "성형외과", "홍보게시판" };

        ListView listView = (ListView)rootView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(values[(int)id] == "홍보게시판") {
                    Intent intent = new Intent(getContext(), PromotionBoard.class);
                    intent.putExtra("values", values[(int) id]); // "홍보게시판"이라는 글자 넘겨주기
                    startActivity(intent);
                }
                else {
                    Intent intent2 = new Intent(getContext(), ArticleBoard.class);
                    intent2.putExtra("values", values[(int) id]); // 게시판 카테고리 넘겨주기
                    startActivity(intent2);
                }
            }
        });

        return rootView;
    }


}