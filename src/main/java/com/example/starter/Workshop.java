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
    mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("_id", json.getString("_id")), findOwner -> {
      if (findOwner.succeeded() && !findOwner.result().isEmpty()) {
        mongo.save(Constants.WORKSHOPS_COLLECTION, json.put("is_deleted", false), saveRes -> {
          if (saveRes.succeeded()) {
            this.apiResponse.respondSuccess(ctx, json);
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
       /* JsonArray parts = js.containsKey("part") ? js.getJsonArray("part") : new JsonArray();
        JsonObject in = new JsonObject().put("$in",parts);
        JsonObject query = new JsonObject().put("_id",in);
        if(!parts.getList().isEmpty()){
          mongo.find(Constants.USERS_COLLECTION,query,found_parts ->{
            if(found_parts.succeeded() && !found_parts.result().isEmpty()){
              this.apiResponse.respondSuccess(ctx,js.put("parts",found_parts.result()));
            }*/
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
        JsonObject js = res.result().get(0);
        JsonArray parts = js.containsKey("part") ? js.getJsonArray("part") : new JsonArray();
        JsonObject in = new JsonObject().put("$in", parts);
        JsonObject query = new JsonObject().put("_id", in);
        if (!parts.getList().isEmpty()) {
          mongo.find(Constants.USERS_COLLECTION, query, found_parts -> {
            if (found_parts.succeeded() && !found_parts.result().isEmpty()) {
              this.apiResponse.respondSuccess(ctx, new JsonObject().put("parts", found_parts.result()));
            }
          });
        } else {
          this.apiResponse.respondSuccess(ctx, res.result().get(0));
        }
      } else {
        this.apiResponse.respondNotFound(ctx, "not found");
      }
    });

  }


}
