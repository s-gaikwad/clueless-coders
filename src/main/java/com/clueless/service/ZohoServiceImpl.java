package com.clueless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;


public class ZohoServiceImpl {

  public static final String REFRESH_TOKEN = "1000.88f915916fb784716f5c56611048acc0.167cd075376bf86afd7a5542da325a87";

  public static final String ACCESS_TOKEN ="1000.7bb9d3dd5bcfef8553e85094bf50be96.24eecee70306139706f5e2d19afa4b4d";

  ObjectMapper mapper = new ObjectMapper();
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


  @SneakyThrows
  public String generateAccessTokenUsingRefreshToken(){

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
        .url(
            "https://accounts.zoho.in/oauth/v2/token?refresh_token=" + REFRESH_TOKEN
                + "&client_id=1000.RJ0QY075W0P82QHUNG79WWPK7D0EEA&client_secret=1d77118b4c5b06f2f9d0da06196a61e64985a6bc35&grant_type=refresh_token")
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

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    String checkInTime = simpleDateFormat.format(new Date());
    val formBody = new FormBody.Builder()
        .add("dateFormat", "dd/MM/yyyy HH:mm:ss")
        .add("checkIn", checkInTime)
        .add("empId","2")
        .build();

    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/attendance")
        .method("POST", formBody)
        .addHeader("Authorization", "Zoho-oauthtoken " + ACCESS_TOKEN)
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!!!!!";
    }
    return " Checked-In At %s".formatted(checkInTime);

  }

  @SneakyThrows
  public String checkOut(){

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    String checkOutTime = simpleDateFormat.format(new Date());
    val formBody = new FormBody.Builder()
        .add("dateFormat", "dd/MM/yyyy HH:mm:ss")
        .add("checkOut", checkOutTime)
        .add("empId","2")
        .build();

    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/attendance")
        .method("POST", formBody)
        .addHeader("Authorization", "Zoho-oauthtoken " + ACCESS_TOKEN)
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!!!!!";
    }
    return " Check-Out %s".formatted(checkOutTime);
  }

  @SneakyThrows
  public String applyLeaveForEmployee() {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    RequestBody body = RequestBody.create(mediaType,
        "inputData={'Employee_ID':'161069000000256392','Leavetype':'161069000000254058','From':07-May-2024,'To':07-May-2024,'days':{'07-May-2024':{'LeaveCount':0.5, 'Session':2}}}");
    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/forms/json/leave/insertRecord?inputData=")
        .method("POST", body)
        .addHeader("Authorization",
            "Zoho-oauthtoken " + ACCESS_TOKEN)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader("Cookie",
            "1b7c7929a1=55f6120f27055335daa474ce1d568d26; CSRF_TOKEN=c00548a8-4b3b-40b6-be64-85d87b562308; _zcsr_tmp=c00548a8-4b3b-40b6-be64-85d87b562308; _zpsid=5D2791167C6C8005DE57197248157D9F")
        .build();
    Response response = client.newCall(request).execute();
    if (response.code() != 200) {
      return "Unknown Error!!!!!";
    }
    return " Leave Applied Successfully.";

  }

//  public static void main(String[] args) {
//    ZohoServiceImpl zohoService = new ZohoServiceImpl();
//    String accessToken = zohoService.generateAccessTokenUsingRefreshToken();
//    System.out.println(accessToken);


//    zohoService.checkIn();
//    zohoService.checkOut();
////    zohoService.applyLeaveForEmployee();
//
//  }
}
