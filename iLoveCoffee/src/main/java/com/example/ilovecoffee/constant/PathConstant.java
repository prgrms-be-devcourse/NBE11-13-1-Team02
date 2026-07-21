package com.example.ilovecoffee.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathConstant {

    public static final String HOME = "/";
    public static final String API_HOME = "/api";


    public static final String ORDER_CREATE = "/orders/create";
    public static final String ORDER_FIND_ALL = "/orders/find_all";

    public static final String CANCEL = "/{id}/cancel";
}
