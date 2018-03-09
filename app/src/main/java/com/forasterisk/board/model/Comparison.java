package com.forasterisk.board.model;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class Comparison {

    /**
     *
     */
    private String msg;
    private Food food_a;
    private Food food_b;

    /**
     *
     */
    public static Comparison newInstance(Food food_a, Food food_b, String msg) {
        Comparison comparison = new Comparison();
        comparison.setFood_a(food_a);
        comparison.setFood_b(food_b);
        comparison.setMsg(msg);
        return comparison;
    }


    /**
     *
     */

    public Food getFood_a() {
        return food_a;
    }

    public void setFood_a(Food food_a) {
        this.food_a = food_a;
    }

    public Food getFood_b() {
        return food_b;
    }

    public void setFood_b(Food food_b) {
        this.food_b = food_b;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
