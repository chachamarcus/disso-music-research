package main.java;

import java.util.ArrayList;
import java.util.List;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

public class ChordParserListener extends ParserListenerAdapter {
	
	private static final double RHYTHM = 0.25;
	private Key key;
	private double currentTime = 0.00;
	private List<Note> currentChordNotes = new ArrayList<>();
//	public List<Chord> progression = new ArrayList<>();
	
	public ChordParserListener(String key) {
		this.key = new Key(key);
	}
	
	@Override
	public void onTrackBeatTimeRequested(double timeBookmarkId) {

		if (timeBookmarkId - currentTime == RHYTHM) {
			currentTime = timeBookmarkId;
			Note hangover = currentChordNotes.get(currentChordNotes.size() - 1);
			currentChordNotes.remove(hangover);
			
			// find root of list
			// find diff to key
			
			currentChordNotes.removeAll(currentChordNotes);
			currentChordNotes.add(hangover);
		}
		
	}
	
	@Override
	public void onNoteParsed(Note note) {
		
		if (note.getOriginalString() != null) {
			currentChordNotes.add(note);
		}
	}
	
	private static int semitonesBetween(Note n, Note m) {
		// assume m is higher than n
		return m.getValue() - n.getValue();
	}
}
