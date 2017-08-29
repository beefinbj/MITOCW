package piano;

import java.util.ArrayList;

import javax.sound.midi.MidiUnavailableException;

import midi.Midi;
import midi.Instrument;
import music.Pitch;
import music.NoteEvent;

public class PianoMachine {
	
	private Midi midi;
	private Instrument instrument;
	private int shift;
	private boolean recording;
	private String last_record;
	private long start_play, stop_play;
    
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
            instrument = Instrument.PIANO;
            shift = 0;
            recording = false;
            last_record = "";
            start_play = 0;
            stop_play = 0;
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
    }
    
    /**
     * Starts playing a note, if it is not playing yet
     * @param rawPitch the note to be played
     */
    
    public void beginNote(Pitch rawPitch) {
    	midi.beginNote(rawPitch.transpose(shift*12).toMidiFrequency(),this.instrument);

    }
    
    /**
     * Stops playing a note, if it is playing
     * @param rawPitch the note to stop playing
     */
    public void endNote(Pitch rawPitch) {
    	midi.endNote(rawPitch.transpose(shift*12).toMidiFrequency(),this.instrument);
    }
    
    /**
     * Changes to the next instrument on the list
     */
    public void changeInstrument() {
       	this.instrument = this.instrument.next();
    }
    
    /**
     * Shifts piano one octave up, unless already two up from default
     */
    public void shiftUp() {
    	if (shift < 2) {
    		shift++;
    	}
    }
    
    /**
     * Shifts piano one octave down, unless already two down from default
     */
    public void shiftDown() {
    	if (shift > -2) {
    		shift--;
    	}
    }
    
    /**
     * Toggle between starting new recording and ending current recording
     * @return whether PM is recording or not
     */
    public boolean toggleRecording() {
    	if (recording) {
    		last_record = midi.history();
    		recording = false;
    	}
    	else {
    		midi.clearHistory();
    		recording = true;
    	}
    	return recording;
    }
    
    public long start_time() {
    	return start_play;
    }
    
    public long stop_time() {
    	return stop_play;
    }
    
    
    /**
     * Play the last recorded clip
     */
    protected void playback() {
    	start_play = System.currentTimeMillis();
        for (String note:last_record.split(" ")) {
        	String[] specs = new String[3];
        	specs = note.split("\\)|\\(|,");
        	if (specs[0].matches("on")) {
        		instrument = Instrument.valueOf(specs[2]);
        		Pitch play = new Pitch(Integer.valueOf(specs[1])-60);
        		this.beginNote(play);
        	}
        	else if (specs[0].matches("off")) {
        		instrument = Instrument.valueOf(specs[2]);
        		Pitch play = new Pitch(Integer.valueOf(specs[1])-60);
        		this.endNote(play);
        	}
        	else if (specs[0].matches("wait")) {
        		Midi.wait(Integer.valueOf(specs[1]));
        	}
        }
        stop_play = System.currentTimeMillis();
    }

}
