package kr.ac.kaist.board.model;

import java.util.ArrayList;

public class Wall {

    private int board_id = -1;
    private String title = "";
    private String explain = "";
    public boolean isLoginWall = false;

    /**
     * Init
     */

    public Wall() {

    }

    /**
     * Getter and Setter
     */

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    private String slug = "";

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
