package com.bbva.integration.bean;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ExceptionResponse implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int code;
 private String description;
 
 public int getCode() {
  return code;
 }
 public void setCode(int code) {
  this.code = code;
 }
 public String getDescription() {
  return description;
 }
 public void setDescription(String description) {
  this.description = description;
 }
 
}