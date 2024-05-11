package com.clueless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.FormEncodingBuilder;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ZohoServiceImpl {

  ObjectMapper mapper = new ObjectMapper();

  @Value("${app.zoho.client-id}")
  private String ZOHO_CLIENT_ID;
  @Value("${app.zoho.client-secret}")
  private String ZOHO_CLIENT_SECRET;

  @SneakyThrows
  public String generateAccessTokenUsingRefreshToken(){

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
        .url(
            "https://accounts.zoho.in/oauth/v2/token?refresh_token=1000.88f915916fb784716f5c56611048acc0.167cd075376bf86afd7a5542da325a87&client_id=1000.RJ0QY075W0P82QHUNG79WWPK7D0EEA&client_secret=1d77118b4c5b06f2f9d0da06196a61e64985a6bc35&grant_type=refresh_token")
        .method("POST", body)
        .build();
    Response response = client.newCall(request).execute();
    JsonNode node = mapper.readTree(Objects.requireNonNull(response.body()).string());
    String accessToken = node.get("access_token").asText();
    System.out.println(accessToken);
    return accessToken;
  }

  @SneakyThrows
  public String checkIn(){

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    val formBody = new FormBody.Builder()
        .add("dateFormat", "dd/MM/yyyy HH:mm:ss")
        .add("checkIn",simpleDateFormat.format(new Date()))
//        .add("checkOut", "09/05/2024 20:45:13")   // only checkout In
        .add("empId","3")
        .build();

    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/attendance")
        .method("POST", formBody)
        .addHeader("Authorization", "Zoho-oauthtoken " + "1000.bd38d1e32556667525e58696119c8b36.25c797d0421ff57a372f625b20182d5a")
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!!!!!";
    }
    return "Check-In Successfully...";

  }

  @SneakyThrows
  public String checkOut(){

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    val formBody = new FormBody.Builder()
        .add("dateFormat", "dd/MM/yyyy HH:mm:ss")
//        .add("checkIn",simpleDateFormat.format(new Date()))  // only checkout Out
        .add("checkOut", simpleDateFormat.format(new Date()))
        .add("empId","3")
        .build();

    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/attendance")
        .method("POST", formBody)
        .addHeader("Authorization", "Zoho-oauthtoken " + "1000.0fcf06722b38c00af173c174bfee0a45.dbb6d4b489ec401674d068b648bdfc9f")
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!!!!!";
    }
    return "Check-Out Successfully...";
  }

  @SuppressWarnings("deprecation")
  @SneakyThrows
  public String applyLeaveForEmployee() {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    RequestBody body = RequestBody.create(mediaType,
        "inputData={'Employee_ID':'161069000000256392','Leavetype':'161069000000254058','From':05-May-2024,'To':05-May-2024,'days':{'05-May-2024':{'LeaveCount':1, 'Session':2}}}");
    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/forms/json/leave/insertRecord?inputData=")
        .method("POST", body)
        .addHeader("Authorization",
            "Zoho-oauthtoken 1000.3a07713b64baacf91e928a8d20a84026.18a0e4c664efa09234d1f15aeba3fa3b")
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader("Cookie",
            "1b7c7929a1=55f6120f27055335daa474ce1d568d26; CSRF_TOKEN=c00548a8-4b3b-40b6-be64-85d87b562308; _zcsr_tmp=c00548a8-4b3b-40b6-be64-85d87b562308; _zpsid=5D2791167C6C8005DE57197248157D9F")
        .build();
    Response response = client.newCall(request).execute();
    if (response.code() != 200) {
      return "Unknown Error!!!!!";
    }
    return "Leave Applied Successfully...";

  }

  public static void main(String[] args) {
    ZohoServiceImpl zohoService = new ZohoServiceImpl();
    String accessToken = zohoService.generateAccessTokenUsingRefreshToken();
    System.out.println(accessToken);


//    zohoService.checkIn();
//    zohoService.checkOut();
//    zohoService.applyLeaveForEmployee();

  }
}
