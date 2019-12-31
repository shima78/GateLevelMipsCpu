package com.example.starter;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class Grayder {

  private MongoClient mongo;
  private ApiResponse apiResponse;

  public Grayder(MongoClient mongo) {

    this.apiResponse = new ApiResponse();
    this.mongo = mongo;
  }

  public void addGrayder(RoutingContext ctx){
    String workshop_id =ctx.request().getParam("workshop_id");
    JsonObject body = ctx.getBodyAsJson();
    JsonObject wrkshp = new JsonObject().put("_id",workshop_id);
    mongo.find(Constants.WORKSHOPS_COLLECTION,wrkshp,res ->{
      if (res.succeeded() && !res.result().isEmpty()){
        String grayder =body.getString("grayder");
        JsonObject newup = new JsonObject().put("grayder",grayder);


       }
    });

  }
}
