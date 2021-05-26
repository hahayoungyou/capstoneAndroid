package com.example.androidcapstone;

public class BoardData {
    Integer board_no;
    String title;
    String question;
    String board_date;
    Integer board_like;
    String category;
    String id;
    String tag1;
    String tag2;
    String tag3;
    String tag4;
    String tag5;
    Integer commentcount;
    String filepath;

    public Integer getBoard_no() {
        return board_no;
    }

    public void setBoard_no(Integer board_no) {
        this.board_no = board_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBoard_date() {
        return board_date;
    }

    public void setBoard_date(String board_date) {
        this.board_date = board_date;
    }

    public Integer getBoard_like() {
        return board_like;
    }

    public void setBoard_like(Integer board_like) {
        this.board_like = board_like;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBoard_id() {
        return id;
    }

    public void setBoard_id(String board_id) {
        this.id = id;
    }

    public String getTag1() { return tag1; }

    public void setTag1(String tag1) { this.tag1 = tag1; }

    public String getTag2() { return tag2; }

    public void setTag2(String tag2) { this.tag2 = tag2; }

    public String getTag3() { return tag3; }

    public void setTag3(String tag3) { this.tag3 = tag3; }

    public String getTag4() { return tag4; }

    public void setTag4(String tag4) { this.tag4 = tag4; }

    public String getTag5() { return tag5; }

    public void setTag5(String tag5) { this.tag5 = tag5; }

    public Integer getCommentcount() { return commentcount; }

    public void setCommentcount(Integer commentcount)  { this.commentcount = commentcount; }

    public String getFilepath() { return filepath; }

    public void setFilepath(String filepath) { this.filepath = filepath; }

    @Override
    public String toString() {
        return "{" +
                "\"board_no\":" + board_no +
                ",\"title\":\"" + title + '\"' +
                ",\"question\":\"" + question + '\"' +
                ",\"board_date\":\"" + board_date + '\"' +
                ",\"board_like\":" + board_like +
                ",\"category\":\"" + category + '\"' +
                ",\"id\":\"" + id + '\"' +
                ",\"tag1\":\"" + tag1 + "\"" +
                ",\"tag2\":\"" + tag2 + "\"" +
                ",\"tag3\":\"" + tag3 + "\"" +
                ",\"tag4\":\"" + tag4 + "\"" +
                ",\"tag5\":\"" + tag5 + "\"" +
                ",\"commentcount\":\"" + commentcount + "\"" +
                ",\"filepath\":\"" + filepath + "\"" +
                '}';
    }

}
