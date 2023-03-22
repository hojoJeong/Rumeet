package com.d204.rumeet.tools;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class OkhttpUtils {

    /**
     * post 호출
     * @param url
     * @param body
     * @param mediaType
     * @return
     */
    public String post(String url, Map<String, String> body, MediaType mediaType) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(new Gson().toJson(body), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * post 호출
     * @param url
     * @param body
     * @param mediaType
     * @return
     */
    public String post(String url, Map<String, String> paramHeader, Map<String, String> body, MediaType mediaType) {
        if (paramHeader.isEmpty()) {
            throw new IllegalArgumentException("paramHeader is null");
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(new Gson().toJson(body), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(Headers.of(paramHeader))
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * GET METHOD
     * @param url
     * @return
     */
    public String get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}