package com.theironyard.charlotte;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> usersMap = new HashMap<>();
    //create a new static hashmap of users.  stores all our current users. string is user name, and values are user objects

    public static void main(String[] args) {

        usersMap.put("Jennifer", new User("Jennifer", "abc")); // this is just to test

        Spark.get(
                "/", // relative path (homepage)
                ((request, response) -> {
                    Session session = request.session(); // get reference to our current session
                    String name = session.attribute("username"); // get username from session
                    User userObj = usersMap.get(name); // create a user object and it equals the value in the users hashmap (of that name)

                    HashMap m = new HashMap<>(); // create model hashmap
                    if (userObj == null) { // if the user object is null
                        return new ModelAndView(m, "login.html"); // return the login page
                    }
                    else {
                        return new ModelAndView(userObj, "messages.html"); //otherwise takes them to messages
                    }
                }),
                new MustacheTemplateEngine()
        );


        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");  // get name trying to log in as
                    String password = request.queryParams("password"); // get password
                    User userObj = usersMap.get(name); // reference hashmap at that name, key is name value is user object

                    if ((userObj == null) || (userObj != null && usersMap.get(name).getPassword().equals(password))) {
                        // if userObj is null OR not null and password is correct
                        if (userObj == null) { // if user is null
                            userObj = new User(name, password); // create object and put in hashmap
                            usersMap.putIfAbsent(name, userObj); // don't really need put if absent, but...just in case?
                        }
                        Session session = request.session(); // both will - set our session in reference to current users name
                        session.attribute("username", name);
                        response.redirect("/"); // then redirect
                    } else {
                        if (usersMap.get(name).getPassword() != password) { // if password is not correct, do not start a session and just redirect
                            response.redirect("/");
                        }
                    }
                    return "";
                })
        );

// if the user doesn't exist in the hashmap, create it, and add it to the hashmap
// if the user doesn't exist before this request then add it to session
// if the user DOES exist AND their password is the same as what they passed in through the form, add that username to session
// redirect always to the homepage


        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username"); // getting current session, asking who are you?
                    User user = usersMap.get(name); // getting current user
                    if (user == null) { // if no user
                        throw new Exception("User is not logged in"); // throw exception
                    }

                    String messageSubject = request.queryParams("messageSubject"); // get message data from form submission
                    String messageDate = request.queryParams("messageDate");
                    String messageBody = request.queryParams("messageBody");
                    Message messageObj = new Message(messageSubject, messageDate, messageBody); // put that data into new message object

                    user.messagesList.add(messageObj); // add that message object to the message array of that user object

                    response.redirect("/"); //redirect
                    return "";
                })
        );

        Spark.post(
                "/delete-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    User user = usersMap.get(name);

                    int messageDelete = Integer.valueOf(request.queryParams("messageDelete")); // get the message number
                    //from form that they want to delete.  since list is in order, can use index to remove from arraylist

                    user.messagesList.remove(messageDelete - 1); // minus 1 to remove based on index

                    response.redirect("/");
                    return "";
                })
        );


        Spark.post(
                "/edit-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    User user = usersMap.get(name);

                    int messageDelete = Integer.valueOf(request.queryParams("messageEditNumber"));
                    String subjectEdit = request.queryParams("subjectEdit");
                    String dateEdit = request.queryParams("dateEdit");
                    String messageEdit = request.queryParams("messageEdit");

                    Message newMessage = new Message(subjectEdit, dateEdit, messageEdit); // create a new message object
                    // with edited information

                    user.messagesList.set(messageDelete - 1, newMessage); // replace old object with new

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session(); // have current session
                    session.invalidate(); // invalidate it
                    response.redirect("/"); // redirect
                    return "";
                })
        );
    }
}


