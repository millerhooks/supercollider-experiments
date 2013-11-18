SynthDef(\zap, {arg freq, vol, period;

var out, carrier, voices, width;

period = period * Rand(1.05,0.95);

out = VarSaw.ar(freq, 0, Line.ar(1,0,period)) + VarSaw.ar(freq * 3, 0, Line.ar(1,0,period * 2)) + LFTri.ar(freq / 2, mul: 0.3);
out = out / 3;
out = out * Line.ar(1,0,period, doneAction: 2) * Line.ar(0,1,0.03) * AmpComp.kr(freq, (70-24).midicps) * 0.5 * XFade2.ar(Silence.ar, out, (vol * 2) - 1);

out = MoogVCF.ar(out, Line.ar(80, 20000, period), 0.2) * 1.5;

width = 0;

Out.ar(0, Pan2.ar(out, Rand(-1 * width,width)));

}).send(s);

Synth(\zap, [\freq, 440, \vol, 1, \gate, 1, \period, 3]);

r = Routine {
	var count,note,cycle,period,new_period,a,b,new_a,new_b,a_note,b_note,c_note;
	period = 1/16;
	cycle = 32;
	count = 0;
	a = 1;
	b = 1;
	a_note = 40.midicps;
	b_note = 40.midicps * 4/3;
	period.postln;
	inf.do {
		if ((count % a) == 0, {
     			Synth(\zap, [\freq, a_note, \vol, 1 - (count / (a*b*cycle)), \period, a * period]); // Initially this "stream" of pulses is at full volume
			// Period of this stream is (a * period)
		},{});
		if ((count % b) == 0, {
     			Synth(\zap, [\freq, b_note, \vol, count / (a*b*cycle), \period, b * period]); // Starts silent, fades to full
			// Period of this stream is (b * period)
		},{}); 
		period.wait;
		count = count + 1;
		if (count == ((a*b*cycle)), {
     			count = 0; // reset count
			if (period <= 0.1,
			{new_a = 1; new_b = 4; "going down, getting too fast".postln; },
			{ if (2.rand == 0, {new_a = 3; new_b = 4; "going down".postln; }, {new_a = 2; new_b = 1; "going up".postln; }); });
			if ((b * period) >= (2), { new_a = 3; new_b = 1; "too slow, speeding up!".postln; } );
			new_period = (b * period) / new_a; // calculate new period to ensure it matches old

			period = new_period;
			a = new_a;
			b = new_b;

			period.postln; // report new period

			c_note = a_note; // switch a and b notes
			a_note = b_note;
			b_note = c_note * (4/3);
			if (b_note >= 250, { b_note = b_note / 2; "octave down".postln; } );
			b_note.postln;
		},{});
	}
};

r.play;
r.stop;
