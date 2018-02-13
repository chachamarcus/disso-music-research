package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.MidiUnavailableException;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.realtime.RealtimePlayer;
import org.jfugue.theory.Note;


public class ChordNameParserListener extends ParserListenerAdapter {
	
	private List<Note> currentNotesBeingPlayed = new ArrayList<Note>();
	private List<String> chords = new ArrayList<String>();
	private static final Logger log = Logger.getLogger("Timings");

	@Override
	public void onNotePressed(Note note) {
		try {
			RealtimePlayer player = new RealtimePlayer();
			player.play(note);
			log.info("note played at " + System.currentTimeMillis()/1000);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		currentNotesBeingPlayed.add(note);
		if (currentNotesBeingPlayed.size() == 3) {
			
			String lastChordPlayed = MusicUtils.findChord(currentNotesBeingPlayed);
			chords.add(lastChordPlayed);
		}
			
	}
	
	@Override
	public void onNoteReleased(Note note) {
		for (int i = 0; i < currentNotesBeingPlayed.size(); i++)
			if (currentNotesBeingPlayed.get(i).getToneString().equals(note.getToneString())) 
				currentNotesBeingPlayed.remove(i);
	}
	
	public List<String> chords() {
		return chords;
	}
	
}
