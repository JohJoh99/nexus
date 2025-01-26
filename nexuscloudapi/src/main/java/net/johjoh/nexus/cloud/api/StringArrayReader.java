package net.johjoh.nexus.cloud.api;

public class StringArrayReader {

	private String[] array;
	private int pointer = -1;
	
	public StringArrayReader(String[] array) {
		this.array = array.clone();
	}
	
	public String readNext() {
		pointer++;
		return array[pointer];
	}

}
