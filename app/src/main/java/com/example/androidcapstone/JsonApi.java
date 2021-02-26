package com.example.androidcapstone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {
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
    @GET("/api/board/search/{keyword}/{searchType}")
    Call<List<BoardData>> getSearchBoards(@Path(value = "keyword") String keyword,
                                          @Path(value = "searchType") String searchType);



    /**
     * 댓글 관련
     */

    // 댓글 조회
    @GET("/api/board/comment/{num}")
    Call<List<CommentData>> getComment(@Path("num") Integer num);

    // 댓글 등록
    @POST("api/board/comment")
    Call<CommentData> addComment(@Body CommentData commentData);

    // 댓글 삭제
    @DELETE("/api/board/comment/{no}")
    Call<Void> deleteComment(@Path("no") int no);

    // 지도
    @GET("/v2/local/search/keyword.json?{format}")
    Call<List<MapData>> getMap(@Query("format") String format);

}
