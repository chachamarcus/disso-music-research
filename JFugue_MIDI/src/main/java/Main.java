package main.java;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfugue.devices.MusicTransmitterToParserListener;
import org.jfugue.devtools.MidiDevicePrompt;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.staccato.StaccatoParser;

public class Main { 
	
	private static final List<String> VALID_COMMANDS = Arrays.asList("key", "rna", "out", "rtc");
	private static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException {
		
		List<String> commands = Arrays.asList(args);
		
		if (Collections.disjoint(VALID_COMMANDS, commands)) {
			System.out.println("No Valid Commands Found.  Usage:");
			System.out.println("key - find the key of a piece");
			System.out.println("rna - get the roman numeral analysis of a piece");
			System.out.println("out - read a file and then write it out again");
			System.out.println("rtc - real time chord analysis");
			System.exit(0);
		}
		
		File file = chooseFile();
		if (file == null)
			System.exit(0);
		
		long startTime = System.currentTimeMillis();
		Pattern pattern = MidiFileManager.loadPatternFromMidi(file);
		long loadTime = System.currentTimeMillis();
		
		log.info("loaded midi file in " + (loadTime - startTime) + "ms");
		
		String key = "";
		
		if (commands.contains("out")) {
			MidiFileManager.savePatternToMidi(pattern, new File(file.getParentFile(), "out-" + file.getName()));
			long saveTime = System.currentTimeMillis();
			log.info("saving " + file.getName() + "-out at " + file.getParentFile().getAbsolutePath());
			log.info("saving took " + (saveTime - loadTime) + "ms");
		}
		
		if (commands.contains("key") || commands.contains("rna")) {
			KeySignatureParserListener parserListener = new KeySignatureParserListener();
			StaccatoParser parser = new StaccatoParser();
			
			parser.addParserListener(parserListener);
			parser.parse(pattern);
			key = KrumhanslSchmuckler.calculateKey(parserListener.getFrequency());
			long keyTime = System.currentTimeMillis();
			log.info("Calculated key - " + key + " in " + (keyTime - loadTime) + "ms");
		}
		
		if (commands.contains("rna")) {
			ChordParserListener parserListener = new ChordParserListener(key);
			StaccatoParser parser = new StaccatoParser();

			parser.addParserListener(parserListener);
			parser.parse(pattern);
			String rns = parserListener.getProgression();
			long rnaTime = System.currentTimeMillis();
			log.info("Progression" + rns + " in " + (rnaTime - loadTime) + "ms");
		}
		
		if (commands.contains("rtc")) {
			MusicTransmitterToParserListener transmitter = new MusicTransmitterToParserListener(MidiDevicePrompt.askForMidiDevice());
			ChordNameParserListener parserListener = new ChordNameParserListener();
			transmitter.addParserListener(parserListener);
			transmitter.startListening();
			
			while(!parserListener.getChordNames().contains("Amin")) {
				
				if (parserListener.getChordNames().contains("Cmaj")) {
					System.out.println("you found the secret chord");
				}
				
			}
			
			transmitter.stopListening();


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
