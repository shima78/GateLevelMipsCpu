package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class Financialmanager {

  private MongoClient mongo;
  private ApiResponse apiResponse;

  public  Financialmanager(MongoClient mongo) {

    this.apiResponse = new ApiResponse();
    this.mongo = mongo;
  }

  public void  addManager(RoutingContext ctx){
    String manager_id =ctx.request().getParam("manager_id");
    JsonObject manager = new JsonObject().put("_id",manager_id);
    mongo.find(Constants.USERS_COLLECTION,manager,res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject user = res.result().get(0);
        JsonArray roles = user.containsKey("roles") ? user.getJsonArray("roles") : new JsonArray();
        roles.add("financial_manager");
        JsonObject update = new JsonObject().put("$set",new JsonObject().put("roles",roles));
        mongo.updateCollection(Constants.USERS_COLLECTION,user,update,resup ->{
          if (resup.succeeded()){
            this.apiResponse.respondSuccess(ctx,user.put("roles",roles));
          }
          else {
            this.apiResponse.respondInternalError(ctx,"failed");
          }
        });

      }
      else {
        this.apiResponse.respondInternalError(ctx,"this user not found");
      }
    });
  }

}


