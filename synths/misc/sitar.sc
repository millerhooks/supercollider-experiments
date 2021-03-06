s.boot;

(

x = SynthDef(\filtpulse, {arg note, gate;

var in,out,tone;
note = note + LFNoise2.ar(0.3, 1/16);
in = SinOsc.ar(note.midicps, phase: LocalIn.ar());
//LocalOut.ar(DelayC.ar(in, 1, 0.01) * 0.8);
LocalOut.ar(DelayC.ar(in, 1, 0.1 + LFNoise2.ar(1, 0.01)) * 0.85);
LocalOut.ar(DelayC.ar(in, 1, 0.05) * 0.7);
LocalOut.ar(Pulse.ar((note+7).midicps) * Line.ar(1,0,2));
//LocalOut.ar(SinOsc.ar((note+12).midicps) * Line.ar(1,0,3));
LocalOut.ar(LFTri.ar(note.midicps * 2) * Line.ar(1,0,1));
out = in + LocalIn.ar();

out = BHiPass.ar(out, (note+7).midicps, 0.3);
out = out * EnvGen.kr(Env.asr(0.1, 1, 3), gate, 1, doneAction: 2) * AmpComp.kr(note.midicps, 20.midicps) * 0.5;
Out.ar(0, out.dup * 0.09);

}).send(s);

)

(

var notes, on, off;
MIDIIn.connect;
notes = Array.newClear(128);  // array has one slot per possible MIDI note

on = Routine({

var event, newNode;

loop {

event = MIDIIn.waitNoteOn;

newNode = Synth(\filtpulse);
newNode.set(\note, event.b);
newNode.set(\gate, 1);
notes.put(event.b, newNode);

}

}).play;


off = Routine({

var event;

loop {

event = MIDIIn.waitNoteOff;

notes[event.b].set(\gate, 0);

}

}).play;

q = { on.stop; off.stop; };

)

q.value;
