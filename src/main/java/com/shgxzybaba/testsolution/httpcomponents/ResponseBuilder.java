package com.shgxzybaba.testsolution.httpcomponents;

public class ResponseBuilder {

    private ResponseBuilder() {
        throw new IllegalStateException("Cannot Initialize static util class");
    }

    public static ApiResponseBody buildResponse(Throwable exception, int httpCode) {
        final ApiResponseBody output = new ApiResponseBody();
        output.setSuccess(false);
        output.setStatusCode(httpCode);
        String exceptionMessage = exception.getCause() == null ? exception.getMessage() : exception.getCause().getMessage();
        output.getErrors().add(exceptionMessage);

        return output;
    }
}
