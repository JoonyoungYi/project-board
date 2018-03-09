package kr.ac.kaist.board.utils;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 프로토콜 컨트롤러는 철저히 스트링만 아웃풋으로 보내줍니다.
 *
 * @author yearnning
 */

public class RequestManager {
    private static final String TAG = "RequestManager";

    /**
     *
     */

    private String url;
    private String url_protocol = "http://";
    private String url_base = "portal.kaist.ac.kr";
    private String url_api = "";

    private String type = "http";
    private String method;

    /**
     *
     */

    private List<NameValuePair> params_body = new LinkedList<NameValuePair>();
    private List<NameValuePair> params_header = new LinkedList<NameValuePair>();

    private List<Cookie> cookies = null;

    /**
     *
     */
    private String response_header;
    private String response_body;

    /**
     * Init Request Controller
     */

    public RequestManager(String url_api, String method, String type) {

        /*

         */
        if (type.equals("http")) {

        } else if (type.equals("https")) {
            this.url_protocol = "https://";

        } else if (type.equals("httpa")) {

        }


        /*

         */
        //this.type = type;
        this.url_api = url_api;
        this.method = method;

    }

    /**
     * @param url_base
     */
    public void setBaseUrl(String url_base) {
        this.url_base = url_base;

    }

    /**
     * add value in Request Body.
     *
     * @param key
     * @param value
     */

    public void addBodyValue(String key, String value) {
        this.params_body.add(new BasicNameValuePair(key, value));
        Log.d(TAG, "addBodyValue():"+key + "," + value);

    }

    /**
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        this.params_header.add(new BasicNameValuePair(key, value));

    }

    /**
     *
     */
    public void doRequest() {

        this.url = url_protocol + url_base + url_api;


        BufferedReader br;
        StringBuffer sb = new StringBuffer();

        try {
            InputStream is = getInputStreamFromUrl(this.url);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                br = new BufferedReader(isr);

                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.response_body = sb.toString().trim();
        Log.d(TAG, this.response_body);


    }

    /**
     * @param url
     * @return
     */
    public InputStream getInputStreamFromUrl(String url) {
        InputStream contentStream = null;

        HttpResponse response = null;

        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

            //   HttpParams params = defaultHttpClient.getParams();
            //   HttpClientParams.setRedirecting(params, false);
            //   defaultHttpClient.setParams(params);

            /*

			 */
            if (type == "https")
                defaultHttpClient = sslClient(defaultHttpClient);

			/*
             * http GET POST method.
			 */
            if (method.equals("GET")) {
                String paramString = URLEncodedUtils.format(params_body, "utf-8");

                if (params_header.size() > 0) {
                    if (!url.contains("?"))
                        url += "?";
                    else
                        url += "&";
                }
                url += paramString;
                Log.d(TAG, "GET url: " + url);

                HttpGet httpGet = new HttpGet(url);

                for (NameValuePair pair : params_header) {
                    httpGet.addHeader(pair.getName(), pair.getValue());
                }

                response = defaultHttpClient.execute(httpGet);

            } else if (method.equals("POST")) {
                HttpPost httpPost = new HttpPost(url);
                Log.d(TAG, "POST url: " + url);

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params_body, HTTP.UTF_8);
                for (NameValuePair pair : params_header) {
                    httpPost.setHeader(pair.getName(), pair.getValue());
                }

                httpPost.setEntity(ent);

                for (Header header : httpPost.getAllHeaders()) {
                    Log.e(TAG, "REQUEST-HEADER " + header.getName() + ":" + header.getValue());
                }

                response = defaultHttpClient.execute(httpPost);

            }

            cookies = defaultHttpClient.getCookieStore().getCookies();

            contentStream = response.getEntity().getContent();


            /*
            for (Header header : response.getAllHeaders()) {
                Log.e(TAG, "HEADER " + header.getName() + ":" + header.getValue());
            }

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    Log.e(TAG, "COOKIE " + cookie.getName() + "=" + cookie.getValue() + ";");
                }
            }

            Log.e(TAG, response.getStatusLine().toString());

*/
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }

        return contentStream;
    }

    /**
     * @param client
     * @return
     */
    private static DefaultHttpClient sslClient(DefaultHttpClient client) {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                                               String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs,
                                               String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        public MySSLSocketFactory(SSLContext context)
                throws KeyManagementException, NoSuchAlgorithmException,
                KeyStoreException, UnrecoverableKeyException {
            super(null);
            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    /**
     * @return
     */
    public String getResponse_header() {
        return this.response_header;

    }

    /**
     * @return
     */
    public String getResponse_body() {
        return this.response_body;

    }

    /**
     * @return
     */
    public List<Cookie> getResponse_cookie() {

        return cookies;
    }

}
