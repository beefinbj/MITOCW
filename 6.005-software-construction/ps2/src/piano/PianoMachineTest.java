package piano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.sound.midi.MidiUnavailableException;

import midi.Midi;
import music.Pitch;

import org.junit.Test;

public class PianoMachineTest {
	
	PianoMachine pm = new PianoMachine();
	
    @Test
    public void singleNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	
        pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }
    
    @Test
    public void chordTest() throws MidiUnavailableException {
    	String expected1 = "on(61,PIANO) wait(0) on(63,PIANO) wait(100) off(63,PIANO) wait(0) off(61,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	midi.clearHistory();
    	
    	pm.beginNote(new Pitch(1));
    	pm.beginNote(new Pitch(3));
    	Midi.wait(100);
    	pm.endNote(new Pitch(3));
    	pm.endNote(new Pitch(1));
    	
    	System.out.println(midi.history());
    	assertEquals(expected1,midi.history());
    }
    
    @Test
    public void scaleTest() throws MidiUnavailableException {
    	String expected1 = "on(62,PIANO) wait(100) on(64,PIANO) wait(100) off(64,PIANO) wait(0) off(62,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	midi.clearHistory();
    	
    	pm.beginNote(new Pitch(2));
    	Midi.wait(100);
    	pm.beginNote(new Pitch(4));
    	Midi.wait(100);
    	pm.endNote(new Pitch(4));
    	pm.endNote(new Pitch(2));
    	
    	System.out.println(midi.history());
    	assertEquals(expected1,midi.history());
    }
    
    @Test
    public void lagTest() throws MidiUnavailableException {
    	String expected1 = "on(62,PIANO) wait(0) on(64,PIANO) wait(100) off(64,PIANO) wait(100) off(62,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	midi.clearHistory();
    	
    	pm.beginNote(new Pitch(2));
    	pm.beginNote(new Pitch(4));
    	Midi.wait(100);
    	pm.endNote(new Pitch(4));
    	Midi.wait(100);
    	pm.endNote(new Pitch(2));
    	
    	System.out.println(midi.history());
    	assertEquals(expected1,midi.history());
    }
 
    @Test
    public void oneChangeNoteTest() throws MidiUnavailableException {
    	String expected = "on(62,BRIGHT_PIANO) wait(100) off(62,BRIGHT_PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	pm.changeInstrument();
    	pm.beginNote(new Pitch(2));
    	Midi.wait(100);
    	pm.endNote(new Pitch(2));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());
    }

    @Test
    public void endChangeTest() throws MidiUnavailableException {
    	String expected = "on(64,CLARINET) wait(100) off(64,CLARINET) wait(100) on(62,GUNSHOT) wait(100) off(62,GUNSHOT) wait(100) on(61,PIANO) wait(100) off(61,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	for (int i = 0; i < 71; i++) {
    		pm.changeInstrument();
    	}
    	
    	pm.beginNote(new Pitch(4));
    	Midi.wait(100);
    	pm.endNote(new Pitch(4));
    	Midi.wait(100);
    	for (int j = 0; j < 56; j++) {
    		pm.changeInstrument();
    	}
    	pm.beginNote(new Pitch(2));
    	Midi.wait(100);
    	pm.endNote(new Pitch(2));
    	pm.changeInstrument();
    	Midi.wait(100);
    	pm.beginNote(new Pitch(1));
    	Midi.wait(100);
    	pm.endNote(new Pitch(1));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());
    }
    
    @Test
    public void upTest() throws MidiUnavailableException {
    	String expected = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) on(75,PIANO) wait(100) off(75,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	pm.beginNote(new Pitch(1));
    	Midi.wait(100);
    	pm.endNote(new Pitch(1));
    	Midi.wait(100);
    	pm.shiftUp();
    	pm.beginNote(new Pitch(3));
    	Midi.wait(100);
    	pm.endNote(new Pitch(3));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());
    }
    
    @Test
    public void downTest() throws MidiUnavailableException {
    	String expected = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) on(55,PIANO) wait(100) off(55,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	pm.beginNote(new Pitch(1));
    	Midi.wait(100);
    	pm.endNote(new Pitch(1));
    	Midi.wait(100);
    	pm.shiftDown();
    	pm.beginNote(new Pitch(7));
    	Midi.wait(100);
    	pm.endNote(new Pitch(7));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());
    }
    
    @Test
    public void upDownDownTest() throws MidiUnavailableException {
    	String expected = "on(73,PIANO) wait(100) off(73,PIANO) wait(100) on(55,PIANO) wait(100) off(55,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	pm.shiftUp();
    	pm.beginNote(new Pitch(1));
    	Midi.wait(100);
    	pm.endNote(new Pitch(1));
    	Midi.wait(100);
    	pm.shiftDown();
    	pm.shiftDown();
    	pm.beginNote(new Pitch(7));
    	Midi.wait(100);
    	pm.endNote(new Pitch(7));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());
    }
    
    @Test
    public void downUpUpTest() throws MidiUnavailableException {
    	String expected = "on(53,PIANO) wait(100) off(53,PIANO) wait(100) on(75,PIANO) wait(100) off(75,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	pm.shiftDown();
    	pm.beginNote(new Pitch(5));
    	Midi.wait(100);
    	pm.endNote(new Pitch(5));
    	Midi.wait(100);
    	pm.shiftUp();
    	pm.shiftUp();
    	pm.beginNote(new Pitch(3));
    	Midi.wait(100);
    	pm.endNote(new Pitch(3));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());
    }

    @Test
    public void downDownDownUpTest() throws MidiUnavailableException {
    	String expected = "on(41,PIANO) wait(100) off(41,PIANO) wait(100) on(51,PIANO) wait(100) off(51,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	pm.shiftDown();
    	pm.shiftDown();
    	pm.shiftDown();
    	pm.beginNote(new Pitch(5));
    	Midi.wait(100);
    	pm.endNote(new Pitch(5));
    	Midi.wait(100);
    	pm.shiftUp();
    	pm.beginNote(new Pitch(3));
    	Midi.wait(100);
    	pm.endNote(new Pitch(3));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());

    }

    @Test
    public void upUpUpDownTest() throws MidiUnavailableException {
    	String expected = "on(89,PIANO) wait(100) off(89,PIANO) wait(100) on(75,PIANO) wait(100) off(75,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	this.pm = new PianoMachine();
    	
    	midi.clearHistory();
    	
    	pm.shiftUp();
    	pm.shiftUp();
    	pm.shiftUp();
    	pm.beginNote(new Pitch(5));
    	Midi.wait(100);
    	pm.endNote(new Pitch(5));
    	Midi.wait(100);
    	pm.shiftDown();
    	pm.beginNote(new Pitch(3));
    	Midi.wait(100);
    	pm.endNote(new Pitch(3));
    	
    	System.out.println(midi.history());
    	assertEquals(expected,midi.history());

    }

    @Test
    public void recordTest() throws MidiUnavailableException {
    	String expected1 = "on(62,PIANO) wait(100) on(64,PIANO) wait(100) off(64,PIANO) wait(0) off(62,PIANO)";
    	
    	Midi midi = Midi.getInstance();
    	
    	midi.clearHistory();
    	
    	pm.toggleRecording();
    	pm.beginNote(new Pitch(2));
    	Midi.wait(100);
    	pm.beginNote(new Pitch(4));
    	Midi.wait(100);
    	pm.endNote(new Pitch(4));
    	pm.endNote(new Pitch(2));
    	pm.toggleRecording();
    	
    	
    	pm.beginNote(new Pitch(5));
    	Midi.wait(100);
    	pm.beginNote(new Pitch(7));
    	Midi.wait(100);
    	pm.endNote(new Pitch(7));
    	pm.endNote(new Pitch(5));
    	
    	
    	midi.clearHistory();
    	
    	pm.playback();
    	
    	System.out.println(midi.history());
    	assertEquals(expected1,midi.history());
    }
    
}
