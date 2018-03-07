package main.java;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import jm.music.data.Note;
import jm.util.Play;

public class ChordReceiver implements Receiver {
	
	private List<Note> currentNotesBeingPlayed = new ArrayList<>();
	private List<String> chords = new ArrayList<String>();

	@Override
	public void send(MidiMessage message, long timeStamp) {
		
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;
			if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
				 Note note = new Note(shortMessage.getData1(), 1);
				 currentNotesBeingPlayed.add(note);
				 Play.midi(note);
			}
			else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
				Note note = new Note(shortMessage.getData1(), 1);
				
				for (int i = 0; i < currentNotesBeingPlayed.size(); i++)
					if (currentNotesBeingPlayed.get(i).getName().equals(note.getName())) 
						currentNotesBeingPlayed.remove(i);
				
			}
		}
		
		if (currentNotesBeingPlayed.size() == 3) {
			String lastChordPlayed = MusicUtils.findChord(currentNotesBeingPlayed);
			chords.add(lastChordPlayed);
		}
		
	}

	@Override
	public void close() {
		
	}

}
