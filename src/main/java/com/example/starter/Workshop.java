package com.example.starter;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

public class Workshop {

  private MongoClient mongo;
  private ApiResponse apiResponse;

  public Workshop(MongoClient mongo) {

    this.apiResponse = new ApiResponse();
    this.mongo = mongo;
  }

  public static String generateString() {
    return UUID.randomUUID().toString();
  }


  public void addWorkshop(RoutingContext ctx) {

    JsonObject json = ctx.getBodyAsJson();
    mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("_id", json.getString("owner")), findOwner -> {
      if (findOwner.succeeded() && !findOwner.result().isEmpty()) {
        JsonObject owner =findOwner.result().get(0);
        mongo.save(Constants.WORKSHOPS_COLLECTION, json.put("is_deleted", false), saveRes -> {
          if (saveRes.succeeded()) {
            JsonObject workshop =new JsonObject().put("role","owner");
            workshop.put("workshop_id",json.getString("_id"));
            JsonArray workshops = owner.containsKey("workshops") ? owner.getJsonArray("workshops") : new JsonArray();
            workshops.add(workshop);
            JsonObject update_user=new JsonObject().put("$set",new JsonObject().put("workshops",workshops));
            mongo.updateCollection(Constants.USERS_COLLECTION,new JsonObject().put("_id",owner.getString("_id")),update_user,res2->{
              if(res2.succeeded()){
                this.apiResponse.respondSuccess(ctx, json);
              }
              else {
                this.apiResponse.respondInternalError(ctx,"failed");
              }
            });
          } else if (saveRes.failed()) {
            this.apiResponse.respondInternalError(ctx, saveRes.cause().getMessage());
          }
        });
      } else {
        this.apiResponse.respondNotFound(ctx, "Owner not found");
      }
    });
  }


  public void ListWorkshops(RoutingContext ctx) {
    mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("is_deleted", false), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        this.apiResponse.respondSuccessCollection(ctx, res.result());
      } else {
        this.apiResponse.respondNotFound(ctx, "no workshop");
      }
    });
  }


  public void getWorkshop(RoutingContext ctx) {

    String Id = ctx.request().getParam("id");
    JsonObject workshop = new JsonObject().put("_id", Id);
    mongo.find(Constants.WORKSHOPS_COLLECTION, workshop, res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject js = res.result().get(0);
        this.apiResponse.respondSuccess(ctx, js);
      } else {
        this.apiResponse.respondNotFound(ctx, "not found");
      }
    });
  }


  public void deleteWorkshop(RoutingContext ctx) {

    String workshop_id = ctx.request().getParam("workshop_id");
    mongo.updateCollection(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id)
      , new JsonObject().put("$set", new JsonObject().put("is_deleted", true)), res -> {
        if (res.succeeded() && res.result().getDocMatched() > 0) {
          this.apiResponse.respondSuccess(ctx, new JsonObject().put("message", "remove successfully"));
        } else {
          this.apiResponse.respondNotFound(ctx, "no workshop found");
        }
      });
  }


  public void showPart(RoutingContext ctx) {
    String workshop_id = ctx.request().getParam("workshop_id");
    JsonObject workshop = new JsonObject().put("_id", workshop_id);
    mongo.find(Constants.WORKSHOPS_COLLECTION, workshop, res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject wrkshp = res.result().get(0);
        JsonArray parts = wrkshp.containsKey("part") ? wrkshp.getJsonArray("part") : new JsonArray();
        JsonObject in = new JsonObject().put("$in", parts);
        JsonObject query = new JsonObject().put("_id", in);
        if (!parts.getList().isEmpty()) {
          mongo.find(Constants.USERS_COLLECTION, query, found_parts -> {
            if (found_parts.succeeded() && !found_parts.result().isEmpty()) {
              this.apiResponse.respondSuccess(ctx, new JsonObject().put("request", found_parts.result()));
            }
            else {
        this.apiResponse.respondNotFound(ctx, "not found");
      }
        });
      }

    }
    else{
      this.apiResponse.respondNotFound(ctx, "not found");
    }
    });

  }

  public void showWorkshopRequest(RoutingContext ctx){
    String workshop_id = ctx.request().getParam("workshop_id");
    mongo.find(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",workshop_id),res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject workshop =res.result().get(0);
        JsonArray request =workshop.containsKey("requests") ? workshop.getJsonArray("requests") : new JsonArray();
        JsonObject in = new JsonObject().put("$in",request);
        JsonObject query = new JsonObject().put("_id", in);
        if (!request.getList().isEmpty()) {
          mongo.find(Constants.REQUEST_COLLECTION, query, found_parts -> {
            if (found_parts.succeeded() && !found_parts.result().isEmpty()) {
              this.apiResponse.respondSuccess(ctx, new JsonObject().put("request", found_parts.result()));
            }
          });
        } else {
          this.apiResponse.respondSuccess(ctx, res.result().get(0));
        }

      }
      else{
        this.apiResponse.respondNotFound(ctx,"this workshop not found");
      }
    });
  }

  public void addPartToWorkshop(RoutingContext ctx){
    String workshop_id=ctx.request().getParam("workshop_id");
    String user_id=ctx.request().getParam("user_id");
    mongo.find(Constants.REGISTER_COLLECTION,new JsonObject().put("id_user",user_id),find_form->{
      if(find_form.succeeded() && !find_form.result().isEmpty()){
        JsonObject registery_form=find_form.result().get(0);
        JsonArray ghest =registery_form.getJsonArray("ghest");
        for(int i =0 ; i <ghest.size() ; ++i){
          if(!ghest.getJsonObject(i).getBoolean("pay")){
            this.apiResponse.respondWithError(ctx,"this user should pay all ghest!!!!");
          }
        }
        mongo.find(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",workshop_id),find_workshop->{
          if(find_workshop.succeeded() && !find_workshop.result().isEmpty()){
            JsonObject workshop =find_workshop.result().get(0);
            JsonArray part =workshop.containsKey("part") ? workshop.getJsonArray("part") : new JsonArray();
            part.add(user_id);
            JsonObject update = new JsonObject().put("$set",new JsonObject().put("part",part));
            mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",workshop_id),update,update_workshop->{
              if (update_workshop.succeeded()){
                this.apiResponse.respondSuccess(ctx,workshop);
              }
              else {
                this.apiResponse.respondInternalError(ctx,"failed");
              }
            });

          }
          else{
            this.apiResponse.respondInternalError(ctx,"this workshop not found");
          }
        });

      }
      else {
        this.apiResponse.respondInternalError(ctx,"this user not have registery form");
      }

    });

  }





}
