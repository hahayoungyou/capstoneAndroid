package com.example.androidcapstone;

import android.util.Log;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {

    /**
     * 멤버 관련
     */

    // 멤버 조회
    @POST("/authenticate")
    Call<AuthenticationResponse> SignIn(@Body UserDto userDto);

    // 토큰 보내고 멤버 가져오기
    @GET("/api/board/mobile/authenticate")
    Call<Username> getUsername();

    /**
     * 글 관련
     */

    // 글 조회
    @GET("/api/mobile/board")
    Call<List<BoardData>> getBoard(@Query("category") String category);

    // 글 등록
    @POST("/api/board")
    Call<BoardData> addPost(@Body BoardData boardData);

    // 글 수정
    @PUT("/api/board/{no}")
    Call<Void> updatePost(@Path("no") Integer no, @Body BoardData boardData);

    // 글 삭제
    @DELETE("/api/board/{no}")
    Call<Void> deletePost(@Path("no") int no);

    // 글 검색
    @GET("/api/board/search/{keyword}")
    Call<List<BoardData>> getSearchBoards(@Path(value = "keyword") String keyword);

    // hot 게시물 조회
    @GET("/api/board/hot")
    Call<List<BoardData>> getHotBoard();

    // 인기 태그 조회
    @GET("/api/board/ptag")
    Call<List> getPopularTag();


    /**
     * 댓글 관련
     */

    // 댓글 조회
    @GET("/api/board/comment/{num}")
    Call<List<CommentData>> getComment(@Path("num") Integer num);

    // 댓글 등록
    @POST("/api/board/comment")
    Call<CommentData> addComment(@Body CommentData commentData);

    // 댓글 수정
    @PUT("/api/board/comment/{no}")
    Call<Void> updateComment(@Path("no") Integer no, @Body CommentData commentData);

    // 댓글 삭제
    @DELETE("/api/board/comment/{no}")
    Call<Void> deleteComment(@Path("no") int no);


    /**
     * 지도 관련
     */

    // 지도
    @GET("/v2/local/search/keyword.json?{format}")
    Call<List<MapData>> getMap(@Query("format") String format);

}
