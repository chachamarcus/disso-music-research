import argparse
from music21 import converter, analysis, stream, roman, note;
import pygame.midi
from pygame import midi
import tkinter as tk
from tkinter import filedialog
import sys

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('-key', help='run Krumhansl algorithm on a music file', action="store_true")
    parser.add_argument('-rna', help='run roman numeral analysis on a music file', action="store_true")
    parser.add_argument('-rtc', help='run real-time chord naming', action="store_true")
    args = parser.parse_args()
    
    if not (args.rtc): 
        root = tk.Tk()
        root.withdraw()
        file_path = filedialog.askopenfilename()
        score = converter.parse(file_path)

    if args.key or args.rna:
        key = analysis.discrete.analyzeStream(score, 'Krumhansl')
        sys.stdout.write(str(key) + '\n')
    
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
            
    if args.rtc:
        midi.init()
        controller = midi.Input(pygame.midi.get_default_input_id())
       
        while True:
            if (controller.poll()):
                midi_event = controller.read(5)
                notePitch = midi_event[0][0][1]
                eventType = midi_event[0][0][0]
                if notePitch > 0 and eventType == 144:
                    n = note.Note(notePitch)
                    print(n)
                
            
        midi.quit()
if __name__ == "__main__": main()