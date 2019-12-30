package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class Niazha {

    private MongoClient mongo;
    private ApiResponse apiResponse;

    public Niazha(MongoClient mongo) {

      this.apiResponse = new ApiResponse();
      this.mongo = mongo;
    }

  public void addPishniaz(RoutingContext ctx) {

    //add_pishniaz_:id_workshop_:id
    String pishniaz_id = ctx.request().getParam("pishniaz_id");
    String workshop_id = ctx.request().getParam("workshop_id");

    //if this workshop exist
    mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), foundWorkshop -> {
      if (foundWorkshop.succeeded() && !foundWorkshop.result().isEmpty()) {
        JsonObject jsworkshop = foundWorkshop.result().get(0);
        JsonArray jarr = jsworkshop.getJsonArray("pishniaz");
        //if this workshop contains this pishniaz id
        if (jarr.contains(pishniaz_id)) {
          this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
        } else {
          mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("pishniaz", pishniaz_id), pishniaz_exist -> {
            if (pishniaz_exist.succeeded() && pishniaz_exist.result().isEmpty()) {

              mongo.updateCollection(Constants.WORKSHOPS_COLLECTION
                , new JsonObject().put("_id", workshop_id)
                , new JsonObject().put("$push", new JsonObject().put("pishniaz", pishniaz_id)), result -> {
                  if (result.succeeded()) {
                    this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
                  } else {
                    this.apiResponse.respondInternalError(ctx, result.cause().getMessage());
                  }
                });
            }
          });
        }
      } else {
        this.apiResponse.respondNotFound(ctx, "Workshop not found");
      }
    });
  }


  public void removePishnaiz(RoutingContext ctx) {


    //add_pishniaz_:id_workshop_:id
    String pishniaz_id = ctx.request().getParam("pishniaz_id");
    String workshop_id = ctx.request().getParam("workshop_id");

    //if this workshop exist
    mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), foundWorkshop -> {
      if (foundWorkshop.succeeded() && !foundWorkshop.result().isEmpty()) {
        JsonObject jsworkshop = foundWorkshop.result().get(0);
        JsonArray jarr = jsworkshop.getJsonArray("pishniaz");
        //if this workshop contains this pishniaz id
        if (jarr.contains(pishniaz_id)) {
          mongo.updateCollection(Constants.WORKSHOPS_COLLECTION
            , new JsonObject().put("_id", workshop_id)
            , new JsonObject().put("$pull", new JsonObject().put("pishniaz", pishniaz_id)), result -> {
              if (result.succeeded()) {
                this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
              } else {
                this.apiResponse.respondInternalError(ctx, result.cause().getMessage());
              }
            });
        } else {
          this.apiResponse.respondNotFound(ctx, "pishniaz not found");
        }
      } else {
        this.apiResponse.respondNotFound(ctx, "Workshop not found");
      }
    });
  }

  public void addPasniaz(RoutingContext ctx) {
    //add_pishniaz_:id_workshop_:id
    String pasniaz_id = ctx.request().getParam("pasniaz_id");
    String workshop_id = ctx.request().getParam("workshop_id");

    //if this workshop exist
    mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), foundWorkshop -> {
      if (foundWorkshop.succeeded() && !foundWorkshop.result().isEmpty()) {
        JsonObject jsworkshop = foundWorkshop.result().get(0);
        JsonArray jarr = jsworkshop.getJsonArray("pasniaz");
        //if this workshop contains this pishniaz id
        if (jarr.contains(pasniaz_id)) {
          this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
        } else {
          mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("pasniaz", pasniaz_id), pishniaz_exist -> {
            if (pishniaz_exist.succeeded() && pishniaz_exist.result().isEmpty()) {

              mongo.updateCollection(Constants.WORKSHOPS_COLLECTION
                , new JsonObject().put("_id", workshop_id)
                , new JsonObject().put("$push", new JsonObject().put("pasniaz", pasniaz_id)), result -> {
                  if (result.succeeded()) {
                    this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
                  } else {
                    this.apiResponse.respondInternalError(ctx, result.cause().getMessage());
                  }
                });
            }
          });
        }
      } else {
        this.apiResponse.respondNotFound(ctx, "Workshop not found");
      }
    });
  }

  public void removePasniaz(RoutingContext ctx){
    //add_pishniaz_:id_workshop_:id
    String pasniaz_id = ctx.request().getParam("pasniaz_id");
    String workshop_id = ctx.request().getParam("workshop_id");

    //if this workshop exist
    mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), foundWorkshop -> {
      if (foundWorkshop.succeeded() && !foundWorkshop.result().isEmpty()) {
        JsonObject jsworkshop = foundWorkshop.result().get(0);
        JsonArray jarr = jsworkshop.getJsonArray("pasniaz");
        //if this workshop contains this pishniaz id
        if (jarr.contains(pasniaz_id)) {
          mongo.updateCollection(Constants.WORKSHOPS_COLLECTION
            , new JsonObject().put("_id", workshop_id)
            , new JsonObject().put("$pull", new JsonObject().put("pasniaz", pasniaz_id)), result -> {
              if (result.succeeded()) {
                this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
              } else {
                this.apiResponse.respondInternalError(ctx, result.cause().getMessage());
              }
            });
        } else {
          this.apiResponse.respondNotFound(ctx, "pasniaz not found");
        }
      } else {
        this.apiResponse.respondNotFound(ctx, "Workshop not found");
      }
    });

  }


}
