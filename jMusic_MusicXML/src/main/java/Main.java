package main.java;


import jm.music.data.Score;
import jm.util.Read;
import jm.util.View;

public class Main {
	
	public static void main(String[] args) {
		
		Score read = new Score("example");
		Read.midi(read);
		View.show(read);
	}

}
