import argparse
from music21 import converter, analysis, stream, roman
import tkinter as tk;
from tkinter import filedialog
import sys

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('-key', help='run Krumhansl algorithm on a music file', action="store_true")
    parser.add_argument('-rna', help='run roman numeral analysis on a music file', action="store_true")
    args = parser.parse_args()
    
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
            
    
if __name__ == "__main__": main()