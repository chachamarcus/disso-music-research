package main.java;

import java.util.HashMap;
import java.util.logging.Logger;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;
import org.staccato.StaccatoUtil;

public class KeySignatureParserListener extends ParserListenerAdapter{
	
	private Logger log = Logger.getLogger(KeySignatureParserListener.class.getName());
	
	private HashMap<String,Double> noteFrequency = new HashMap<String, Double>();
	
	@Override
	public void onKeySignatureParsed(byte key, byte scale) { 
		String keySig = StaccatoUtil.createKeySignatureElement(key, scale);
		log.info("key found on midi file = " + keySig);
	}
	
	@Override
	public void onNoteParsed(Note note) {
		
		if (note.getOriginalString() != null) {
			String parsed = Note.getToneStringWithoutOctave(note.getValue());
			noteFrequency.compute(parsed, (k, v) -> (v == null) ? note.getDuration() : v + note.getDuration());
		}
		
	}
	
	public HashMap<String,Double> getFrequency() {
		return noteFrequency;
	}
	
}
