package com.example.starter;

import org.omg.CORBA.PUBLIC_MEMBER;

public class ConstantRouter {

  public static String SIGN_UP = "/signup";
  public static String LOG_IN = "/login";
  public static String ADD_WORKSHOP = "/add_workshop";
  public static String SHOW_WORKSHOP = "/show_workshop/:id";
  public static String SHOW_PART = "/show_part/:workshop_id";
  public static String ADD_GRAYDER = "/add_grayder/:workshop_id";
  public static String SHOW_OWNER = "/show_owner/:workshop_id";
  public static String SHOW_REQUEST = "/show_request";
  public static String ADD_OWNER = "/add_owner/:workshop_id";
  public static String ADD_GROUP = "/add_group";
  public static String SHOW_GROUP = "/show_group";
  public static String LIST_WORKSHOP = "/list_workshop";
  public static String LIST_USER = "/list_users";
  public static String ADD_PISHNIAZ = "/add_pishniaz/:workshop_id/:pishniaz_id";
  public static String REMOVE_PISHNIAZ = "/remove_pishniaz/:workshop_id/:pishniaz_id";
  public static String DELETE_WORKSHOP = "/delete_workshop/:workshop_id";
  public static String DELETE_USER="/delete_user/:user_id";
  public static String SEARCH_USERS = "/search_users";
  public static String ADD_PASNIAZ="/add_pasniaz/:workshop_id/:pasniaz_id";
  public static String REMOVE_PASNIAZ="/remove_pasniaz/:workshop_id/:pasniaz_id";
}
