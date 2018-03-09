package kr.ac.kaist.board.api;

import android.app.Application;
import android.util.Log;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

import kr.ac.kaist.board.model.Article;
import kr.ac.kaist.board.model.Attachment;
import kr.ac.kaist.board.model.Comment;
import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.RequestManager;
import kr.ac.kaist.board.utils.TimeStampManager;

public class ArticleDetailApi extends ApiBase {
    private final static String TAG = "Article Detail Api";
    private JSONObject jsonObj = null;

    private int mBoard_id = -1;

    /**
     * Init
     */
    public ArticleDetailApi(Application application, int board_id, String detail_url) {

        this.mBoard_id = board_id;

        //
        RequestManager requestManager = new RequestManager(detail_url, "GET", "http");
        requestManager.setBaseUrl(getBaseUrl(board_id));
        requestManager.addHeader("Cookie", getStringInPrefs(application, getCookieArgument(board_id), ""));
        requestManager.doRequest();

        response = requestManager.getResponse_body();

        //
        if (board_id == Argument.ARG_BOARD_PORTAL_BOARD || board_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            requestManager = new RequestManager(detail_url + "/comments", "GET", "http");
            requestManager.addHeader("Cookie", getStringInPrefs(application, Argument.PREFS_PORTAL_COOKIE, ""));
            requestManager.doRequest();

            try {
                JSONObject responseObj = new JSONObject();
                responseObj.put("detail", new JSONObject(response));
                responseObj.put("comments", new JSONArray(requestManager.getResponse_body()));
                response = responseObj.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @return
     */
    public int getRequestCode() {

        if (mBoard_id == Argument.ARG_BOARD_PORTAL_BOARD || mBoard_id == Argument.ARG_BOARD_PORTAL_NOTICE) {

            try {
                jsonObj = new JSONObject(response);

            } catch (Exception e) {
                e.printStackTrace();
                return Argument.REQUEST_CODE_FAIL;
            }

            return Argument.REQUEST_CODE_SUCCESS;

        } else if (mBoard_id == Argument.ARG_BOARD_ARA) {
            return Argument.REQUEST_CODE_SUCCESS;
        }

        return Argument.REQUEST_CODE_UNEXPECTED;
    }


    /**
     * @return
     */
    public Article getResult() {

        if (mBoard_id == Argument.ARG_BOARD_PORTAL_BOARD || mBoard_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            return getResultPortal();
        } else if (mBoard_id == Argument.ARG_BOARD_ARA) {
            return getResultAra();
        }

        return null;
    }

    private Article getResultPortal() {
        Article article = new Article();

        try {

            JSONObject detailObject = jsonObj.getJSONObject("detail");

            if (!detailObject.isNull("read_count")) {
                int read_cnt = detailObject.getInt("read_count");
                article.setRead_cnt(read_cnt);
            }

            if (!detailObject.isNull("id")) {
                String id = detailObject.getString("id");
                article.setId(id);
            }

            if (!detailObject.isNull("title")) {
                String title = detailObject.getString("title");
                article.setTitle(title);
            }

            if (!detailObject.isNull("content")) {
                String content = detailObject.getString("content");
                article.setContent(content);

            }

            if (!detailObject.isNull("created_at")) {
                String ts = detailObject.getString("created_at");
                article.setTs(ts);
            }

            if (!detailObject.isNull("user")) {

                JSONObject userObj = detailObject.getJSONObject("user");

                if (!userObj.isNull("first_name")) {
                    String writer = userObj.getString("first_name");
                    article.setWriter(writer);
                    article.setWriter_img_url(writer.substring(0, 1).toUpperCase());
                }

            }

            JSONArray commentArray = jsonObj.getJSONArray("comments");
            ArrayList<Comment> comments = new ArrayList<Comment>();

            for (int i = 0; i < commentArray.length(); i++) {
                JSONObject obj = commentArray.getJSONObject(i);
                Comment comment = new Comment();

                if (!obj.isNull("id")) {
                    String id = obj.getString("id");
                    comment.setId(id);
                }

                if (!obj.isNull("content")) {
                    String content = obj.getString("content");
                    comment.setContent(content);
                }

                if (!obj.isNull("created_at")) {
                    String ts = obj.getString("created_at");
                    article.setTs(TimeStampManager.convertTimeFormat(ts));
                }

                if (!obj.isNull("user")) {

                    JSONObject userObj = obj.getJSONObject("user");

                    if (!userObj.isNull("first_name")) {
                        String writer = userObj.getString("first_name");
                        comment.setWriter(writer);
                        comment.setWriter_img_url(writer.substring(0, 1).toUpperCase());
                    }
                }

                comments.add(comment);
            }

            article.setComments(comments);

        } catch (JSONException e) {
            e.printStackTrace();
            article = null;
        }

        return article;
    }

    private Article getResultAra() {
        Article article = new Article();

        //
        try {
            Document doc = Jsoup.parse(response);

            Element bodyElement = doc.getElementsByTag("body").get(0);
            Element headElement = bodyElement.getElementById("article_head");

            article.setTitle(getTitleFromElement(headElement));

            StringTokenizer st = new StringTokenizer(getInfoFromElement(headElement), "|/");

            String writer = getNextTokenTrimmed(st);
            article.setWriter(writer);
            article.setWriter_img_url(writer.substring(0, 1).toUpperCase());

            String ts = getNextTokenTrimmed(st);
            article.setTs(TimeStampManager.convertTimeFormat(ts, "yyyy-MM-dd HH:mm"));

            String plus = getNextTokenTrimmed(st).replace("추천 ", "");
            article.setPlus(Integer.parseInt(plus));
            //Log.d(TAG, plus);

            String minus = getNextTokenTrimmed(st).replace("반대 ", "");
            article.setMinus(Integer.parseInt(minus));
            //Log.d(TAG, minus);

            String read_cnt = getNextTokenTrimmed(st).replace("조회 ", "");
            article.setRead_cnt(Integer.parseInt(read_cnt));
            //Log.d(TAG, read_cnt);

            String content = getContentFromElement(bodyElement);
            article.setContent(content);
            //Log.d(TAG, content);

            ArrayList<Comment> comments = new ArrayList<Comment>();
            Elements commentsElement = bodyElement.getElementById("reply").getElementsByTag("ul").get(0).getElementsByClass("re_list");
            for (Element commentElement : commentsElement){

                try {
                    Comment comment = new Comment();

                    comment.setContent(getCommentContentFromElement(commentElement));

                    st = new StringTokenizer(getCommentInfoFromElement(commentElement), "|");

                    writer = getNextTokenTrimmed(st);
                    comment.setWriter(writer);
                    comment.setWriter_img_url(writer.substring(0, 1).toUpperCase());
                    //Log.d(TAG, writer);

                    ts = getNextTokenTrimmed(st);
                    Log.d(TAG, ts);
                    comment.setTs(TimeStampManager.convertTimeFormat(ts, "yyyy-MM-dd HH:mm"));

                    plus = getNextTokenTrimmed(st);
                    comment.setPlus(Integer.parseInt(plus));

                    minus = getNextTokenTrimmed(st);
                    comment.setMinus(Integer.parseInt(minus));

                    comments.add(comment);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            article.setComments(comments);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return article;
    }

    private String getTitleFromElement(Element element) {
        Element titleElement = element.getElementsByClass("article_title").get(0);
        String title = titleElement.text();

        return title;
    }

    private String getInfoFromElement(Element element) {
        Element infoElement = element.getElementsByClass("article_info").get(0);
        String info = infoElement.text();
        return info;
    }

    private String getContentFromElement(Element element) {
        Element contentElement = element.getElementsByClass("article_contents").get(0);
        String content = contentElement.html();

        return content;
    }

    private String getCommentContentFromElement(Element element){
        Element contentElement = element.getElementsByClass("re_contents").get(0);
        String content = contentElement.html();
//        content = content.replaceAll(special, System.getProperty("line.separator"));
//        Log.d(TAG, content);

        return content;
    }

    private String getCommentInfoFromElement(Element element) {
        Element infoElement = element.getElementsByClass("left").get(0);
        String info = infoElement.html();
        Log.d(TAG, info);
        info = info.replaceAll("</span>", "|");
        Log.d(TAG, info);
        info = info.replaceAll("</a>", "");
        Log.d(TAG, info);
        info = info.replaceAll("/", "");
        Log.d(TAG, info);
        info = info.replaceAll("<a.*>", "|");
        Log.d(TAG, info);
        info = info.replaceAll("\\<.*?\\>" ,"");
        Log.d(TAG, info);
        info = info.replaceAll("▲", "");
        Log.d(TAG, info);
        info = info.replaceAll("▼", "");
        Log.d(TAG, info);
        info = info.replaceAll("\n", "");

        Log.d(TAG, info);
        return info;
    }
}
