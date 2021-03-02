from tkinter import Tk, Canvas, Label
import tkinter
import csv
import subprocess
import sys
import random
from datetime import datetime

class Menu(Canvas):
    PATH = "./"
    # コンストラクタ
    def __init__(self, master):
        # 親クラスのコンストラクタ
        super().__init__(master, bg="white")
        self.grid(row=0, column=0, sticky=tkinter.N)
        self.menu_items = self.finput()
        self.master = master
        self.master.lift()

    def keyPress(self, ev):
        if ev.keysym in ['1','2','3','4','5','6','7','8','9']:
            print(ev.keysym)
            n = int(ev.keysym_num) - 49
            self.flog( str(n) )
            self.master.lower()
            subprocess.run( ['sh','./shell.sh', self.menu_items[n][1]])
            self.master.lift()
        elif ev.keysym in ['0']:
            self.flog( str(9) )
            self.master.lower()
            subprocess.run( ['sh','./shell.sh', self.menu_items[9][1]])
            self.master.lift()
        elif ev.keysym in ['A','a']:
            self.flog( str(10) )
            self.master.lower()
            subprocess.run( ['sh','./shell.sh', self.menu_items[10][1]])
            self.master.lift()
        elif ev.keysym in ['Q','q']:
            sys.exit()
 
    def menu_gen(self):
        for i, item in enumerate(self.menu_items):
            b = tkinter.Label(self, text=item[0], bg="white", font=("DSEG7 Classic", 20, "bold"))
            b.grid(row=i, column=0, ipady=2, sticky=tkinter.W)

    def finput(self):
        with open(Menu.PATH + 'menu.csv', 'r', encoding='utf-8') as f:
            reader = csv.reader(f)
            l = [row for row in reader]
        return l
    
    def flog(self, item='_'):
        fname = "log_"
        dt_now = str(datetime.now())
        with open(Menu.PATH + fname+item, mode='a') as f:
            f.write(dt_now + '\n')

    # 表示を更新
    def update(self):
        # 現在日時を表示
        now = datetime.now()
        str = now.strftime("%Y/%m/%d (%a.)") + " = " + now.strftime("%H:%M") + ":" + now.strftime("%S")
        self.master.title( str )
        # 1秒後に再表示
        self.master.after(1000, self.update)

# 単独処理の場合
def main():
    # メインウィンドウ作成
    root=Tk()
    # メインウィンドウタイトル
    root.title("Menu")
    # メインウィンドウサイズ
    root.geometry("400x500")
    # メインウィンドウの最大化
    #root.attributes("-zoom", "1")
    #root.attributes("-fullscreen", True)
    # 常に最前面に表示
    #root.attributes("-topmost", True)
    # メインウィンドウの背景色
    root.configure(bg="white")
    # Menu クラスのインスタンスを生成
    menu=Menu(root)
    menu.menu_gen()
    root.bind("<Key>", menu.keyPress)
    # 画面に配置
    menu.pack(expand=1)
    # 表示の更新を開始（update メソッド呼び出し）
    menu.update()
    # メインループ
    root.mainloop()

# import Menu による呼び出しでなければ単独処理 main() を実行
if __name__ == "__main__":
    main()
