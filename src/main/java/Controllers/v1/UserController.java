package Controllers.v1;
import Services.UserService;
import Utils.JsonTransformer;
import dev.morphia.Datastore;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class UserController {
    public static void methods(Datastore store, JsonTransformer transformer) {
        // Group users routes
        path("/users", () -> {
            // Route for work with users list
            get("", (Request req, Response res) ->
                    UserService.getList(req, res, store), transformer);
            // Route for register user
            post("/register", (Request req, Response res) ->
                    UserService.registerUser(req, res, store), transformer);
            // Route for user login
            post("/login", (Request req, Response res) ->
                    UserService.loginUser(req, res, store), transformer);
            // Route for user autologin
            get("/autologin", (Request req, Response res) ->
                    UserService.autoLoginUser(req, res, store), transformer);
            // Logout user
            get("/logout", (Request req, Response res) ->
                    UserService.logoutUser(req, res, store), transformer);
        });
    }
}
