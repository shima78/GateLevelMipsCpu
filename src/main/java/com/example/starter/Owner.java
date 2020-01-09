package com.example.starter;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class Owner {

  private MongoClient mongo;
  private ApiResponse apiResponse;

  public Owner(MongoClient mongo) {

    this.apiResponse = new ApiResponse();
    this.mongo = mongo;
  }

  public void showOwner(RoutingContext ctx){
    String workshop_id =ctx.request().getParam("workshop_id");
   // String owner_id =ctx.request().getParam("owner_id");
    JsonObject newjson = new JsonObject().put("_id",workshop_id);
    mongo.find(Constants.WORKSHOPS_COLLECTION,newjson,res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject workshp =res.result().get(0);
        JsonObject owner =  new JsonObject().put("owner",workshp.getString("owner"));
        this.apiResponse.respondSuccess(ctx,owner);
      }
      else{
        this.apiResponse.respondNotFound(ctx,"no workshop found");
      }
    });
  }

  public void addOwner(RoutingContext ctx){
    String workshop_id =ctx.request().getParam("workshop_id");
    String owner_id =ctx.request().getParam("owner_id");
    JsonObject newone =new JsonObject().put("_id",workshop_id);
    mongo.find(Constants.WORKSHOPS_COLLECTION,newone,res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject workshp =res.result().get(0);
        mongo.find(Constants.USERS_COLLECTION,new JsonObject().put("_id",owner_id),res1 ->{
          if(res1.succeeded() && !res1.result().isEmpty()){
            JsonObject owner =res1.result().get(0);
            JsonObject update = new JsonObject().put("$set", new JsonObject()
              .put("owner",owner));
            mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,workshp,update,resup->{
              if (resup.succeeded()){
                this.apiResponse.respondSuccess(ctx,workshp.put("owner",owner));
              }
              else {
                this.apiResponse.respondWithError(ctx,"failed");
              }
            });
          }
          else{
            this.apiResponse.respondNotFound(ctx,"this owner not found");
          }

        });
      }
      else {
        this.apiResponse.respondWithError(ctx,"no workshop found");
      }
    });





  }

}
