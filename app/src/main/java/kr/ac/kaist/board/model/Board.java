package kr.ac.kaist.board.model;

import java.util.ArrayList;

public class Board {

    private int id = -1;

    private String title = "";
    private String explain = "";

    private ArrayList<Wall> walls = new ArrayList<Wall>();

    /**
     * Init
     */

    public Board() {

    }

    /**
     * Getter and Setter
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Board addWall(Wall wall) {
        this.walls.add(wall);
        return this;
    }

    public ArrayList<Wall> getWalls() {
        return this.walls;
    }
}
