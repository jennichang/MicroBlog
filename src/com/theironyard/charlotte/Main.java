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

        usersMap.put("Jennifer", new User("Jennifer", "abc"));

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    User userObj = usersMap.get(name);

                    HashMap m = new HashMap<>();
                    if (userObj == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        return new ModelAndView(userObj, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );


        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    User userObj = usersMap.get(name);
                    if ((userObj != null) && (usersMap.get(name).getPassword().equals(password))) {
                        Session session = request.session();
                        session.attribute("username", name);

                        response.redirect("/");
                        return "";
                    } else if ((userObj != null) && (usersMap.get(name).getPassword() != password)) {
                        userObj = null;
                        Session session = request.session();
                        session.invalidate();
                        response.redirect("/");
                        return "";
                    } else {
                        userObj = new User(name, password);
                        usersMap.put(name, userObj);
                        Session session = request.session();
                        session.attribute("username", name);

                        response.redirect("/");
                        return "";
                    }

                })
        );


        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    User user = usersMap.get(name);
                    if (user == null) {
                        throw new Exception("User is not logged in");
                    }

                    String messageSubject = request.queryParams("messageSubject");
                    String messageDate = request.queryParams("messageDate");
                    String messageBody = request.queryParams("messageBody");
                    Message messageObj = new Message(messageSubject, messageDate, messageBody);

                    user.messagesList.add(messageObj);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }
}


