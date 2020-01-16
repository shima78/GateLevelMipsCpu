package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import java.util.Date;

public class Form {

  private ApiResponse apiResponse;
  private MongoClient mongo;

  public Form(MongoClient mongoClient) {
    this.mongo = mongoClient;
    this.apiResponse = new ApiResponse();
  }

  public void createForm(RoutingContext ctx) {
    String workshop_id = ctx.request().getParam("workshop_id");
    String owner_id = ctx.request().getParam("owner_id");//bayad avaz beshe
    JsonObject form = new JsonObject();
    form.put("workshop_id", workshop_id);
    form.put("time", new Date().getTime());
    form.put("maker", owner_id);
    mongo.save(Constants.FORM_COLLECTION, form, res -> {
      if (res.succeeded()) {
        this.apiResponse.respondSuccess(ctx, form);
      } else {
        this.apiResponse.respondInternalError(ctx, "failed");
      }
    });

  }

  public void addQuestionsToForm(RoutingContext ctx) {
    String form_id = ctx.request().getParam("form_id");
    mongo.find(Constants.FORM_COLLECTION, new JsonObject().put("_id", form_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject form = res.result().get(0);
        JsonArray questions = form.containsKey("questions") ? form.getJsonArray("questions") : new JsonArray();
        JsonObject question = ctx.getBodyAsJson();
        question.put("id", questions.getList().size());
        questions.add(question);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("questions", questions));
        mongo.updateCollection(Constants.FORM_COLLECTION, new JsonObject().put("_id", form_id), update, res1 -> {
          if (res1.succeeded()) {
            this.apiResponse.respondSuccess(ctx, form);
          } else {
            this.apiResponse.respondInternalError(ctx, "failed");
          }
        });
      } else {
        this.apiResponse.respondInternalError(ctx, "failed");
      }
    });

  }

  public void answerToForm(RoutingContext ctx) {
    String form_id = ctx.request().getParam("form_id");
    String user_id = ctx.request().getParam("user_id");
    JsonObject answersToform = new JsonObject();
    answersToform.put("form_id", form_id);
    answersToform.put("user_id", user_id);//bayad in dorost beshe
    answersToform.put("date", new Date().getTime());
    mongo.save(Constants.ANSWERS_COLLECTION, answersToform, res -> {
      if (res.succeeded()) {
        this.apiResponse.respondSuccess(ctx, answersToform);
      } else {
        this.apiResponse.respondInternalError(ctx, "failed");
      }
    });

  }

  public void answerToQuestion(RoutingContext ctx) {
    String answer_id=ctx.request().getParam("form_id");
    String question_id = ctx.getBodyAsJson().getString("id");
    String answer_data = ctx.getBodyAsJson().getString("answer");
    JsonObject answer_new = new JsonObject();
    answer_new.put("answer", answer_data);
    answer_new.put("question_id", question_id);
    mongo.find(Constants.ANSWERS_COLLECTION,new JsonObject().put("_id",answer_id),res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject answer=res.result().get(0);
        JsonArray answers= answer.containsKey("answers") ? answer.getJsonArray("answers") : new JsonArray();
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("answers", answers));
        mongo.updateCollection(Constants.ANSWERS_COLLECTION,new JsonObject().put("_id",answer_id),update,res1->{
          if(res1.succeeded()){
            this.apiResponse.respondSuccess(ctx,update);
          }
          else{
            this.apiResponse.respondInternalError(ctx,"failed");
          }
        });

      }
      else{
        this.apiResponse.respondNotFound(ctx,"this answer not found");
      }
    });

  }


}
