package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

public class ChordParserListener extends ParserListenerAdapter {
	
	private static final double RHYTHM = 0.25;
	private Key key;
	private double currentTime = 0.00;
	private List<Note> currentChordNotes = new ArrayList<>();

	private String progressionString = "";
	
	public ChordParserListener(String key) {
		this.key = new Key(key);
		
	}
	
	@Override
	public void onTrackBeatTimeRequested(double timeBookmarkId) {
		
		if (timeBookmarkId - currentTime == RHYTHM || timeBookmarkId == Double.NEGATIVE_INFINITY) {
			currentTime = timeBookmarkId;
			Note hangover = null;
			
			if (timeBookmarkId != Double.NEGATIVE_INFINITY) {
				hangover = currentChordNotes.get(currentChordNotes.size() - 1); //we have to take the last note in the chord out due to the way timestamps work
				currentChordNotes.remove(hangover);
			}
				
			boolean match = false;
			
			for (int inv = 0; inv < 3; inv++) {
				
				int first = MusicUtils.semitonesBetween(currentChordNotes.get(0), currentChordNotes.get(1));
				int second = MusicUtils.semitonesBetween(currentChordNotes.get(0), currentChordNotes.get(2));
				
				if (MusicUtils.qualities().contains(new Pair<>(first, second))) {
					match = true;
					int scaleDegreeInt = MusicUtils.semitonesBetween(key.getRoot(), currentChordNotes.get(0));
					
					if (key.getScale().getDisposition() == 1)
						progressionString += " - " + MusicUtils.majorNumerals().get(scaleDegreeInt);
					else
						progressionString += " - " + MusicUtils.minorNumerals().get(scaleDegreeInt);
					break;
				}
				else
				{
					currentChordNotes.set(0, new Note(currentChordNotes.get(0).getValue() + 12));
					Collections.rotate(currentChordNotes, -1);
				}
			}
			
			if (!match) 
				progressionString += " - xxx";
			
			currentChordNotes.removeAll(currentChordNotes);
			
			if (timeBookmarkId != Double.NEGATIVE_INFINITY)
				currentChordNotes.add(hangover);
		}
		
	}
	
	@Override
	public void afterParsingFinished() {
		onTrackBeatTimeRequested(Double.NEGATIVE_INFINITY);
	}
	
	@Override
	public void onNoteParsed(Note note) {
		if (note.getOriginalString() != null) {
			currentChordNotes.add(note);
		}
	}

	
	public String getProgression() {
		return progressionString;
	}
}
