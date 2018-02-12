package main.java;

import java.util.ArrayList;
import java.util.List;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;


public class ChordNameParserListener extends ParserListenerAdapter {
	
	private List<Note> currentNotesBeingPlayed = new ArrayList<Note>();
	private List<String> chordNames = new ArrayList<String>();

	@Override
	public void onNotePressed(Note note) {
		System.out.println(note + " pressed");
		currentNotesBeingPlayed.add(note);
		if (currentNotesBeingPlayed.size() == 3) {
			// pass list of chords to figure outerer
			System.out.println("found chord" + currentNotesBeingPlayed);
		}
	}
	
	@Override
	public void onNoteReleased(Note note) {
		System.out.println(note + " released");
		currentNotesBeingPlayed.remove(note);
	}
	
	public List<String> getChordNames() {
		return chordNames;
	}
	
}
