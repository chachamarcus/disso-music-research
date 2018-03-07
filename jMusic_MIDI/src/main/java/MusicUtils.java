package main.java;

import java.util.HashMap;

import jm.music.tools.PhraseAnalysis;

public class MusicUtils {
	
	private static HashMap<String, Integer> midiNumberFromNote() {
		final HashMap<String, Integer> MIDI_NOTES = new HashMap<>();
		MIDI_NOTES.put("C", 0);
		MIDI_NOTES.put("C#", 1);
		MIDI_NOTES.put("D", 2);
		MIDI_NOTES.put("D#", 3);
		MIDI_NOTES.put("E", 4);
		MIDI_NOTES.put("F", 5);
		MIDI_NOTES.put("F#", 6);
		MIDI_NOTES.put("G", 7);
		MIDI_NOTES.put("G#", 8);
		MIDI_NOTES.put("A", 9);
		MIDI_NOTES.put("A#", 10);
		MIDI_NOTES.put("B", 11);
		return MIDI_NOTES;
	}
	
	private static HashMap<Integer, String> majorNumerals() {
		final HashMap<Integer, String> ROMAN_NUMERALS_MAJ = new HashMap<>();
		ROMAN_NUMERALS_MAJ.put(0, "I");
		ROMAN_NUMERALS_MAJ.put(1, "ii");
		ROMAN_NUMERALS_MAJ.put(2, "iii");
		ROMAN_NUMERALS_MAJ.put(3, "IV");
		ROMAN_NUMERALS_MAJ.put(4, "V");
		ROMAN_NUMERALS_MAJ.put(5, "vi");
		ROMAN_NUMERALS_MAJ.put(6, "vii" + "\u00b0");
		return ROMAN_NUMERALS_MAJ;
	}
	
	private static HashMap<Integer, String> minorNumerals() {
		final HashMap<Integer, String> ROMAN_NUMERALS_MIN = new HashMap<>();
		ROMAN_NUMERALS_MIN.put(0, "i");
		ROMAN_NUMERALS_MIN.put(1, "ii" + "\u00b0");
		ROMAN_NUMERALS_MIN.put(2, "b" + "III");
		ROMAN_NUMERALS_MIN.put(3, "iv");
		ROMAN_NUMERALS_MIN.put(4, "v");
		ROMAN_NUMERALS_MIN.put(5, "b" + "VI");
		ROMAN_NUMERALS_MIN.put(6, "b" + "VII");
		return ROMAN_NUMERALS_MIN;
	}
	
	public static int midiNumberFromNote(String key) {
		if (key.contains("#"))
			return midiNumberFromNote().get(key.substring(0, 2));
		return midiNumberFromNote().get(key.substring(0,1));
	}
	
	public static int[] tonalityFromString(String key) {
		if (key.endsWith("maj"))
			return PhraseAnalysis.MAJOR_SCALE;
		return PhraseAnalysis.MINOR_SCALE;
	}

	public static String intChordsToString(int[] chords, String key) {
		String prog = "";
		if (key.endsWith("maj")) {
			
			for (int chord : chords) 
				prog +=(" - " + majorNumerals().get(chord));
			
			return prog;
		}
		
		for (int chord : chords) 
			prog += (" - " + minorNumerals().get(chord));
		
		return prog;
	}
	
//	public static int semitonesBetween(Note n, Note m) {
//		if (m.getValue() > n.getValue()) {
//			return (m.getValue() - n.getValue()) % 12;
//		}
//		return (n.getValue() - m.getValue()) % 12;
//	}
//
//	public static String findChord(List<Note> currentNotesBeingPlayed) {
//		for (int inv = 0; inv < 3; inv++) {
//			
//			int first = MusicUtils.semitonesBetween(currentNotesBeingPlayed.get(0), currentNotesBeingPlayed.get(1));
//			int second = MusicUtils.semitonesBetween(currentNotesBeingPlayed.get(0), currentNotesBeingPlayed.get(2));
//			
//			if (MusicUtils.qualities().contains(new Pair<>(first, second))) {
//				if (first == 3 && second == 7) {
//					System.out.println("found - " + currentNotesBeingPlayed.get(0).getToneString() + "min");
//					return currentNotesBeingPlayed.get(0).getToneString() + "min";
//				}
//				else if (first == 4 && second == 7) {
//					System.out.println("found - " + currentNotesBeingPlayed.get(0).getToneString() + "maj");
//					return currentNotesBeingPlayed.get(0).getToneString() + "maj";
//				}
//				else {
//					System.out.println("found - " + currentNotesBeingPlayed.get(0).getToneString() + "dim");
//					return currentNotesBeingPlayed.get(0).getToneString() + "dim";
//				}
//					
//			}
//			else
//			{
//				currentNotesBeingPlayed.set(0, new Note(currentNotesBeingPlayed.get(0).getValue() + 12));
//				Collections.rotate(currentNotesBeingPlayed, -1);
//			}
//		}
//
//		return null;
//	}
	
}
