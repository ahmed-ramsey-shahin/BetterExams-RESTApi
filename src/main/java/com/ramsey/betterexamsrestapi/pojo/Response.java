package com.ramsey.betterexamsrestapi.pojo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
	
	private Integer status;
	private T message;
	
}
