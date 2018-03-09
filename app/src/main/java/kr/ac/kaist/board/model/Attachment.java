package kr.ac.kaist.board.model;

import android.util.Log;
import android.webkit.MimeTypeMap;

import kr.ac.kaist.board.R;

/**
 * Created by yearnning on 2014. 8. 2..
 */
public class Attachment {
    private static final String TAG = "Attachment Model";

    /**
     *
     */
    private String id;

    private String url;
    private String name;

    private int icon_res_id = R.drawable.board_file_default;
    private String mime_type = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        //name = name.replaceAll("-", " ");
        //name = name.replaceAll("_", " ");
        this.name = name;
    }

    public int getIcon_res_id() {
        return icon_res_id;
    }

    public void setIcon_res_id(int icon_res_id) {
        this.icon_res_id = icon_res_id;

    }

    public void setIcon_res_id(String filename) {
        if (!filename.contains(".")) {
            return;
        }

        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if (this.getMime_type().contains("image")) {
            icon_res_id = R.drawable.board_file_img;

        } else if (ext.equals("hwp")) {
            icon_res_id = R.drawable.board_file_hwp;

        } else if (ext.equals("zip")) {

        } else if (ext.equals("pdf")) {
            icon_res_id = R.drawable.board_file_pdf;

        } else if (ext.equals("xls") || ext.equals("xlsx")) {
            icon_res_id = R.drawable.board_file_excel;

        } else if (ext.equals("docx") || ext.equals("doc")) {
            icon_res_id = R.drawable.board_file_word;

        } else if (ext.equals("pptx") || ext.equals("ppt")) {
            icon_res_id = R.drawable.board_file_ppt;

        } else if (ext.equals("avi") || ext.equals("swf")) {
            icon_res_id = R.drawable.board_file_video;

        } else if (ext.equals("rtf") || ext.equals("mht")) {
            icon_res_id = R.drawable.board_file_doc;

        } else if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("bmp")) {
            icon_res_id = R.drawable.board_file_img;
            //This flow is NOT necessary.

        } else {
            Log.e(TAG, ext);
        }
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

}
