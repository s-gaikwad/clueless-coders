package com.clueless.handlers;

import static com.amazon.ask.request.Predicates.intentName;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.clueless.service.ZohoServiceImpl;
import java.util.Optional;

public class CheckInIntentHandler implements RequestHandler {


  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("CheckInIntent"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    ZohoServiceImpl zohoService = new ZohoServiceImpl();
    String checkInResponse = zohoService.checkIn();
    String speechText = "Hey you have been checked in for the day!" + checkInResponse;
    return handlerInput.getResponseBuilder()
        .withSpeech(speechText)
        .withSimpleCard("HelloWorld", speechText)
        .build();
  }
}
