package com.theironyard.charlotte;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> usersMap = new HashMap<>();

    public static void main(String[] args) {

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = usersMap.get(username);

                    HashMap modelMap = new HashMap<>();
                    if (user == null) {
                        return new ModelAndView(modelMap, "login.html");
                    } else {
                        return new ModelAndView(user, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    User user = usersMap.get(name);
                    if (user == null) {
                        user = new User(name, password);
                        usersMap.put(name, user);
                    } else if (user != null) {
                        if (password != user.getPassword()) {
                            throw new Exception("Password is not correct");
                        }
                    }
                    Session session = request.session();
                    session.attribute("username", name);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {

                }



        )




    }
}
