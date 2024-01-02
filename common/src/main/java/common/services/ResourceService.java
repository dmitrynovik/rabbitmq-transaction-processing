package common.services;

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

// Naming this "*Service" in a Spring Boot based application may be a bit confusing (wrt `@Service').
// It seems more like a ResourceUtil or a JsonResourceStreamUtil or something like that.

public class ResourceService<T> {

    // ONE: If this method is called frequently we may want to consider caching the object mapper

    // TWO: This needs a few unit tests

    public Stream<T> toStream(String path, Class<T> classId) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream(path);
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);

        if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
            throw new IllegalStateException("The file '%s' did not contain a valid list of JSON objects".formatted(path));
        }
        
        jsonParser.nextToken(); // advance jsonParser to start of first object
        Iterator<T> iterator = jsonParser.readValuesAs(classId);

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
            false);
    }
}
