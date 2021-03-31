package Controllers;

import Models.User;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import spark.Request;
import spark.Response;

import java.util.List;

public class UserController {
    public static List<User> getList(Request request, Response response, Datastore datastore) {
        response.type("application/json");
        return datastore.find(User.class).iterator(new FindOptions().skip(0).limit(10)).toList();
    }
}
