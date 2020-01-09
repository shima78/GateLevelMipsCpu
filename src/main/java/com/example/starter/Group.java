package com.example.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class Group {

  private MongoClient mongo;
  private ApiResponse apiResponse;

  public Group(MongoClient mongo) {
    this.mongo = mongo;
    this.apiResponse = new ApiResponse();
  }

  public void addGroup(RoutingContext ctx) {
    String workshop_id = ctx.request().getParam("workshop_id");
    JsonObject js = new JsonObject().put("workshop_id", workshop_id);
    mongo.save(Constants.GROUP_COLLECTION, js, res -> {
      if (res.succeeded()) {
        this.apiResponse.respondSuccess(ctx, js);
      } else {
        this.apiResponse.respondInternalError(ctx, "fail");
      }
    });
  }

  public void addGrayderTogroup(RoutingContext ctx) {
    String group_id = ctx.request().getParam("group_id");
    String grayder_id = ctx.request().getParam("grayder_id");
    mongo.find(Constants.GROUP_COLLECTION, new JsonObject().put("_id", group_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject group = res.result().get(0);
        JsonArray grayders = group.containsKey("gtayders") ? group.getJsonArray("grayders") : new JsonArray();
        grayders.add(grayder_id);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("grayders", grayders));
        mongo.updateCollection(Constants.GROUP_COLLECTION, group, update, res1 -> {
          if (res1.succeeded()) {
            this.apiResponse.respondSuccess(ctx, group);
          } else {
            this.apiResponse.respondInternalError(ctx, "failed");
          }
        });
      } else {
        this.apiResponse.respondNotFound(ctx, "this group not found");
      }
    });


  }

  public void addGrayderArshad(RoutingContext ctx) {
    String workshop_id = ctx.request().getParam("workshop_id");
    String grayder_id = ctx.request().getParam("grayder_id");
    mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject workshop = res.result().get(0);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("grayderArshad", grayder_id));
        mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,workshop, update, res1 -> {
          if (res1.succeeded()) {
            this.apiResponse.respondSuccess(ctx, update);
          } else {
            this.apiResponse.respondInternalError(ctx, "failed");
          }
        });
      } else {
        this.apiResponse.respondNotFound(ctx, "this group not found");
      }
    });
  }

  public void addPartgroup(RoutingContext ctx) {
    String group_id = ctx.request().getParam("group_id");
    String part_id = ctx.request().getParam("part_id");
    //peyda kon in group ro to collection
    mongo.find(Constants.GROUP_COLLECTION, new JsonObject().put("_id", group_id), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject group = res.result().get(0);
        //check kon bebin in part to in group has ya na
        if (group.containsKey("parts") && group.getJsonArray("parts").getList().contains(part_id)) {
          //age bod khob hichi
          this.apiResponse.respondSuccess(ctx, group);
        } else {
          String workshop_id = group.getString("workshop_id");
          JsonArray parts = group.containsKey("parts") ? group.getJsonArray("parts") : new JsonArray();
          JsonObject query = new JsonObject().put("_id", workshop_id);
          query.put("part", new JsonObject().put("$in", new JsonArray().add(part_id)));
          //check kon bebin asan in part jozve part haye workshop has ya ne
          mongo.find(Constants.WORKSHOPS_COLLECTION, query, res3 -> {
            if (res3.succeeded() && !res3.result().isEmpty()) {
              parts.add(part_id);
              JsonObject update = new JsonObject().put("$set", new JsonObject().put("parts", parts));
              mongo.updateCollection(Constants.GROUP_COLLECTION, group, update, res1 -> {
                if (res1.succeeded()) {
                  this.apiResponse.respondSuccess(ctx, group);
                } else {
                  this.apiResponse.respondInternalError(ctx, "failed");
                }
              });

            } else {
              this.apiResponse.respondNotFound(ctx, "not found");
            }

          });

        }

      } else {
        this.apiResponse.respondNotFound(ctx, "this group not found");
      }
    });


  }

}
