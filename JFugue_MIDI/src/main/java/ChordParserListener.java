package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

public class ChordParserListener extends ParserListenerAdapter {
	
	private static final double RHYTHM = 0.25;
	private Key key;
	private double currentTime = 0.00;
	private List<Note> currentChordNotes = new ArrayList<>();
	private static final Set<Pair<Integer, Integer>> CHORD_QUALITIES = new HashSet<>();
	private static final HashMap<Integer, String> ROMAN_NUMERALS_MAJ = new HashMap<>();
	private static final HashMap<Integer, String> ROMAN_NUMERALS_MIN = new HashMap<>();
	private String progressionString = "";
	
	public ChordParserListener(String key) {
		this.key = new Key(key);
		CHORD_QUALITIES.add(new Pair<>(3,7));
		CHORD_QUALITIES.add(new Pair<>(4,7));
		CHORD_QUALITIES.add(new Pair<>(3,6));
		ROMAN_NUMERALS_MAJ.put(0, "I");
		ROMAN_NUMERALS_MAJ.put(2, "ii");
		ROMAN_NUMERALS_MAJ.put(4, "iii");
		ROMAN_NUMERALS_MAJ.put(5, "IV");
		ROMAN_NUMERALS_MAJ.put(7, "V");
		ROMAN_NUMERALS_MAJ.put(9, "vi");
		ROMAN_NUMERALS_MAJ.put(11, "vii" + "\u00b0");
		ROMAN_NUMERALS_MIN.put(0, "i");
		ROMAN_NUMERALS_MIN.put(2, "ii" + "\u00b0");
		ROMAN_NUMERALS_MIN.put(3, "\u266D" + "III");
		ROMAN_NUMERALS_MIN.put(5, "iv");
		ROMAN_NUMERALS_MIN.put(7, "v");
		ROMAN_NUMERALS_MIN.put(8, "\u266D" + "VI");
		ROMAN_NUMERALS_MIN.put(10, "\u266D" + "VII");
	}
	
	@Override
	public void onTrackBeatTimeRequested(double timeBookmarkId) {

		if (timeBookmarkId - currentTime == RHYTHM) {
			currentTime = timeBookmarkId;
			Note hangover = currentChordNotes.get(currentChordNotes.size() - 1); //we have to take the last note in the chord out due to the way timestamps work
			currentChordNotes.remove(hangover);
			
			int first = semitonesBetween(currentChordNotes.get(0), currentChordNotes.get(1));
			int second = semitonesBetween(currentChordNotes.get(0), currentChordNotes.get(2));
			
			if (CHORD_QUALITIES.contains(new Pair<>(first, second))) {
				int scaleDegreeInt = semitonesBetween(key.getRoot(), currentChordNotes.get(0));
				
				if (key.getScale().getDisposition() == 1)
					progressionString += " - " + ROMAN_NUMERALS_MAJ.get(scaleDegreeInt);
				else
					progressionString += " - " + ROMAN_NUMERALS_MIN.get(scaleDegreeInt);
			}
			else
			{
				progressionString += " - " + "skipped";
				// rotate notes and try again (max twice)
			}
			
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
		return (m.getValue() - n.getValue()) % 12;
	}
	
	public String getProgression() {
		return progressionString;
	}
}
