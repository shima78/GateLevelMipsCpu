package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import javax.xml.bind.util.JAXBSource;
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
    mongo.save(Constants.FORM_COLLECTION, form, res1 -> {
      if (res1.succeeded()) {
        this.apiResponse.respondSuccess(ctx, form);
      } else {
        this.apiResponse.respondInternalError(ctx, "failed");
      }
    });


  }

  public void addUserToForm(RoutingContext ctx) {
    String form_id = ctx.request().getParam("form_id");
    String user_id = ctx.request().getParam("user_id");
    mongo.find(Constants.FORM_COLLECTION, new JsonObject().put("_id", form_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject form = res.result().get(0);
        JsonArray users = form.containsKey("users") ? form.getJsonArray("users") : new JsonArray();
        users.add(user_id);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("users", users));
        mongo.updateCollection(Constants.FORM_COLLECTION, new JsonObject().put("_id",form_id), update, res1 -> {
          if (res1.succeeded()) {
            this.apiResponse.respondSuccess(ctx, form);
          } else {
            this.apiResponse.respondInternalError(ctx, "failed");
          }
        });
      } else {
        this.apiResponse.respondNotFound(ctx, "form not found");
      }
    });
  }

  public void addGroupToForm(RoutingContext ctx) {
    String group_id = ctx.request().getParam("group_id");
    String form_id = ctx.request().getParam("form_id");
    mongo.find(Constants.FORM_COLLECTION, new JsonObject().put("_id", form_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject form = res.result().get(0);
        mongo.find(Constants.GROUP_COLLECTION, new JsonObject().put("_id", group_id), res1 -> {
          if (res1.succeeded() && !res1.result().isEmpty()) {
            JsonObject group = res1.result().get(0);
            JsonArray users = form.containsKey("users") ? form.getJsonArray("users") : new JsonArray();
            for (Object id_user : group.getJsonArray("parts").getList()) {
              users.add(id_user);
            }
            JsonObject update = new JsonObject().put("$set", new JsonObject().put("users", users));
            mongo.updateCollection(Constants.FORM_COLLECTION, new JsonObject().put("_id",form_id), update, res2 -> {
              if (res2.succeeded()) {
                this.apiResponse.respondSuccess(ctx, form);
              } else {
                this.apiResponse.respondInternalError(ctx, "failed");
              }
            });

          } else {
            this.apiResponse.respondInternalError(ctx, "this group not found");
          }
        });
      } else {
        this.apiResponse.respondInternalError(ctx, "this form not found");
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
    String answer_id = ctx.request().getParam("answer_id");
    String question_id = ctx.getBodyAsJson().getString("id");
    String answer_data = ctx.getBodyAsJson().getString("answer");
    JsonObject answer_new = new JsonObject();
    answer_new.put("answer", answer_data);
    answer_new.put("question_id", question_id);
    mongo.find(Constants.ANSWERS_COLLECTION, new JsonObject().put("_id", answer_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject answer = res.result().get(0);
        JsonArray answers = answer.containsKey("answers") ? answer.getJsonArray("answers") : new JsonArray();
        answers.add(answer_new);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("answers", answers));
        mongo.updateCollection(Constants.ANSWERS_COLLECTION, new JsonObject().put("_id", answer_id), update, res1 -> {
          if (res1.succeeded()) {
            this.apiResponse.respondSuccess(ctx, answer);
          } else {
            this.apiResponse.respondInternalError(ctx, "failed");
          }
        });
      }
     else{
      this.apiResponse.respondNotFound(ctx, "this answer not found");
    }
  });

}

  public void registerForm(RoutingContext ctx){
    JsonObject json =ctx.getBodyAsJson();
    String id_user =ctx.getBodyAsJson().getString("id_user");
    String id_workshop=ctx.getBodyAsJson().getString("id_workshop");
     JsonArray ghest=json.getJsonArray("ghest");
     for (int i=0 ; i <ghest.size() ; i++){
       ghest.getJsonObject(i).put("id",i);
     }
     JsonObject register_form=new JsonObject().put("id_user",id_user);
     register_form.put("id_workshop",id_workshop);
     register_form.put("ghest",ghest);
    mongo.save(Constants.REGISTER_COLLECTION,register_form,res->{
        if(res.succeeded()){
         mongo.find(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",json.getString("id_workshop")),find->{
            if(find.succeeded() && !find.result().isEmpty()){
              JsonObject workshop=find.result().get(0);
              JsonArray register = workshop.containsKey("register") ? workshop.getJsonArray("register") : new JsonArray();
              register.add(json.getString("id_user"));
              JsonObject update = new JsonObject().put("$set",new JsonObject().put("register",register));
              mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",workshop.getString("_id")),update,resup->{
                if(resup.succeeded()){
                  this.apiResponse.respondSuccess(ctx,json);
                }
              });
            }
            else{
              this.apiResponse.respondInternalError(ctx,"not found");
            }
          });

        }
        else {
          this.apiResponse.respondInternalError(ctx,"failed");
        }
      });
    }






}
