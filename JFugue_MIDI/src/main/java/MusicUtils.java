package main.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.jfugue.theory.Note;

public class MusicUtils {
	
	public static Set<Pair<Integer, Integer>> qualities() {
		final Set<Pair<Integer, Integer>> CHORD_QUALITIES = new HashSet<>();
		CHORD_QUALITIES.add(new Pair<>(3,7));
		CHORD_QUALITIES.add(new Pair<>(4,7));
		CHORD_QUALITIES.add(new Pair<>(3,6));
		return CHORD_QUALITIES;
	}
	
	public static HashMap<Integer, String> majorNumerals() {
		final HashMap<Integer, String> ROMAN_NUMERALS_MAJ = new HashMap<>();
		ROMAN_NUMERALS_MAJ.put(0, "I");
		ROMAN_NUMERALS_MAJ.put(2, "ii");
		ROMAN_NUMERALS_MAJ.put(4, "iii");
		ROMAN_NUMERALS_MAJ.put(5, "IV");
		ROMAN_NUMERALS_MAJ.put(7, "V");
		ROMAN_NUMERALS_MAJ.put(9, "vi");
		ROMAN_NUMERALS_MAJ.put(11, "vii" + "\u00b0");
		return ROMAN_NUMERALS_MAJ;
	}
	
	public static HashMap<Integer, String> minorNumerals() {
		final HashMap<Integer, String> ROMAN_NUMERALS_MIN = new HashMap<>();
		ROMAN_NUMERALS_MIN.put(0, "i");
		ROMAN_NUMERALS_MIN.put(2, "ii" + "\u00b0");
		ROMAN_NUMERALS_MIN.put(3, "b" + "III");
		ROMAN_NUMERALS_MIN.put(5, "iv");
		ROMAN_NUMERALS_MIN.put(7, "v");
		ROMAN_NUMERALS_MIN.put(8, "b" + "VI");
		ROMAN_NUMERALS_MIN.put(10, "b" + "VII");
		return ROMAN_NUMERALS_MIN;
	}
	
	
	public static int semitonesBetween(Note n, Note m) {
		if (m.getValue() > n.getValue()) {
			return (m.getValue() - n.getValue()) % 12;
		}
		return (n.getValue() - m.getValue()) % 12;
	}

	public static String findChord(List<Note> currentNotesBeingPlayed) {
		for (int inv = 0; inv < 3; inv++) {
			
			int first = MusicUtils.semitonesBetween(currentNotesBeingPlayed.get(0), currentNotesBeingPlayed.get(1));
			int second = MusicUtils.semitonesBetween(currentNotesBeingPlayed.get(0), currentNotesBeingPlayed.get(2));
			
			if (MusicUtils.qualities().contains(new Pair<>(first, second))) {
				if (first == 3 && second == 7) {
					System.out.println("found - " + currentNotesBeingPlayed.get(0).getToneString() + "min");
					return currentNotesBeingPlayed.get(0).getToneString() + "min";
				}
				else if (first == 4 && second == 7) {
					System.out.println("found - " + currentNotesBeingPlayed.get(0).getToneString() + "maj");
					return currentNotesBeingPlayed.get(0).getToneString() + "maj";
				}
				else {
					System.out.println("found - " + currentNotesBeingPlayed.get(0).getToneString() + "dim");
					return currentNotesBeingPlayed.get(0).getToneString() + "dim";
				}
					
			}
			else
			{
				currentNotesBeingPlayed.set(0, new Note(currentNotesBeingPlayed.get(0).getValue() + 12));
				Collections.rotate(currentNotesBeingPlayed, -1);
			}
		}

		return null;
	}
	
}
