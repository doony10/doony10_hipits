package com.hipits.apps.business;

import java.io.Serializable;

public class TestItem implements Serializable{
	private static long index = 0;
	private long Id = 0;
	private String name;
	private String number;
	private boolean check;
	public TestItem(String name,String number){
		Id = index++;
		this.name = name;
		this.number = number;
	}
	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
	public long getId() {
		return Id;
	}

	public void setId(long Id) {
		this.Id = Id;
	}

	public String getTitle() {
		return name;
	}

	public void setTitle(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
