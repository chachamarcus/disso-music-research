package main.java;

import java.util.HashMap;
import java.util.logging.Logger;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;
import org.staccato.StaccatoUtil;

public class KeySignatureParserListener extends ParserListenerAdapter{
	
	private Logger log = Logger.getLogger(KeySignatureParserListener.class.getName());
	
	public static HashMap<String,Double> noteFrequency = new HashMap<String, Double>();
	
	@Override
	public void onKeySignatureParsed(byte key, byte scale) { 
		String keySig = StaccatoUtil.createKeySignatureElement(key, scale);
		log.info("key found on midi file = " + keySig);
	}
	
	@Override
	public void onNoteParsed(Note note) { 
		noteFrequency.compute(Note.getToneStringWithoutOctave(note.getValue()), (k, v) -> (v == null) ? note.getDuration() : v + note.getDuration());
	}
	
	@Override
	public void afterParsingFinished() {
		for (String pitchClass : noteFrequency.keySet())
			log.info("pitch class - " + pitchClass + ", frequency - " + noteFrequency.get(pitchClass));
	}
	
}
