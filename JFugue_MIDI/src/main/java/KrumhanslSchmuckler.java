package main.java;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class KrumhanslSchmuckler {
	
	private static String[] pitchClasses = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	private static double[] majorProfile = {6.35, 2.23, 3.45, 2.33, 4.38, 4.09, 2.52, 5.19, 2.39, 3.66, 2.29, 2.88};
	private static double[] minorProfile = {6.33, 2.68, 3.52, 5.38, 2.60, 3.53, 2.54, 4.75, 3.98, 2.69, 3.34, 3.17};
	
	public static String calculateKey(HashMap<String, Double> frequencies)
	{
		Map<String, Double> sortedFrequencies = new TreeMap<>(frequencies);
		String currentBestMatch = "";
		double currentBestCorrelation = Double.NEGATIVE_INFINITY;
		double[] pitchClassDurations = setupFrequencies(sortedFrequencies);
		PearsonsCorrelation correlation = new PearsonsCorrelation();
		
		for (String pitchClass : pitchClasses) {
			
			double maj = correlation.correlation(majorProfile, pitchClassDurations);
			
			if (maj > currentBestCorrelation) {
				currentBestCorrelation = maj;
				currentBestMatch = pitchClass + "maj";
			}
				
			
			double min = correlation.correlation(minorProfile, pitchClassDurations);
			
			if (min > currentBestCorrelation) {
				currentBestCorrelation = min;
				currentBestMatch = pitchClass + "min";
			}
				
			pitchClassDurations = rotateSemitone(pitchClassDurations);
		}
		
		return currentBestMatch;
	}
	
	private static double[] rotateSemitone(final double[] unorderedFrequencies) {
	    final int length = unorderedFrequencies.length;
	    final double[] rotated = new double[length];
	    for (int i = 0; i < length; i++) {
	        rotated[(i)] = unorderedFrequencies[(i + 1) % length];
	    }
	    return rotated;
	}
	
	private static double[] setupFrequencies(Map<String, Double> frequencies) 
	{
		double[] pitchClassDurations = new double[pitchClasses.length];
		int toneCount = 0;
		
		Map<String, Double> modFrequencies = new HashMap<>();
		
		for (String key : frequencies.keySet()) {
			switch (key) {
			case "Db" :
				modFrequencies.put("C#", frequencies.get(key));
				break;
			case "Eb" :
				modFrequencies.put("D#", frequencies.get(key));
				break;
			case "Gb" :
				modFrequencies.put("F#", frequencies.get(key));
				break;
			case "Ab" :
				modFrequencies.put("G#", frequencies.get(key));
				break;
			case "Bb" :
				modFrequencies.put("A#", frequencies.get(key));
				break;
			default :
				modFrequencies.put(key, frequencies.get(key));
			}
		}
		
		for (String pitch : pitchClasses) {
			if (!modFrequencies.containsKey(pitch)) {
				pitchClassDurations[toneCount] = 0.00; 
			}
			else {
				pitchClassDurations[toneCount] = modFrequencies.get(pitch);
			}
			toneCount++;
		}
				
		return pitchClassDurations;
	}
	
	
}
