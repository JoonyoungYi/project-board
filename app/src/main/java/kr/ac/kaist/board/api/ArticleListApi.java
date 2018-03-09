package kr.ac.kaist.board.api;

import android.app.Application;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.StringTokenizer;

import kr.ac.kaist.board.model.Article;
import kr.ac.kaist.board.model.Attachment;
import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.RequestManager;
import kr.ac.kaist.board.utils.TimeStampManager;

public class ArticleListApi extends ApiBase {

    private final static String TAG = "ArticleListApi";

    private int board_id = -1;
    private String slug = "";

    /**
     *
     */
    private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

    /**
     *
     */
    private ArrayList<Article> articles = null;

    /**
     * Init
     */
    public ArticleListApi(Application application, int board_id, String slug, int page) {

        this.board_id = board_id;
        this.slug = slug;
        Log.d(TAG, "board_id -> " + board_id);

        /**
         *
         */
        RequestManager requestManager = new RequestManager(slug, "GET", "http");
        requestManager.setBaseUrl(getBaseUrl(board_id));
        requestManager.addHeader("Cookie", getStringInPrefs(application, getCookieArgument(board_id), ""));
        requestManager.addBodyValue(getPageHeaderName(board_id), Integer.toString(page));
        if (board_id == Argument.ARG_BOARD_PORTAL_BOARD || board_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            requestManager.addBodyValue("format", "json");
        }
        requestManager.doRequest();
        String response = requestManager.getResponse_body();

        /**
         *
         */
        if (this.board_id == Argument.ARG_BOARD_PORTAL_BOARD || this.board_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            articles = getPortalArticles(application, response, slug);

        } else if (this.board_id == Argument.ARG_BOARD_ARA) {
            articles = getAraArticles(response);

        } else if (this.board_id == Argument.ARG_BOARD_BF) {
            articles = getBfArticles();
        }
    }

    /**
     * @param board_id
     * @return
     */
    private String getPageHeaderName(int board_id) {

        if (board_id == Argument.ARG_BOARD_PORTAL_BOARD || board_id == Argument.ARG_BOARD_PORTAL_NOTICE || board_id == Argument.ARG_BOARD_BF) {
            return ("page");
        } else if (board_id == Argument.ARG_BOARD_ARA) {
            return ("page_no");
        }

        return null;
    }

    /**
     *
     * @param response
     * @param slug
     * @return
     */
    private ArrayList<Article> getPortalArticles(Application application, String response, String slug) {

        ArrayList<Article> articles = new ArrayList<>();

        /**
         *
         */
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Article article = new Article();

                if (!obj.isNull("read_count")) {
                    int read_cnt = obj.getInt("read_count");
                    article.setRead_cnt(read_cnt);
                }

                if (!obj.isNull("id")) {
                    String id = obj.getString("id");
                    article.setId(id);
                }

                if (!obj.isNull("slug")) {

                    String detail_url = slug.substring(0, slug.length() - 1);
                    detail_url = detail_url.substring(0, detail_url.lastIndexOf("/") + 1);
                    detail_url = detail_url + obj.getString("slug") + "/" + article.getId();

                    article.setDetail_url(detail_url);
                } else {
                    String detail_url = slug + article.getId();
                    article.setDetail_url(detail_url);
                }

                if (!obj.isNull("title")) {
                    String title = obj.getString("title");
                    article.setTitle(title);
                }

                if (!obj.isNull("content")) {
                    String content = obj.getString("content");
                    article.setContent(content);
                }

                if (!obj.isNull("created_at")) {
                    String ts = obj.getString("created_at");
                    article.setTs(TimeStampManager.convertTimeFormat(ts));
                }

                if (!obj.isNull("attachments")) {
                    JSONArray attachmentArray = obj.getJSONArray("attachments");
                    ArrayList<Attachment> attachments = new ArrayList<Attachment>();
                    ArrayList<Attachment> imgs = new ArrayList<Attachment>();

                    for (int j = 0; j < attachmentArray.length(); j++) {

                        Attachment attachment = new Attachment();
                        JSONObject attachmentObj = attachmentArray.getJSONObject(j);

                        if (!attachmentObj.isNull("id")) {
                            String id = attachmentObj.getString("id");
                            attachment.setId(id);
                        }

                        if (!attachmentObj.isNull("filename")) {
                            String filename = attachmentObj.getString("filename");
                            attachment.setName(filename);


                            String ext = "";
                            if (filename.contains(".")) {
                                ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                            }

                            if (!ext.equals("")) {
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String mime_type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                if (mime_type == null) {
                                    if (ext.equals("hwp")) {
                                        mime_type = "application/x-hwp";
                                    } else if (ext.equals("mht")) {
                                        mime_type = "message/rfc822";
                                    } else {
                                        Log.e(TAG, "filename: " + filename);
                                    }
                                }
                                attachment.setMime_type(mime_type);
                                attachment.setIcon_res_id(filename);
                            }

                        }

                        if (!attachmentObj.isNull("download_path")) {
                            String download_path = "/board" + attachmentObj.getString("download_path");
                            attachment.setUrl(download_path);
                        }

                        if (attachment.getMime_type().contains("image")) {

                            boolean isDuplicated = false;
                            for (Attachment attachment_img : imgs) {
                                if (attachment_img.getName().equals(attachment.getName())) {
                                    isDuplicated = true;
                                    break;
                                }
                            }

                            if (!isDuplicated) {
                                imgs.add(attachment);
                            }

                        } else {
                            attachments.add(attachment);
                        }
                    }

                    if (imgs.size() > 1) {
                        for (int k = imgs.size() - 1; k > 0; k--) {
                            attachments.add(imgs.get(k));
                            imgs.remove(k);
                        }
                    }

                    article.setImgs(imgs);
                    article.setAttachments(attachments);
                }

                if (!obj.isNull("user")) {
                    JSONObject userObj = obj.getJSONObject("user");
                    if (!userObj.isNull("first_name")) {
                        String writer = userObj.getString("first_name");
                        article.setWriter_img_url(writer.substring(0, 1));
                        article.setWriter(writer);
                    }
                }

                articles.add(article);
            }

