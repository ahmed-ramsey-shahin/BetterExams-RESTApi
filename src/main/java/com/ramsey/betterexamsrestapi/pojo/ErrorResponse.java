package com.ramsey.betterexamsrestapi.pojo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	
	private Integer status;
	private String error;
	private String message;
	
}
