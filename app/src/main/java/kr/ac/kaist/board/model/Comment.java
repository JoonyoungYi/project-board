package kr.ac.kaist.board.model;

public class Comment {

    private String id;
    private String writer = "";
    private String writer_img_url = "";
    private String ts = "";
    private String content = "";

    private int plus = -1;
    private int minus = -1;

    /**
     * Init
     */

    public Comment() {

    }

    /**
     * Getter and Setter
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter_img_url() {
        return writer_img_url;
    }

    public void setWriter_img_url(String writer_img_url) {
        this.writer_img_url = writer_img_url;
    }

    public int getPlus() {
        return plus;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public int getMinus() {
        return minus;
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }

}
