package main.java;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jm.music.data.Score;
import jm.util.Read;

public class Main {

	private static final List<String> VALID_COMMANDS = Arrays.asList("key", "rna", "out", "rtc");
	private static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		
		List<String> commands = Arrays.asList(args);

		if (Collections.disjoint(VALID_COMMANDS, commands)) {
			System.out.println("No Valid Commands Found.  Usage:");
			System.out.println("key - find the key of a piece");
			System.out.println("rna - get the roman numeral analysis of a piece");
			System.out.println("out - read a file and then write it out again");
			System.out.println("rtc - real time chord analysis");
			System.exit(0);
		}

		Score score = new Score("loaded");
		String key = "";
		long startTime = 0;
		long loadTime = 0;
		File file = null;

		if (!(commands.size() == 1 && commands.contains("rtc"))) {
			file = chooseFile();
			if (file == null)
				System.exit(0);

			startTime = System.currentTimeMillis();
			Read.midi(score, file.getAbsolutePath());
			loadTime = System.currentTimeMillis();

			log.info("loaded midi file in " + (loadTime - startTime) + "ms");
		}
	}
	
	private static File chooseFile() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "MIDI", "mid", "midi");
	    chooser.setFileFilter(filter);
	    chooser.setCurrentDirectory(new File("."));
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	        return chooser.getSelectedFile();
	    }
	    else {
			return null;
		}
	    
	    
	}
}
