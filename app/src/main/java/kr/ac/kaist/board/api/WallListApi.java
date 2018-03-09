package kr.ac.kaist.board.api;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ac.kaist.board.model.Article;
import kr.ac.kaist.board.model.Wall;
import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.RequestManager;

public class WallListApi extends ApiBase {
    private final static String TAG = "Article List Api";
    private JSONArray jsonArray = null;

    /**
     * Init
     */
    public WallListApi(Application application) {

    }

    /**
     * @return
     */
    public int getRequestCode() {

        return Argument.REQUEST_CODE_SUCCESS;
    }


    /**
     * @return
     */
    public ArrayList<Wall> getResult() {
        ArrayList<Wall> walls = new ArrayList<Wall>();



        return walls;
    }
}
