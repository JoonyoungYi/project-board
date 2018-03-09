package kr.ac.kaist.board.model;

import java.util.ArrayList;

public class Article {

    private String id = "";

    private String detail_url = "";

    private String title = "";
    private String content = "";
    private String writer = "";
    private String writer_img_url = "";

    private ArrayList<Attachment> attachments = new ArrayList<Attachment>();
    private ArrayList<Attachment> imgs = new ArrayList<Attachment>();

    private String ts = null;
    private int plus = -1;
    private int minus = -1;
    private int read_cnt = -1;
    private int comment_cnt = -1;

    private ArrayList<Comment> comments = new ArrayList<Comment>();

    /**
     * Init
     */

    public Article() {

    }

    /**
     * Getter and Setter
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public int getRead_cnt() {
        return read_cnt;
    }

    public void setRead_cnt(int read_cnt) {
        this.read_cnt = read_cnt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getWriter_img_url() {
        return writer_img_url;
    }

    public void setWriter_img_url(String writer_img_url) {
        this.writer_img_url = writer_img_url;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<Attachment> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<Attachment> imgs) {
        this.imgs = imgs;
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

    public int getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(int comment_cnt) {
        this.comment_cnt = comment_cnt;
    }


}
