package main.java;

import java.util.ArrayList;
import java.util.List;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;


public class ChordNameParserListener extends ParserListenerAdapter {
	
	List<Note> currentNotesBeingPlayed = new ArrayList<Note>();

	@Override
	public void onNotePressed(Note note) {
		System.out.println(note + " pressed");
		currentNotesBeingPlayed.add(note);
		if (currentNotesBeingPlayed.size() == 3) {
			System.out.println("found chord" + currentNotesBeingPlayed);
			// pass list of chords to figure outerer
		}
	}
	
	@Override
	public void onNoteReleased(Note note) {
		System.out.println(note + " released");
		currentNotesBeingPlayed.remove(note);
	}
	
}
