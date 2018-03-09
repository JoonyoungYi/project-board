package com.forasterisk.board.api;

import java.util.Random;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class ComparisonAddApi {

    public ComparisonAddApi() {

        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResult() {

    }

}
