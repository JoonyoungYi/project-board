package com.forasterisk.board.model;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class Food {

    /**
     *
     */
    private String name;
    private String img_url;
    private Store store;
    private int price = -1;

    /**
     *
     */
    public static Food newInstance(String name, String img_url, int price, Store store) {
        Food food = new Food();
        food.setName(name);
        food.setImg_url(img_url);
        food.setPrice(price);
        food.setStore(store);
        return food;
    }

    public static Food newInstance(String name, String img_url, Store store) {
        return Food.newInstance(name, img_url, -1, store);
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
