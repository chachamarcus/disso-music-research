package main.java;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

public class MidiControllerChordListener {
	
	private MidiDevice device;
	private Transmitter transmitter;
	private ChordReceiver receiver;

	public MidiControllerChordListener() throws MidiUnavailableException, InvalidMidiDataException {
		receiver = new ChordReceiver();
		device = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[1]);
		transmitter = device.getTransmitter();
	}
	
	public void start() throws MidiUnavailableException {
		device.open();
		transmitter.setReceiver(receiver);
	}
	
	public void stop() {
		transmitter.close();
		device.close();
	}
	
	
}
