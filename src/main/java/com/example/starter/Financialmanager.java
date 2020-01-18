package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class Financialmanager {

  private MongoClient mongo;
  private ApiResponse apiResponse;

  public Financialmanager(MongoClient mongo) {

    this.apiResponse = new ApiResponse();
    this.mongo = mongo;
  }

  public void addManager(RoutingContext ctx) {
    String manager_id = ctx.request().getParam("manager_id");
    JsonObject manager = new JsonObject().put("_id", manager_id);
    mongo.find(Constants.USERS_COLLECTION, manager, res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject user = res.result().get(0);
        JsonArray roles = user.containsKey("roles") ? user.getJsonArray("roles") : new JsonArray();
        roles.add("financial_manager");
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("roles", roles));
        mongo.updateCollection(Constants.USERS_COLLECTION, new JsonObject().put("_id", manager_id), update, resup -> {
          if (resup.succeeded()) {
            this.apiResponse.respondSuccess(ctx, user.put("roles", roles));
          } else {
            this.apiResponse.respondInternalError(ctx, "failed");
          }
        });

      } else {
        this.apiResponse.respondInternalError(ctx, "this user not found");
      }
    });
  }

  public void confirmGhest(RoutingContext ctx) {
    String ghest_id = ctx.getBodyAsJson().getString("ghest_id");
    String form_id = ctx.getBodyAsJson().getString("form_id");
    mongo.find(Constants.REGISTER_COLLECTION, new JsonObject().put("_id", form_id), find_form -> {
      if (find_form.succeeded() && !find_form.result().isEmpty()) {
        JsonObject form = find_form.result().get(0);
        JsonArray array = form.getJsonArray("ghest");
        for (int i = 0; i < array.size(); i++) {
          JsonObject singleGhest = array.getJsonObject(i);
          if (ghest_id.equals(String.valueOf(singleGhest.getInteger("id")))) {
            singleGhest.put("pay", true);
            break;
          }
        }
        mongo.replaceDocuments(Constants.REGISTER_COLLECTION, new JsonObject().put("_id", form_id), form, update -> {
          if (update.succeeded()) {
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

}


