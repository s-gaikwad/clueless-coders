package com.clueless.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazonaws.util.StringUtils;
import com.clueless.service.ZohoServiceImpl;
import java.util.Map;
import java.util.Optional;

public class TimesheetIntentHandler implements RequestHandler {

  private static final String TASK_KEY = "Task";
  private static final String TICKET_KEY = "Ticket";

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("FillTimesheetIntent"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    Request request = handlerInput.getRequestEnvelope().getRequest();
    IntentRequest intentRequest = (IntentRequest) request;
    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();

    Slot task = slots.get(TASK_KEY);
    Slot ticket = slots.get(TICKET_KEY);

    String taskValue = task.getValue();
    String ticketValue = ticket.getValue();

    String speechText;
    boolean noAnswerProvided = false;

    if (!StringUtils.isNullOrEmpty(taskValue) || !StringUtils.isNullOrEmpty(ticketValue)) {
      speechText = String.format("Today I worked on %s for %s.", ticketValue, taskValue);
      ZohoServiceImpl zohoService = new ZohoServiceImpl();
      zohoService.addTimeLog(speechText);
    } else {
      speechText = "Please describe proper summary.";
      noAnswerProvided = true;
    }
    return handlerInput.getResponseBuilder()
        .withSimpleCard("Timesheet", speechText)
        .withSpeech(speechText)
        .withShouldEndSession(!noAnswerProvided)
        .build();
  }
}
