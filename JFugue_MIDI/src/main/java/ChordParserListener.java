package main.java;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;

public class ChordParserListener extends ParserListenerAdapter {
	
	private static final double RHYTHM = 0.25;
	public ChordProgression progression = new ChordProgression("");
	
	public ChordParserListener(String key) {
		
	}
	
	@Override
	public void onTrackBeatTimeBookmarked(String timeBookmarkId) {
		
	}
	
	@Override
	public void onNoteParsed(Note note) {
		
		if (note.getOriginalString() != null) {
			
		}
	}
}
