package com.example.starter;

import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();


    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    JsonObject config = new JsonObject()
      .put("connection_string", "mongodb://localhost:27017")
      .put("db_name", "cms");


    MongoClient mongo = MongoClient.createShared(vertx, config);
    Users users = new Users(mongo);
    Workshop workshops = new Workshop(mongo);
    Niazha niazha = new Niazha(mongo);
    Owner owner = new Owner(mongo);
    Grayder grayder = new Grayder(mongo);
    Financialmanager financialmanager = new Financialmanager(mongo);
    Group group = new Group(mongo);
    Form form =new Form(mongo);

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());


    /*Handle CORS*/

    Set<String> allowedHeaders = new HashSet<>();
    allowedHeaders.add("x-requested-with");
    allowedHeaders.add("Access-Control-Allow-Origin");
    allowedHeaders.add("origin");
    allowedHeaders.add("Content-Type");
    allowedHeaders.add("accept");
    allowedHeaders.add("X-PINGARUNER");

    Set<HttpMethod> allowedMethods = new HashSet<>();
    allowedMethods.add(HttpMethod.GET);
    allowedMethods.add(HttpMethod.POST);
    allowedMethods.add(HttpMethod.OPTIONS);
    /*
     * these methods aren't necessary for this sample,
     * but you may need them for your projects
     */
    allowedMethods.add(HttpMethod.DELETE);
    allowedMethods.add(HttpMethod.PATCH);
    allowedMethods.add(HttpMethod.PUT);

    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));



    //workshop apis
    router.post(ConstantRouter.ADD_WORKSHOP).handler(workshops::addWorkshop);
    router.get(ConstantRouter.LIST_WORKSHOP).handler(workshops::ListWorkshops);
    router.get(ConstantRouter.SHOW_WORKSHOP).handler(workshops::getWorkshop);
    router.delete(ConstantRouter.DELETE_WORKSHOP).handler(workshops::deleteWorkshop);
    router.get(ConstantRouter.SHOW_PART).handler(workshops::showPart);
    router.get(ConstantRouter.SHOW_WORKSHOP_REQUEST).handler(workshops::showWorkshopRequest);
    router.get(ConstantRouter.ADD_PART_TO_WORKSHOP).handler(workshops::addPartToWorkshop);


    //users api
    router.get(ConstantRouter.LIST_USER).handler(users::ListUsers);
    router.post(ConstantRouter.SIGN_UP).handler(users::signup);
    router.post(ConstantRouter.LOG_IN).handler(users::login);
    router.get(ConstantRouter.SEARCH_USERS).handler(users::searchUserByName);
    router.delete(ConstantRouter.DELETE_USER).handler(users::deleteUser);
    router.get(ConstantRouter.ADD_FAINACIALM).handler(financialmanager::addManager);
    router.get(ConstantRouter.REQUEST_GRAYDERY).handler(users::taRequest);
    router.get(ConstantRouter.SHOW_USER_REQUEST).handler(users::showUserRequest);
    router.get(ConstantRouter.SHOW_MY_WORKSHOP).handler(users::showMyWorkshop);
    router.get(ConstantRouter.SHOW_MY_ROLE).handler(users::showMyRole);
    router.get(ConstantRouter.SHOW_ROLE).handler(users::showRole);

    //niazha api
    router.get(ConstantRouter.ADD_PASNIAZ).handler(niazha::addPasniaz);
    router.get(ConstantRouter.REMOVE_PASNIAZ).handler(niazha::removePasniaz);
    router.get(ConstantRouter.ADD_PISHNIAZ).handler(niazha::addPishniaz);
    router.get(ConstantRouter.REMOVE_PISHNIAZ).handler(niazha::removePishnaiz);


  //owner api
    router.get(ConstantRouter.SHOW_OWNER).handler(owner::showOwner);
    router.get(ConstantRouter.ADD_OWNER).handler(owner::addOwner);
    router.get(ConstantRouter.ACCEPT_REQUEST).handler(owner::acceptRequest);

    //grayder api
    router.post(ConstantRouter.ADD_GRAYDER).handler(grayder::addGrayder);

    //financial_manager api
    router.get(ConstantRouter.ADD_FAINACIALM).handler(financialmanager::addManager);
    router.post(ConstantRouter.CONFIRM_GHEST).handler(financialmanager::confirmGhest);

    //manager
    router.get(ConstantRouter.ADD_MANAGER).handler(users::addmanager);

    //group
    router.get(ConstantRouter.ADD_GROUP).handler(group::addGroup);
    router.get(ConstantRouter.ADD_GRAYDERGROUP).handler(group::addGrayderTogroup);
    router.get(ConstantRouter.GRAYDER_ARSHAD).handler(group::addGrayderArshad);
    router.get(ConstantRouter.ADD_PARTGROUP).handler(group::addPartgroup);

    //form
    router.get(ConstantRouter.CREATE_FORM).handler(form::createForm);
    router.post(ConstantRouter.ADD_QUESTION_FORM).handler(form::addQuestionsToForm);
    router.get(ConstantRouter.ANSWER_FORM).handler(form::answerToForm);
    router.get(ConstantRouter.ADD_QUESTION_FORM).handler(form::addQuestionsToForm);
    router.post(ConstantRouter.ANSWER_TO_QUESTION).handler(form::answerToQuestion);
    router.get(ConstantRouter.ADD_USER_TO_FORM).handler(form::addUserToForm);
    router.get(ConstantRouter.ADD_GROUP_TO_FORM).handler(form::addGroupToForm);

  //registery form
    router.post(ConstantRouter.REGISTER_FORM).handler(form::registerForm);

    vertx.createHttpServer().requestHandler(router).listen(8086);


  }
}
