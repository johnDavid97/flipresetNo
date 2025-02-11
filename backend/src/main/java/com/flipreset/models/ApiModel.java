package com.flipreset.models;

import java.util.Map;

public class ApiModel {
    public String name;
    public String url;
    public String method;
    public Map<String, String> headers;
    public Body body;

    // Inner class for body-delen
    public static class Body {
        public String query;
        // public String operationName;
        // public Map<String, String> variables;
    }

}
