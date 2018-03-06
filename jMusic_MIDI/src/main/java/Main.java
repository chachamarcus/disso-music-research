package main.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.ChordAnalysis;
import jm.util.Read;
import jm.util.Write;

public class Main {

	private static final List<String> VALID_COMMANDS = Arrays.asList("key", "rna", "out", "rtc");
	private static Logger log = Logger.getLogger(Main.class.getName());
	private static HashMap<String,Double> noteFrequency = new HashMap<String, Double>();

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException, IOException {
		
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
		
		if (commands.contains("out")) {
			System.out.println(file.getParentFile() + "\\out-" + file.getName());
			Write.midi(score,new FileOutputStream(file.getParentFile() + "\\out-" + file.getName()));
			long saveTime = System.currentTimeMillis();
			log.info("saving " + file.getName() + "-out at " + file.getParentFile().getAbsolutePath());
			log.info("saving took " + (saveTime - loadTime) + "ms");
		}
		
		if (commands.contains("key") || commands.contains("rna")) {
			
			for (Part p: score.getPartArray())
				for (Phrase ph : p.getPhraseArray())
					for (Note n: ph.getNoteArray())
						noteFrequency.compute(n.getName(), (k, v) -> (v == null) ? n.getDuration() : v + n.getDuration());
			
			key = KrumhanslSchmuckler.calculateKey(noteFrequency);
			long keyTime = System.currentTimeMillis();
			log.info("Calculated key - " + key + " in " + (keyTime - loadTime) + "ms");
		}
		
		if (commands.contains("rna")) {
			
			Phrase phrase = score.getPart(0).getPhrase(0);
			int tonic = MusicUtils.midiNumberFromNote(key);
			int[] scale = MusicUtils.tonalityFromString(key);
			int[] chords = ChordAnalysis.getSecondPassChords(phrase, 1.0, tonic, scale);
			String rns = MusicUtils.intChordsToString(chords, key);
			long rnaTime = System.currentTimeMillis();
			log.info("Progression" + rns + " in " + (rnaTime - loadTime) + "ms");
		}
		
		if (commands.contains("rtc")) {
			MidiDevice device = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[1]);
			Sequencer sequencer = MidiSystem.getSequencer();
			Transmitter transmitter = device.getTransmitter();
			Receiver receiver = sequencer.getReceiver();
			Sequence sequence = new Sequence(Sequence.PPQ, 24);
			Track track = sequence.createTrack();
			device.open();
			sequencer.open();
			transmitter.setReceiver(receiver);
			sequencer.setSequence(sequence);
			sequencer.setTickPosition(0);
			sequencer.recordEnable(track, -1);
			sequencer.startRecording();
//			System.out.println("recording");
//			Thread.sleep(5000);
//			sequencer.stopRecording();
//			System.out.println("stop recording");
//			Sequence tmp = sequencer.getSequence();
//			MidiSystem.write(tmp, 0, new File("MyMidiFile.mid"));
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
