package com.example.starter;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Tuple;

import javax.swing.*;
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


}


