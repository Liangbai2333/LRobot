package site.liangbai.lrobot.util.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * 一个Http GET 请求的类.
 * @author Liangbai
 */
public final class HttpRequest {
    private final URLConnection urlConnection;
    private String charset = "utf-8";

    public HttpRequest(String url) throws IOException {
        this.urlConnection = new URL(url).openConnection();
    }

    public HttpRequest(URL url) throws IOException {
        this.urlConnection = url.openConnection();
    }

    public HttpRequest(URLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    /**
     * 为这个Http设置属性.
     * @param key 属性键值.
     * @param value 值.
     */
    public void putRequestProperty(final String key, final String value) {
        this.urlConnection.setRequestProperty(key, value);
    }

    /**
     * 获取当前的编码集名称.
     * @return 编码集名称
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置当前的编码集名称.
     * @param charset 编码集名称.
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 发送一个请求并返回它输出的结果.
     * @return 返回结果.
     * @throws IOException 抛出IO异常.
     */
    public String sendRequestAndReturn() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName(getCharset())));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
