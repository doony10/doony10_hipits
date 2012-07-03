package hipits.com;

import java.io.Serializable;

public class LVSample3Item implements Serializable {

	private static final long serialVersionUID = -1586987653093943671L;
	private static long index = 0;
	private long Id = 0;
	private String name;
	private String number;

	public LVSample3Item(String name, String number) {
		Id = index++;
		this.name = name;
		this.number = number;
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
