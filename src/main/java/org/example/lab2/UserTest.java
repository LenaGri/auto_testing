package org.example.lab2;

public class UserTest {
    private static final String baseUrl = "//https://petstore.swagger.io/v2";
    private static final String USER = "/user",
            USER_USERNAME = USER + "/{username}",
            USER_LOGIN = USER + "/login",
            USER_LOGOUT = USER + "/logout";
    private String username;
    private String firstName;
}
