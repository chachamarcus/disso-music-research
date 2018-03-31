package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.ShortMessage;

import jm.midi.MidiCommunication;
import jm.music.data.Note;
import jm.util.Play;

public class ChordMidiCommunication extends MidiCommunication {
	
	private List<Note> currentNotesBeingPlayed = new ArrayList<>();
	private List<String> chords = new ArrayList<String>();
	private static final Logger log = Logger.getLogger("Timings");

	@Override
	public void handleMidiInput(int status, int channel, int data1, int data2) {
		
		if (status == ShortMessage.NOTE_ON) {
			 Note note = new Note(data1, 2);
			 currentNotesBeingPlayed.add(note);
			 log.info("note played at " + System.currentTimeMillis()/1000);
			 Play.midi(note);
		}
		else if (status == ShortMessage.NOTE_OFF) {
			Note note = new Note(data1, 2);
			
			for (int i = 0; i < currentNotesBeingPlayed.size(); i++)
				if (currentNotesBeingPlayed.get(i).getName().equals(note.getName())) 
					currentNotesBeingPlayed.remove(i);
			
		}
		
		if (currentNotesBeingPlayed.size() == 3) {
			String lastChordPlayed = MusicUtils.findChord(currentNotesBeingPlayed);
			chords.add(lastChordPlayed);
		}
		
	}
	
	public List<String> chords() {
		return chords;
	}

}
