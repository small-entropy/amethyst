package core.response.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
    @Override
    public void write(final JsonWriter out, final ObjectId value) throws IOException {
            out.value(value.toString());
        }

    @Override
    public ObjectId read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
