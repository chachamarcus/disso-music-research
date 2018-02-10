package main.java;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

public class Main {

	public static void main(String[] args) throws IOException, InvalidMidiDataException {
		Pattern pattern = MidiFileManager.loadPatternFromMidi(chooseFile());
		System.out.println(pattern);
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
