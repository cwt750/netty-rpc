package com.cwt.common.http;

import lombok.Data;


@Data
public class Request {

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object parameter[];

}
