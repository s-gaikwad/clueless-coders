package com.clueless.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.clueless.service.ZohoServiceImpl;
import java.util.Optional;

public class FutureLeaveIntentHandler implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("FutureLeaveIntent"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    ZohoServiceImpl zohoService = new ZohoServiceImpl();
    String applyLeaveResponse = zohoService.applyLeaveForEmployeeTomorrow();
    String speechText = "Your leave has been applied for %s".formatted(applyLeaveResponse);
    return handlerInput.getResponseBuilder()
        .withSpeech(speechText)
        .withSimpleCard("Leave", speechText)
        .build();
  }
}
