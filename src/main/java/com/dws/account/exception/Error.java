package com.dws.account.exception;

import lombok.Builder;

@Builder
public class Error {

   private Integer code;
   private String message;

}
