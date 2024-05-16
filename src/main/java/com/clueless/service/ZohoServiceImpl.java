package com.clueless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

  public static final String REFRESH_TOKEN = "1000.caa659019061d9664d5b0665aaaa7973.462e982ad1c34d7cfff5376f558fee3c";

//  public static final String ACCESS_TOKEN = "1000.9f9d8f7399adcb84eab1a2c6c6f3ba66.9d74dc012c70b4574dd9be0381249a6b";

  ObjectMapper mapper = new ObjectMapper();
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


  @SneakyThrows
  public String generateAccessTokenUsingRefreshToken() {

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
  public String checkIn() {
    String accessToken = generateAccessTokenUsingRefreshToken();
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    String checkInTime = simpleDateFormat.format(new Date());
    val formBody = new FormBody.Builder()
        .add("dateFormat", "dd/MM/yyyy HH:mm:ss")
        .add("checkIn", checkInTime)
        .add("empId", "2")
        .build();

    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/attendance")
        .method("POST", formBody)
        .addHeader("Authorization", "Zoho-oauthtoken " + accessToken)
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!";
    }
    return " Checked-In at %s".formatted(checkInTime);

  }

  @SneakyThrows
  public String checkOut() {
    String accessToken = generateAccessTokenUsingRefreshToken();

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    String checkOutTime = simpleDateFormat.format(new Date());
    val formBody = new FormBody.Builder()
        .add("dateFormat", "dd/MM/yyyy HH:mm:ss")
        .add("checkOut", checkOutTime)
        .add("empId", "2")
        .build();

    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/attendance")
        .method("POST", formBody)
        .addHeader("Authorization", "Zoho-oauthtoken " + accessToken)
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!";
    }
    return " Checked Out at %s".formatted(checkOutTime);
  }

  @SneakyThrows
  public String applyLeaveForEmployee() {
    String accessToken = generateAccessTokenUsingRefreshToken();
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-YYYY");
    String todayDate = simpleDateFormat.format(new Date());
    RequestBody body = RequestBody.create(mediaType,
        "inputData={'Employee_ID':'161069000000256392','Leavetype':'161069000000254058','From':%s,'To':%s,'days':{'%s':{'LeaveCount':1, 'Session':2}}}".formatted(
            todayDate, todayDate, todayDate));
    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/forms/json/leave/insertRecord?inputData=")
        .method("POST", body)
        .addHeader("Authorization",
            "Zoho-oauthtoken " + accessToken)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!";
    }
    return todayDate;

  }

  @SneakyThrows
  public String applyLeaveForEmployeeTomorrow() {
    String accessToken = generateAccessTokenUsingRefreshToken();
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    String tomorrowDate = simpleDateFormat.format(calendar.getTime());

    RequestBody body = RequestBody.create(mediaType,
        "inputData={'Employee_ID':'161069000000256392','Leavetype':'161069000000254058','From':"
            + tomorrowDate + ",'To':" + tomorrowDate + ",'days':{'" + tomorrowDate
            + "':{'LeaveCount':1.0, 'Session':2}}}");
    Request request = new Request.Builder()
        .url("https://people.zoho.in/people/api/forms/json/leave/insertRecord?inputData=")
        .method("POST", body)
        .addHeader("Authorization",
            "Zoho-oauthtoken " + accessToken)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader("Cookie",
            "1b7c7929a1=55f6120f27055335daa474ce1d568d26; CSRF_TOKEN=c00548a8-4b3b-40b6-be64-85d87b562308; _zcsr_tmp=c00548a8-4b3b-40b6-be64-85d87b562308; _zpsid=5D2791167C6C8005DE57197248157D9F")
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!";
    }
    return tomorrowDate;
  }

  @SneakyThrows
  public String addTimeLog(String description) {
    String accessToken = generateAccessTokenUsingRefreshToken();
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
        .url(
            "https://people.zoho.in/people/api/timetracker/addtimelog?user=shashankchhapanimohan%40gmail.com&jobName=Testing20samples123&workDate=2024-05-16&billingStatus=Billable&hours=08&description="
                + description)
        .method("POST", body)
        .addHeader("Authorization", "Zoho-oauthtoken " + accessToken)
        .addHeader("Cookie",
            "1b7c7929a1=03bb32d0a3548e55cf840f4698772ae8; 1b7c7929a1=03bb32d0a3548e55cf840f4698772ae8")
        .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
    if (response.code() != 200) {
      return "Unknown Error!";
    }
    return " Time log added Successfully %s".formatted(description);
  }

//  public static void main(String[] args) {
//
//    ZohoServiceImpl zohoService = new ZohoServiceImpl();
//    String accessToken = zohoService.generateAccessTokenUsingRefreshToken();
//    System.out.println(accessToken);

//    zohoService.checkIn();
//    zohoService.checkOut();
//    zohoService.applyLeaveForEmployee();
//    zohoService.applyLeaveForEmployeeTomorrow();
//    zohoService.addTimeLog("PLAT-3000");
//
//  }
}
