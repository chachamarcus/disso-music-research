package main.java;

import java.util.ArrayList;
import java.util.List;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;


public class ChordNameParserListener extends ParserListenerAdapter {
	
	private List<String> currentNotesBeingPlayed = new ArrayList<String>();
	private List<String> chordNames = new ArrayList<String>();

	@Override
	public void onNotePressed(Note note) {
		System.out.println(note + " pressed");
		currentNotesBeingPlayed.add(note.getToneString());
		if (currentNotesBeingPlayed.size() == 3) {
			// pass list of notes to figure outerer
			System.out.println("found chord" + currentNotesBeingPlayed);
		}
	}
	
	@Override
	public void onNoteReleased(Note note) {
		currentNotesBeingPlayed.remove(note.getToneString());
	}
	
	public List<String> getChordNames() {
		return chordNames;
	}
	
}
