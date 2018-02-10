package main.java;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.staccato.StaccatoParser;

public class Main { 

	public static void main(String[] args) throws IOException, InvalidMidiDataException {
		
		File file = chooseFile();
		if (file == null)
			System.exit(0);
		
		Pattern pattern = MidiFileManager.loadPatternFromMidi(file);
		
		KeySignatureParserListener parserListener = new KeySignatureParserListener();
		StaccatoParser parser = new StaccatoParser();
		
		parser.addParserListener(parserListener);
		parser.parse(pattern);
		String key = KrumhanslSchmuckler.calculateKey(parserListener.getFrequency());
		System.out.println("Calculated key - " + key);
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
