package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class ApiResponse {

  public void respondNotFound(RoutingContext ctx, String message) {
    ctx.response().setStatusCode(404);
    ctx.response().putHeader("Content-Type","application/json");
    ctx.response().end(new JsonObject().put("error", message).toString());
  }

  public void respondInternalError(RoutingContext ctx, String message) {
    ctx.response().setStatusCode(500);
    ctx.response().putHeader("Content-Type","application/json");
    ctx.response().end(new JsonObject().put("error", message).toString());
  }

  public void respondSuccess(RoutingContext ctx, JsonObject json) {
    ctx.response().setStatusCode(200);
    ctx.response().putHeader("Content-Type","application/json");
    ctx.response().end(new JsonObject().put("data", json).toString());
  }

  public  void  respondWithError(RoutingContext ctx ,String message){
    ctx.response().setStatusCode(200);
    JsonObject js = new JsonObject();
    js.put("error",message);
    ctx.response().putHeader("Content-Type","application/json");
    ctx.response().end(js.toString());
  }

  public  void respondSuccessCollection(RoutingContext ctx, List<JsonObject> jsonObjects){
    JsonArray jarr = new JsonArray();
    for (JsonObject jsonObject : jsonObjects) {
      jarr.add(jsonObject);
    }
    ctx.response().setStatusCode(200);
    ctx.response().putHeader("Content-Type","application/json");
    ctx.response().end(new JsonObject().put("data", jarr).toString());


  }
}
