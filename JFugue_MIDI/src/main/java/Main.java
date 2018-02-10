package main.java;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.parser.Parser;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.staccato.StaccatoParser;

public class Main {

	public static void main(String[] args) throws IOException, InvalidMidiDataException {
		
		Pattern pattern = MidiFileManager.loadPatternFromMidi(chooseFile());
		
		KeySignatureParserListener parserListener = new KeySignatureParserListener();
		StaccatoParser parser = new StaccatoParser();
		
		parser.addParserListener(parserListener);
		parser.parse(pattern);
	}
	
	private static File chooseFile() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "MIDI", "mid", "midi");
	    chooser.setFileFilter(filter);
	    chooser.setCurrentDirectory(new File("."));
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	        System.out.println("opening: " +
	                chooser.getSelectedFile().getName());
	    }
	    
	    return chooser.getSelectedFile();
	}
	
}
