package com.example.starter;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Tuple;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Users {

  private ApiResponse apiResponse;
  //private MySQLPool mySQLClient;
  private MongoClient mongo;

  public Users(MongoClient mongoClient) {
    // this.mySQLClient = mySQLPool;
    this.mongo = mongoClient;
    this.apiResponse = new ApiResponse();
  }


  public void signup(RoutingContext ctx) {

    JsonObject json = ctx.getBodyAsJson();
    JsonObject query = new JsonObject().put("username", json.getString("username"));
    this.mongo.find(Constants.USERS_COLLECTION, query, res -> {
      if (res.succeeded() && res.result().isEmpty()) {
        mongo.save(Constants.USERS_COLLECTION, json.put("is_deleted", false), saveRes -> {
          if (saveRes.succeeded()) {
            this.apiResponse.respondSuccess(ctx, new JsonObject().put("success", "success"));
          } else if (saveRes.failed()) {
            this.apiResponse.respondInternalError(ctx, saveRes.cause().getMessage());
          }
        });
      } else {
        this.apiResponse.respondWithError(ctx, "please choose another username");
      }

    });

  }


  public void searchUserByName(RoutingContext ctx) {
    String search_text = ctx.request().getParam("search");
    String regx = ".*" + search_text + ".*";
    JsonObject query = new JsonObject();
    query.put("username", new JsonObject().put("$regex", regx));
    mongo.find(Constants.USERS_COLLECTION, query, matched_users -> {
      if (matched_users.succeeded() && !matched_users.result().isEmpty()) {
        this.apiResponse.respondSuccessCollection(ctx, matched_users.result());
      } else {
        this.apiResponse.respondNotFound(ctx, "No user found");
      }
    });
  }

  public static String generateString() {
    return UUID.randomUUID().toString();
  }

  public void ListUsers(RoutingContext ctx) {
    mongo.find(Constants.USERS_COLLECTION, new JsonObject(), res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        this.apiResponse.respondSuccessCollection(ctx, res.result());
      } else {
        this.apiResponse.respondNotFound(ctx, "no user found");
      }
    });

  }


  public void login(RoutingContext ctx) {

    JsonObject json = ctx.getBodyAsJson();
    mongo.find(Constants.USERS_COLLECTION, json, res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject founduser = res.result().get(0);
        if (founduser.containsKey("token")) {
          this.apiResponse.respondSuccess(ctx, founduser);
        } else {
          String token = generateString();
          JsonObject jstoken = new JsonObject().put("$set", new JsonObject().put("token", token));
          mongo.updateCollection(Constants.USERS_COLLECTION, json, jstoken, res1 -> {
            if (res1.succeeded()) {
              this.apiResponse.respondSuccess(ctx, founduser.put("token ", token));

            }
          });
        }
      } else {
        this.apiResponse.respondNotFound(ctx, "username or password is wrong");
      }


    });

  }

  public void deleteUser(RoutingContext ctx) {
    String user_id = ctx.request().getParam("user_id");
    mongo.updateCollection(Constants.USERS_COLLECTION, new JsonObject().put("_id", user_id)
      , new JsonObject().put("$set", new JsonObject().put("is_deleted", true)), res -> {
        if (res.succeeded() && res.result().getDocMatched() > 0) {
          this.apiResponse.respondSuccess(ctx, new JsonObject().put("message", "remove successfully"));
        } else {
          this.apiResponse.respondNotFound(ctx, "no user found");
        }
      });
  }

  public void addmanager(RoutingContext ctx) {
    String manager_id = ctx.request().getParam("manager_id");
    JsonObject manager = new JsonObject().put("_id", manager_id);
    mongo.find(Constants.USERS_COLLECTION, manager, res -> {
      if (res.succeeded() && !res.result().isEmpty()) {
        JsonObject user = res.result().get(0);
        JsonArray roles = user.containsKey("roles") ? user.getJsonArray("roles") : new JsonArray();
        roles.add("manager");
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("roles", roles));
        mongo.updateCollection(Constants.USERS_COLLECTION,new JsonObject().put("_id",manager_id), update, resup -> {
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


  public void taRequest(RoutingContext ctx) {
    String workshop_id = ctx.request().getParam("workshop_id");
    String user_id = ctx.request().getParam("user_id");
    JsonObject request_graydery = new JsonObject().put("user_id", user_id);
    request_graydery.put("date_request", new Date().getTime());
    request_graydery.put("date_result", new Date().getTime());
    request_graydery.put("confirm", false);
    mongo.insert(Constants.REQUEST_COLLECTION, request_graydery, res2 -> {
      if (res2.succeeded()) {
        mongo.find(Constants.USERS_COLLECTION, new JsonObject().put("_id", user_id), res -> {
          if (res.succeeded() && !res.result().isEmpty()) {
            JsonObject user = res.result().get(0);
            mongo.find(Constants.WORKSHOPS_COLLECTION, new JsonObject().put("_id", workshop_id), res1 -> {
              if (res1.succeeded() && !res1.result().isEmpty()) {
                JsonObject workshop = res1.result().get(0);
                JsonArray requests = workshop.containsKey("requests") ? workshop.getJsonArray("requests") : new JsonArray();
                requests.add(request_graydery.getString("_id"));
                JsonObject update_workshop = new JsonObject().put("$set", new JsonObject().put("requests", requests));
                mongo.updateCollection(Constants.WORKSHOPS_COLLECTION,new JsonObject().put("_id",workshop_id), update_workshop, res3 -> {
                  if (res3.succeeded()) {
                    JsonArray request = user.containsKey("request") ? user.getJsonArray("request") : new JsonArray();
                    request.add(request_graydery.getString("_id"));
                    JsonObject update_user = new JsonObject().put("$set", new JsonObject().put("request", request));
                    mongo.updateCollection(Constants.USERS_COLLECTION,new JsonObject().put("_id",user_id), update_user, res4 -> {
                      if (res4.succeeded()) {
                        this.apiResponse.respondSuccess(ctx, request_graydery);
                      } else {
                        this.apiResponse.respondInternalError(ctx, "failed");
                      }
                    });
                  } else {
                    this.apiResponse.respondInternalError(ctx, "request dont send to workshop");
                  }
                });

              } else {
                this.apiResponse.respondNotFound(ctx, "this workshop not found");
              }
            });

          } else {
            this.apiResponse.respondNotFound(ctx, "this user not found");
          }
        });
      } else {
        this.apiResponse.respondInternalError(ctx, "failed");
      }
    });

  }

  public void showUserRequest(RoutingContext ctx){
    String user_id = ctx.request().getParam("user_id");
    mongo.find(Constants.USERS_COLLECTION,new JsonObject().put("_id",user_id),res->{
      if(res.succeeded() && !res.result().isEmpty()){
        JsonObject user =res.result().get(0);
        JsonArray request =user.containsKey("request") ? user.getJsonArray("request") : new JsonArray();
        JsonObject in = new JsonObject().put("$in",request);
        JsonObject query = new JsonObject().put("_id", in);
        if (!request.getList().isEmpty()) {
          mongo.find(Constants.REQUEST_COLLECTION, query, found_parts -> {
            if (found_parts.succeeded() && !found_parts.result().isEmpty()) {
              this.apiResponse.respondSuccess(ctx, new JsonObject().put("request", found_parts.result()));
            }
          });
        } else {
          this.apiResponse.respondSuccess(ctx, res.result().get(0));
        }

      }
      else{
        this.apiResponse.respondNotFound(ctx,"this user not found");
      }
    });
  }


}


