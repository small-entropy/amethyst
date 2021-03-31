package Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import spark.ResponseTransformer;

import java.util.ArrayList;

public class JsonTransformer implements ResponseTransformer {
    // Create instance GsonBuilder
    // Used for work with MongoDB uuid (not tranform to object)
    private Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter())
                .serializeNulls()
                .create();

    @Override
    public String render(Object model) {
        Answer<Object, Meta> answer = new Answer<Object, Meta>(model,null);
        Meta meta;

        if (model instanceof ArrayList) {
            meta = new Meta();
            meta.setCount(((ArrayList<?>) model).size());
        } else  {
            meta = null;
        }

        answer.setMeta(meta);
        return gson.toJson(answer);
    }

}