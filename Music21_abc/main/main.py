import argparse
from music21 import converter, analysis, stream, roman, note, chord
import pygame.midi
from pygame import midi
import tkinter as tk
from tkinter import filedialog
import sys
import time


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('-key', help='run Krumhansl algorithm on a music file', action="store_true")
    parser.add_argument('-rna', help='run roman numeral analysis on a music file', action="store_true")
    parser.add_argument('-rtc', help='run real-time chord naming', action="store_true")
    parser.add_argument('-out', help='read a file and then write it out again', action="store_true")
    args = parser.parse_args()
    
    if not (args.rtc): 
        root = tk.Tk()
        root.withdraw()
        file_path = filedialog.askopenfilename()
        start = int(round(time.time() * 1000))
        score = converter.parse(file_path)
        read = int(round(time.time() * 1000))
        sys.stdout.write('file read in ' + str(read - start) + 'ms\n')

    if args.key or args.rna:
        key = analysis.discrete.analyzeStream(score, 'Krumhansl')
        keyTime = int(round(time.time() * 1000))
        sys.stdout.write(str(key) + ' established in ' + str(keyTime - read) + 'ms\n')
    
    if args.rna:
        delim = ""
        chordFilter = stream.filters.ClassFilter('Chord')
        sIter = score.recurse().iter
        sIter.addFilter(chordFilter)
        
        for a in sIter:
            root = a.root()
            rn = roman.romanNumeralFromChord(a,key)
            sys.stdout.write(delim + str(rn.romanNumeralAlone))
            delim = " - "
    
        rnaTime = int(round(time.time() * 1000))
        sys.stdout.write(' determined in ' + str(rnaTime - read) + 'ms\n')
        
    if args.out:
        score.write('abc', 'abcout.abc')
        score.write('midi', 'midiout.mid')
        score.write('musicxml', 'xmlout.xml')
        write = int(round(time.time() * 1000))
        sys.stdout.write('file written in ' + str(write - read) + 'ms\n')
            
    if args.rtc:
        midi.init()
        controller = midi.Input(pygame.midi.get_default_input_id())
        c = chord.Chord()
        while True:
            if (controller.poll()):
                midi_event = controller.read(5)
                notePitch = midi_event[0][0][1]
                eventType = midi_event[0][0][0]
                n = note.Note(notePitch)
                if eventType == 144:
                    c.add(n)
                    sys.stdout.write(str(n) + '\n')
                    if c.isTriad():
                        sys.stdout.write(c.pitchedCommonName + '\n')
                    
                if c.pitchedCommonName == 'A4-minor triad':
                    break    
                        
                if eventType == 128:
                    c.remove(n)
                    c = chord.Chord(c)
                            
        
if __name__ == "__main__": main()