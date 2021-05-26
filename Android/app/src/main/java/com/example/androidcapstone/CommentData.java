package com.example.androidcapstone;

public class CommentData {
    Integer comment_no;
    String answer;
    String comment_date;
    Integer comment_like;
    String comment_id;
    Integer board_no;
    String board_id;

    public Integer getComment_no() { return comment_no; }

    public void setComment_no(Integer comment_no) { this.comment_no = comment_no; }

    public String getAnswer() { return answer; }

    public void setAnswer(String answer) { this.answer = answer; }

    public String getComment_date() { return comment_date; }

    public void setComment_date(String comment_date) { this.comment_date = comment_date; }

    public Integer getComment_like() { return comment_like; }

    public void setComment_like(Integer comment_like) { this.comment_like = comment_like; }

    public String getComment_id() { return comment_id; }

    public void setComment_id(String comment_id) { this.comment_id = comment_id; }

    public Integer getBoard_no() { return board_no; }

    public void setBoard_no(Integer board_no) { this.board_no = board_no; }

    public String getBoard_id() { return board_id; }

    public void setBoard_id(String board_id) { this.board_id = board_id; }

    @Override
    public String toString() {
        return "{" +
                "\"comment_no\":" + comment_no +
                ",\"answer\":\"" + answer + '\"' +
                ",\"comment_date\":\"" + comment_date + '\"' +
                ",\"comment_like\":\"" + comment_like + '\"' +
                ",\"comment_id\":" + comment_id +
                ",\"board_no\":\"" + board_no + '\"' +
                ",\"board_id\":\"" + board_id + '\"' +
                '}';
    }
}
