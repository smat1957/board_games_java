#! /usr/bin/env python

import tkinter as Tk
from tkinter import ttk
import csv
import subprocess

class Frame(Tk.Frame):
    """ Frame with three label """

    def finput(self):
        with open('./menu.csv', 'r', encoding='utf-8') as f:
            reader = csv.reader(f)
            l = [row for row in reader]
        return l

    def radio(self, items, frame):
        rb=[]
        v1 = Tk.StringVar()
        for i, txt in enumerate(items):
            # Radiobutton
            rb1 = Tk.Radiobutton( frame,text=txt[0],value=txt[1],variable=v1)
            rb.append(rb1)
        return rb, v1

    def __init__(self, master=None):
        Tk.Frame.__init__(self, master)
        self.master.title('Board Games in java')
        self.master.grid_columnconfigure(1, weight=1)
        self.master.grid_rowconfigure(1, weight=1)
        # Style - Theme
        ttk.Style().theme_use('classic')
        # Frame
        frame1 = ttk.Frame(self, padding=10)
        # Label Frame
        label_frame = ttk.Labelframe(self,text='[ Select and Go! ]',padding=(10),style='My.TLabelframe')
        # Radio Button
        items = self.finput()
        rb, v1 = self.radio(items, label_frame)
        # Button
        button = ttk.Button( self,text='Go!',padding=(20, 5),\
                command=lambda : subprocess.run( ['sh','./shell.sh',v1.get()]) )
        # Layout
        #label_frame.grid_columnconfigure(1, weight=1)
        label_frame.grid(row=0, column=0)
        for i, r in enumerate(rb):
            r.grid(row=i, column=0,sticky=Tk.W)
        button.grid(row=1, pady=1)
        #button.grid_rowconfigure(1, weight=1)
   
 ##----------------
if __name__ == '__main__':
    f = Frame()
    f.pack(padx=5, pady=5)
    f.mainloop()
