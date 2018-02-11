package main.java;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.staccato.StaccatoParser;

public class Main {
	
	private static final List<String> VALID_COMMANDS = Arrays.asList("key", "rna", "out");
	private static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws IOException, InvalidMidiDataException {
		
		List<String> commands = Arrays.asList(args);
		
		if (Collections.disjoint(VALID_COMMANDS, commands)) {
			System.out.println("No Valid Commands Found.  Usage:");
			System.out.println("key - find the key of a piece");
			System.out.println("rna - get the roman numeral analysis of a piece");
			System.out.println("out - read a file and then write it out again");
			System.exit(0);
		}
		
		File file = chooseFile();
		if (file == null)
			System.exit(0);
		
		Pattern pattern = MidiFileManager.loadPatternFromMidi(file);
		
		String key = "";
		
		if (commands.contains("out")) {
			MidiFileManager.savePatternToMidi(pattern, new File(file.getParentFile(), "out-" + file.getName()));
			log.info("saving " + file.getName() + "-out at " + file.getParentFile().getAbsolutePath());
		}
		
		if (commands.contains("key") || commands.contains("rna")) {
			KeySignatureParserListener parserListener = new KeySignatureParserListener();
			StaccatoParser parser = new StaccatoParser();
			
			parser.addParserListener(parserListener);
			parser.parse(pattern);
			key = KrumhanslSchmuckler.calculateKey(parserListener.getFrequency());
			log.info("Calculated key - " + key);
		}
		
		if (commands.contains("rna")) {
			ChordParserListener parserListener = new ChordParserListener(key);
			StaccatoParser parser = new StaccatoParser();

			parser.addParserListener(parserListener);
			parser.parse(pattern);
			String rns = parserListener.getProgression();
			log.info("Progression" + rns);
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
