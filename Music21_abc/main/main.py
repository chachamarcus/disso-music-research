import argparse
from music21 import converter
import tkinter as tk;
from tkinter import filedialog

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('-key', help='run Krumhansl algorithm on an abc file', action="store_true")
    args = parser.parse_args()
    
    root = tk.Tk()
    root.withdraw()
    file_path = filedialog.askopenfilename()

    if (args.key):
        score = converter.parse(file_path)
        print(score)
    
if __name__ == "__main__": main()