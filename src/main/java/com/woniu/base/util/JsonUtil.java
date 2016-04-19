package com.woniu.base.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.Reader;
import java.util.List;
import java.util.TimeZone;

public final class JsonUtil {
    @SuppressWarnings("serial")
    public static class CodecException extends RuntimeException {
        public CodecException(Throwable cause) {
            super(cause);
        }
    }

    private static final ObjectMapper mapper;
    private static final ObjectReader objectReader;
    private static final ObjectWriter objectWriter;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.registerModule(new JodaModule());

        //see http://wiki.fasterxml.com/JacksonFAQDateHandling
        mapper.setTimeZone(TimeZone.getDefault());//force local timezone

        objectReader = mapper.reader();
        objectWriter = mapper.writer();
    }

    private JsonUtil() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static String dump(Object value) throws CodecException {
        try {
            return objectWriter.writeValueAsString(value);
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

    public static <T> T load(Reader reader, Class<T> type) throws CodecException {
        try {
            return objectReader.withType(type).readValue(reader);
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

    public static <T> T load(String json, Class<T> type) throws CodecException {
        try {
            return objectReader.withType(type).readValue(json);
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

    public static <T> List<T> loadList(Reader reader, Class<T> type) throws CodecException {
        try {
            return objectReader.withType(mapper.getTypeFactory().constructCollectionType(List.class, type))
                    .readValue(reader);
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

    public static <T> List<T> loadList(String jsonArray, Class<T> type) throws CodecException {
        try {
            return objectReader.withType(mapper.getTypeFactory().constructCollectionType(List.class, type))
                    .readValue(jsonArray);
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

}
