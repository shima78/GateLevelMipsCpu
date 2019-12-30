package com.example.starter;

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


  }
}
