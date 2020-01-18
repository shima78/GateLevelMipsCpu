package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import javax.swing.*;
import java.util.Date;

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
            JsonObject workshop =new JsonObject().put("role","owner");
            workshop.put("workshop_id",workshop_id);
            JsonArray workshops = owner.containsKey("workshops") ? owner.getJsonArray("workshops") : new JsonArray();
            workshops.add(workshop);
            JsonObject update_user = new JsonObject().put("$set",new JsonObject().put("workshops",workshops));
            mongo.updateCollection(Constants.USERS_COLLECTION,new JsonObject().put("_id",owner.getString("_id")),update_user,resupdate->{
              if(resupdate.succeeded()){
                JsonObject update = new JsonObject().put("$set", new JsonObject()
                  .put("owner",owner));
                mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",workshop_id),update,resup->{
                  if (resup.succeeded()){

                    this.apiResponse.respondSuccess(ctx,workshp.put("owner",owner));
                  }
                  else {
                    this.apiResponse.respondWithError(ctx,"failed");
                  }
                });

              }
              else{
                this.apiResponse.respondInternalError(ctx,"failed");
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


  public void acceptRequest(RoutingContext ctx){//lazeme ke owner begirim ya ne
    //String workshop_id=ctx.request().getParam("workshop_id");
    String request_id=ctx.request().getParam("request_id");
    mongo.find(Constants.REQUEST_COLLECTION,new JsonObject().put("_id",request_id),find_request->{
      if(find_request.succeeded() && !find_request.result().isEmpty()){
        JsonObject request =find_request.result().get(0);
        JsonObject update=new JsonObject().put("$set",new JsonObject().put("confirm",true).put("date_result", new Date().getTime()));
        mongo.updateCollection(Constants.REQUEST_COLLECTION,new JsonObject().put("_id",request_id),update,upRequest->{
          if(upRequest.succeeded()){
            this.apiResponse.respondSuccess(ctx,request);
          }
          else {
            this.apiResponse.respondWithError(ctx,"failed");
          }
        });
      }
      else{
        this.apiResponse.respondWithError(ctx,"this request not found");
      }
    });
  }

}
