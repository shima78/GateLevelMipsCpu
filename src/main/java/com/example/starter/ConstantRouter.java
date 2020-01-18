package com.example.starter;

import org.omg.CORBA.PUBLIC_MEMBER;

public class ConstantRouter {

  public static String SIGN_UP = "/signup";
  public static String LOG_IN = "/login";
  public static String ADD_WORKSHOP = "/add_workshop";
  public static String SHOW_WORKSHOP = "/show_workshop/:id";
  public static String SHOW_PART = "/show_part/:workshop_id";
  public static String ADD_GRAYDER = "/add_grayder/:workshop_id";
  public static String SHOW_OWNER = "/show_owner/:workshop_id/:owner_id";
  public static String SHOW_REQUEST = "/show_request";
  public static String ADD_OWNER = "/add_owner/:workshop_id/:owner_id";
  public static String ADD_GROUP = "/add_group/:workshop_id";
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
  public static String ADD_FAINACIALM ="/add_financialm/:manager_id";
  public static String ADD_MANAGER="/add_manager/:manager_id";
  public static String ADD_GRAYDERGROUP ="/add_groupgray/:group_id/:grayder_id";
  public static String GRAYDER_ARSHAD ="/add_grouparshad/:workshop_id/:grayder_id";
  public static String ADD_PARTGROUP ="/add_partgroup/:group_id/:part_id";
  public static String REQUEST_GRAYDERY="/request/:workshop_id/:user_id";
  public static String SHOW_USER_REQUEST="/user_request/:user_id";
  public static String SHOW_WORKSHOP_REQUEST ="/workshop_request/:workshop_id";
  public static String ACCEPT_REQUEST ="/accept_request/:request_id";
  public static String CREATE_FORM="/create_form/:workshop_id/:owner_id";
  public static String ADD_QUESTION_FORM="/add_question/:form_id";
  public static String ANSWER_FORM="/answer_form/:form_id/:user_id";
  public static String ANSWER_TO_QUESTION="/answer_question/:answer_id";
  public static String ADD_USER_TO_FORM="/adduser_form/:form_id/:user_id";
  public static String ADD_GROUP_TO_FORM="/addgroup_form/:form_id/:group_id";
  public static String REGISTER_FORM="/register_form/:user_id/:wokshop_id";//monde hanoz
  public static String SHOW_MY_WORKSHOP="/show_myworkshop/:user_id";
  public static String SHOW_MY_ROLE="/show_myrole/:user_id/:workshop_id";
  public static String SHOW_ROLE="/show_role/:user_id";//admin or finantional manager
  public static String CONFIRM_GHEST="/confirm_ghest";
  public static String ADD_PART_TO_WORKSHOP="/add_part/:workshop_id/:user_id";


}