            request_code = Argument.REQUEST_CODE_SUCCESS;

        } catch (JSONException e) {
            e.printStackTrace();

            if (getStringInPrefs(application, Argument.PREFS_PORTAL_LOGIN, "false").equals("false")){
                request_code = Argument.REQUEST_CODE_NOT_LOGIN;
            } else {
                request_code = Argument.REQUEST_CODE_FAIL;
            }

            return null;
        }

        /**
         * Check There is data.
         */
        if (articles.size() == 0){
            request_code = Argument.REQUEST_CODE_NO_DATA;
            return null;
        }
        return articles;

    }

    /**
     *
     * @return
     */
    private ArrayList<Article> getAraArticles(String response) {

        Log.d(TAG, "getAraArticles() start!");

        ArrayList<Article> articles = new ArrayList<Article>();

        //
        try {
            Document doc = Jsoup.parse(response);
            Element list = doc.getElementsByClass("list").get(0);
            if (list != null) {
                Elements items = list.getElementsByTag("a");
                for (Element item : items) {

                    try {
                        Article article = new Article();

                        article.setTitle(getAraTitleFromElement(item));

                        String indicator = getAraIndicatorFromElement(item);

                        Element infoElement = item.getElementsByClass("listInfo").get(0);

                        String wall = "";
                        Elements wallElements = item.getElementsByTag("b");
                        if (wallElements.size() != 0) {
                            wall = wallElements.get(0).text();
                        }
                        Log.d(TAG, "WALL : " + wall);

                        String info = infoElement.text().replace(wall, "").replace(indicator, "").replaceAll("/", "|").replaceAll("추천", "").replaceAll("반대", "").replaceAll("조회:", "");
                        if (!wall.equals("")) {
                            info = info.substring(0, info.indexOf("|")) + info.substring(info.indexOf("|") + 1);
                            //Log.e(TAG, info);
                        }

                        StringTokenizer st = new StringTokenizer(info, "|");
                        String writer = getNextTokenTrimmed(st);
                        article.setWriter(writer);
                        article.setWriter_img_url(writer.substring(0, 1).toUpperCase());
                        //Log.d(TAG, writer);

                        String plus_str = getNextTokenTrimmed(st);
                        int plus = Integer.parseInt(plus_str);
                        article.setPlus(plus);
                        //Log.d(TAG, "" + plus);

                        String minus_str = getNextTokenTrimmed(st);
                        int minus = Integer.parseInt(minus_str);
                        article.setMinus(minus);
                        //Log.d(TAG, "" + minus);

                        String read_cnt_str = getNextTokenTrimmed(st);
                        int read_cnt = Integer.parseInt(read_cnt_str);
                        article.setRead_cnt(read_cnt);
                        //Log.d(TAG, "" + read_cnt);

                        String detail_url = item.attr("href");
                        article.setDetail_url(detail_url);
                        Log.d(TAG, "DETAIL URL : " + detail_url);


                        Elements commentElements = item.getElementsByClass("re_num");
                        int comment_cnt = -1;
                        if (commentElements.size() == 0) {
                            comment_cnt = 0;
                        } else {
                            String comment_cnt_str = commentElements.get(0).text().replace("(", "").replace(")", "").trim();
                            comment_cnt = Integer.parseInt(comment_cnt_str);
                            Log.d(TAG, "" + comment_cnt);
                        }
                        article.setComment_cnt(comment_cnt);

                        articles.add(article);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return articles;
    }

    private String getAraTitleFromElement(Element element) {
        Element titleElement = element.getElementsByClass("listTitle").get(0);
        String title = titleElement.text();
        //Log.d(TAG, title);

        return title;
    }

    private String getAraIndicatorFromElement(Element element) {
        Element indicatorElement = element.getElementsByClass("indicator").get(0);
        String indicator = indicatorElement.text();
        Log.d(TAG, "INDICATOR : " + indicator);
        return indicator;
    }

    /**
     * @return
     */
    private ArrayList<Article> getBfArticles() {

        ArrayList<Article> articles = new ArrayList<Article>();

        //
        try {
            Document doc = Jsoup.parse(response);

            Element tbodyElement = doc.getElementsByTag("tbody").get(0);
            Elements trElements = tbodyElement.getElementsByTag("tr");

            for (Element trElement : trElements) {

                try {
                    Article article = new Article();
                    Elements tdElements = trElement.getElementsByTag("td");

                    Element indicatorElement = tdElements.first();

                    Element writerElement = indicatorElement.nextElementSibling();
                    String writer = writerElement.text();
                    article.setWriter(writer);
                    //Log.d(TAG, writer);

                    Element titleCommentElement = writerElement.nextElementSibling();
                    Element titleElement = titleCommentElement.getElementsByClass("post-table-title-detail").get(0);
                    String title = titleElement.text();
                    article.setTitle(title);
                    //Log.d(TAG, title);

                    Element commentElement = titleCommentElement.getElementsByClass("comment-count").get(0);
                    String comment = commentElement.text().replaceAll("\\[", "").replaceAll("\\]", "").trim();
                    if (comment.equals("")) {
                        article.setComment_cnt(0);
                    } else {
                        article.setComment_cnt(Integer.parseInt(comment));
                    }
                    //Log.d(TAG, comment);

                    Element infoElement = titleCommentElement.nextElementSibling();
                    String info = infoElement.text();
                    //Log.d(TAG, info);

                    String plus = info.substring(info.indexOf("+") + 1, info.indexOf("-")).trim();
                    article.setPlus(Integer.parseInt(plus));
                    //Log.d(TAG, plus);

                    String minus = info.substring(info.indexOf("-") + 1, info.indexOf("/")).trim();
                    article.setMinus(Integer.parseInt(minus));
                    //Log.d(TAG, minus);

                    String read_cnt = info.substring(info.indexOf("/") + 1).trim();
                    article.setRead_cnt(Integer.parseInt(read_cnt));
                    //Log.d(TAG, read_cnt);

                    Element tsElement = infoElement.nextElementSibling().getElementsByClass("timeago").get(0);
                    String ts = TimeStampManager.convertTimeFormat(tsElement.attr("timestamp"), "yyyy-MM-dd'T'HH:mm:ssZZZZZ");
                    article.setTs(ts);
                    //Log.d(TAG, "TS : "+ts);

                    articles.add(article);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    /**
     * @return
     */
    public int getRequestCode() {


        if (board_id == Argument.ARG_BOARD_ARA) {
            return Argument.REQUEST_CODE_SUCCESS;

        } else if (board_id == Argument.ARG_BOARD_BF) {
            return Argument.REQUEST_CODE_SUCCESS;
        }

        return request_code;
    }

    /**
     * @return
     */
    public ArrayList<Article> getResult() {

        return articles;
    }

}
