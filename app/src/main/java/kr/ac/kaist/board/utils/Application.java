package kr.ac.kaist.board.utils;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.api.ApiBase;
import kr.ac.kaist.board.api.BoardInitApi;
import kr.ac.kaist.board.model.Board;
import kr.ac.kaist.board.utils.lib_auil.BoardImageDownloader;

/**
 * Created by yearnning on 2014. 8. 1..
 * This is singletone.
 */
public class Application extends android.app.Application {
    private static final String TAG = "Application";
    /**
     *
     */
    public DisplayImageOptions options;
    public ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     *
     */
    private static ArrayList<Board> boards = new ArrayList<Board>();

    /**
     * Getter and Setter
     */
    public void clearBoard() {
        this.boards.clear();
    }

    public void addBoard(Board board) {
        boards.add(board);

        Log.d(TAG, "board size=" + boards.size());
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public Board findBoardById(int id) {
        for (Board board : boards) {
            if (board.getId() == id)
                return board;
        }
        return null;
    }

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();

        /**
         *
         */

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(new BoardImageDownloader(this))
                .build();
        imageLoader.init(config);

        BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
        resizeOptions.inScaled = true;

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", ApiBase.getStringInPrefs(this, Argument.PREFS_PORTAL_COOKIE, ""));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.board_card_image_container_bg)
                .showImageForEmptyUri(R.color.board_card_image_container_bg)
                .showImageOnFail(android.R.color.holo_green_light)
                .extraForDownloader(headers)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .delayBeforeLoading(1000)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .considerExifParams(true)
                .decodingOptions(resizeOptions)
                .resetViewBeforeLoading(true)
                .build();


        /**
         *
         */
        BoardInitApi boardInitApi = new BoardInitApi();
        boards.clear();
        boards.addAll(boardInitApi.getResult());
        Log.d(TAG, "boards size -> "+boards.size());
    }

    /**
     *
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     *
     */
    public void onTerminate() {
        super.onTerminate();
    }
}
