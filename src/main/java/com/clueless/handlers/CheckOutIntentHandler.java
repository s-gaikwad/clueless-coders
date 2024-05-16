package com.clueless.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.clueless.service.ZohoServiceImpl;
import java.util.Optional;

public class CheckOutIntentHandler implements RequestHandler {


  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("CheckOutIntent"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    ZohoServiceImpl zohoService = new ZohoServiceImpl();
    String checkOutResponse = zohoService.checkOut();
    String speechText = "You have been checked out for the day." + checkOutResponse;
    return handlerInput.getResponseBuilder()
        .withSpeech(speechText)
        .withSimpleCard("CheckOut", speechText)
        .build();
  }
}
