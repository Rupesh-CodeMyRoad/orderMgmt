//package com.rupjava.ordermgmt.Utils;
//
//import java.util.List;
//
//public class ResponseUtil {
//    public static <T> ApiResponse<T> error(List<String> errors, String message, int errorCode, String path) {
//        return new ApiResponse<>(
//                false,                // success
//                message,              // message
//                null,                 // data (null for error responses)
//                errors,               // list of errors
//                errorCode,            // error code
//                System.currentTimeMillis(), // timestamp
//                path                  // request path
//        );
//    }
//}
