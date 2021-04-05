package Controllers.common;
import Utils.ErrorResponse;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import spark.Request;
import spark.Response;


import static spark.Spark.*;

public class ErrorsController {

    final static String ERROR_500 = """
                                    {
                                        "message":"Custom 500 handling"
                                    }
                                    """;

    final static String RESPONSE_TYPE = "application/json";

    public static void erros_InternalServerError() {
        internalServerError((Request req, Response res) -> {
            res.type(ErrorsController.RESPONSE_TYPE);
            return ErrorsController.ERROR_500;
        });
    }

    public static void errors_Custom() {
        exception(MongoWriteException.class, (error, req, res) -> {
            res.status(501);
            res.type("application/json");
            ErrorResponse response = new ErrorResponse(error.getMessage(), null);
            res.body(new Gson().toJson(response));
        });
    }
}
