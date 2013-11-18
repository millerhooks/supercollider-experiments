s = Server.local;


s.sendMsg("/b_allocRead", 0, "/home/bjorn/audio_work/samples/j5.wav");

{
var index,out,notes,freq,pos,seq,pulse,start_frame,stutter,seq3,in,tt_freq;
in = AudioIn.ar(1);
tt_freq = ZeroCrossing.ar(in);
tt_freq = Lag.ar(tt_freq,0.003);
freq = 16 / BufDur.kr(0);
seq = Dseq([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15], inf);
seq3 = Drand([1/3,-1/3,1/4,-1/4,0], inf);
pulse = Impulse.kr(freq);
pos = Demand.kr(pulse,0,seq);
start_frame = (pos * BufFrames.kr(0)) / 16;
stutter = MouseY.kr(0,4).round(1);
index = Phasor.ar(pulse, BufRateScale.kr(0) * LinLin.ar(tt_freq,0,1500,0,1.5), start_frame, start_frame + (BufFrames.kr(0) / (8 * (stutter ** 2))), start_frame);
out = BufRd.ar(1, 0, index,1,4) * 0.5;
out = out + (pulse * 0.3);
Out.ar([0,1],Pan2.ar(out,Demand.kr(pulse,0,seq3)));
}.play;

{
var notes,note,trig,foo,width,in,tt_freq;
in = AudioIn.ar(1);
tt_freq = ZeroCrossing.ar(in);
tt_freq = Clip.ar(tt_freq,100,3000);
tt_freq = Lag.ar(tt_freq,0.03);
notes = Dseq([0, 3, 7, 3, 7, 10, 0, 3, 7, 0, 3, 5, -4, 0, 3, -7, -4, 0, -4, 0, 3, -7, -4, 0], inf);
trig = Impulse.ar(2);
note = Demand.ar(trig, 0, notes);
note = Lag.ar(note, 0.05) + 40 + (LFNoise2.ar(8) / 32);
width = LinExp.ar(LFNoise2.ar(8),0,1,0.7,0.8);
foo = LFSaw.ar(note.midicps) * (1/8);
foo = RLPF.ar(foo, LinLin.ar(tt_freq,100,1500,(note+(12*1.5)).midicps,(note+(12*3)).midicps), 0.05);
[foo,foo]
}.play();

