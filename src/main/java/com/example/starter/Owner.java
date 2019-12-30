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
    JsonObject newone =new JsonObject().put("_id",workshop_id);
    JsonObject body =ctx.getBodyAsJson();
    mongo.find(Constants.WORKSHOPS_COLLECTION,newone,res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject workshp =res.result().get(0);
        JsonObject update = new JsonObject().put("$set", new JsonObject()
          .put("owner",body.getString("owner")));
        mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,workshp,update,resup->{
          if (resup.succeeded()){
           /* JsonObject newup = body.put("owner",body.getString("owner"));
            mongo.save(Constants.WORKSHOPS_COLLECTION,newup,res1->{
              if (res1.succeeded()){
                this.apiResponse.respondSuccess(ctx,workshp);
              }
            });*/
            this.apiResponse.respondSuccess(ctx,workshp.put("owner",body.getString("owner")));
          }
          else {
            this.apiResponse.respondWithError(ctx,"failed");
          }
        });
      }
      else {
        this.apiResponse.respondWithError(ctx,"no workshop found");
      }
    });





  }

}
