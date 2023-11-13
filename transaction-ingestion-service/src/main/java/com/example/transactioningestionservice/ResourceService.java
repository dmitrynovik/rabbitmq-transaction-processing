package com.example.transactioningestionservice;

import java.io.Console;
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

public class ResourceService<T> {

    // TODO: classId replacement
    Stream<T> toStream(String path, Class classId) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(path);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);

        if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
            System.out.println("TEXT:");
            System.out.println(jsonParser.getText());
            throw new IllegalStateException("Not an array");
        }
        
        jsonParser.nextToken(); // advance jsonParser to start of first object
        Iterator<T> iterator = jsonParser.readValuesAs(classId);

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
            false);
    }
}
