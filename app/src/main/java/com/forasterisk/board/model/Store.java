package com.forasterisk.board.model;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class Store {
    private String name;
    private String tel;

    /**
     *
     */
    public static Store newInstance(String name, String tel) {
        Store store = new Store();
        store.setName(name);
        store.setTel(tel);
        return store;
    }

    public static Store newInstance(String name) {
        return Store.newInstance(name, null);
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


}
