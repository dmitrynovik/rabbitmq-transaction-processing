package common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceUtils {

    public static <T> Stream<T> toStream(String path, Class<T> classId) throws IOException {
        InputStream inputStream = classId.getResourceAsStream(path);
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);

        if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
            throw new IllegalStateException("The file '%s' did not contain a valid JSON array".formatted(path));
        }
        
        jsonParser.nextToken(); // advance jsonParser to start of first object
        Iterator<T> iterator = jsonParser.readValuesAs(classId);

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
            false);
    }
}
