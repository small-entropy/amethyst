package Transformers;
import Adapters.ObjectIdTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {
    // Create instance GsonBuilder
    // Used for work with MongoDB uuid (not tranform to object)
    private Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter())
    //            .serializeNulls()
                .create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}