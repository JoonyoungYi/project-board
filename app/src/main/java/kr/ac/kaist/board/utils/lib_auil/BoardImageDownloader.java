package kr.ac.kaist.board.utils.lib_auil;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import kr.ac.kaist.board.api.ApiBase;
import kr.ac.kaist.board.utils.Argument;

/**
 * Created by yearnning on 2014. 8. 5..
 */
public class BoardImageDownloader extends BaseImageDownloader {

    public BoardImageDownloader(Context context) {
        super(context);
    }

    public BoardImageDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    @Override
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = super.createConnection(url, extra);

        Map<String, String> headers = (Map<String, String>) extra;
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
        }
        return conn;
    }
}