package test.java;


import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import main.java.KrumhanslSchmuckler;

public class KrumhanslSchmucklerTest {
	
	private static String[] pitchClasses = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	private static double[] majorProfile = {6.35, 2.23, 3.45, 2.33, 4.38, 4.09, 2.52, 5.19, 2.39, 3.66, 2.29, 2.88};
	private static double[] minorProfile = {6.33, 2.68, 3.52, 5.38, 2.60, 3.53, 2.54, 4.75, 3.98, 2.69, 3.34, 3.17};

	private static HashMap<String, Double> perfectMajor = new HashMap<>();
	private static HashMap<String, Double> perfectMinor = new HashMap<>();
	
	@BeforeClass
	public static void setUp() {
		
		for (int i = 0; i < pitchClasses.length; i++) {
			perfectMajor.put(pitchClasses[i], majorProfile[i]);
			perfectMinor.put(pitchClasses[i], minorProfile[i]);
		}

	}
	
	@Test
	public void test() {
		assertEquals("Cmaj", KrumhanslSchmuckler.calculateKey(perfectMajor));
		assertEquals("Cmin", KrumhanslSchmuckler.calculateKey(perfectMinor));
	}

}
