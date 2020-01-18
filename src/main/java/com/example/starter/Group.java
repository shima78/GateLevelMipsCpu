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
        JsonArray grayders = group.containsKey("grayders") ? group.getJsonArray("grayders") : new JsonArray();
        grayders.add(grayder_id);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("grayders", grayders));
        mongo.updateCollection(Constants.GROUP_COLLECTION, new JsonObject().put("_id", group_id), update, res1 -> {
          if (res1.succeeded()) {
            mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("_id", grayder_id), res2 -> {
              if (res2.succeeded() && !res2.result().isEmpty()) {
                JsonObject grayder = res2.result().get(0);
                JsonArray workshops = grayder.containsKey("workshops") ? grayder.getJsonArray("workshops") : new JsonArray();
                JsonObject workshop = new JsonObject().put("role", "grayder");
                workshop.put("workshop_id", group.getString("workshop_id"));
                workshops.add(workshop);
                JsonObject update_user = new JsonObject().put("$set", new JsonObject().put("workshops", workshops));
                mongo.updateCollection(Constants.USERS_COLLECTION, new JsonObject().put("_id", grayder.getString("_id")), update_user, res4 -> {
                  if (res4.succeeded()) {
                    this.apiResponse.respondSuccess(ctx, group);
                  } else {
                    this.apiResponse.respondInternalError(ctx, "failed");
                  }
                });
              } else {
                this.apiResponse.respondInternalError(ctx, "this user not found");
              }
            });


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
        mongo.updateCollection(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), update, res1 -> {
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
    mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("_id",part_id),user_update->{
      if(user_update.succeeded() && !user_update.result().isEmpty()){
        JsonObject user =user_update.result().get(0);
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
                  mongo.updateCollection(Constants.GROUP_COLLECTION, new JsonObject().put("_id", group_id), update, res1 -> {
                    if (res1.succeeded()) {
                      JsonObject workshop = new JsonObject().put("role","part");
                      workshop.put("workshop_id",workshop_id);
                      JsonArray workshops = user.containsKey("workshops") ? user.getJsonArray("workshops") : new JsonArray();
                      workshops.add(workshop);
                      JsonObject upadte_user=new JsonObject().put("$set",new JsonObject().put("workshops",workshops));
                      mongo.updateCollection(Constants.USERS_COLLECTION,new JsonObject().put("_id",user.getString("_id")),upadte_user,resu->{
                        if (resu.succeeded()){
                          this.apiResponse.respondSuccess(ctx, group);
                        }
                        else {
                          this.apiResponse.respondInternalError(ctx,"failed");
                        }
                      });

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
      else{
        this.apiResponse.respondInternalError(ctx,"this user not found");
      }

    });


  }

}
