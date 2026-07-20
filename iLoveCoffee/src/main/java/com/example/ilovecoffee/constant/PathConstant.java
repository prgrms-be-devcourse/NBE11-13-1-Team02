package com.example.ilovecoffee.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathConstant {

    public static final String HOME = "/";
    public static final String API_HOME = "/api";

    public static final String CREATE = "/create";
    public static final String FIND_ALL = "/find_all";

    public static final String CANCEL = "/{id}/cancel";
}
