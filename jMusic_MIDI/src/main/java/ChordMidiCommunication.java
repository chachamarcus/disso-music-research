package main.java;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.ShortMessage;

import jm.midi.MidiCommunication;
import jm.music.data.Note;
import jm.util.Play;

public class ChordMidiCommunication extends MidiCommunication {
	
	private List<Note> currentNotesBeingPlayed = new ArrayList<>();
	private List<String> chords = new ArrayList<String>();

	@Override
	public void handleMidiInput(int status, int channel, int data1, int data2) {
		
		if (status == ShortMessage.NOTE_ON) {
			 Note note = new Note(data1, 3);
			 currentNotesBeingPlayed.add(note);
			 Play.midi(note);
		}
		else if (status == ShortMessage.NOTE_OFF) {
			Note note = new Note(data1, 3);
			
			for (int i = 0; i < currentNotesBeingPlayed.size(); i++)
				if (currentNotesBeingPlayed.get(i).getName().equals(note.getName())) 
					currentNotesBeingPlayed.remove(i);
			
		}
		
		if (currentNotesBeingPlayed.size() == 3) {
			String lastChordPlayed = MusicUtils.findChord(currentNotesBeingPlayed);
			chords.add(lastChordPlayed);
		}
		
	}

}
