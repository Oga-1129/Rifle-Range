package jp.riflerange.minigame;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public abstract class GameHoneGumi {
	public static final int GS_STARTGAMEN = 0;
	public static final int GS_SELECT = 1;
	public static final int GS_GAMEMAIN  = 2;
	public static final int GS_GAMECLEAR = 3;
	public static final int GS_GAMEOVER  = 4;
	private int gamestate;//ゲームの状態を表す
	private int difficulty;//ゲームの難易度
	private int clearrank = 0;//解放難易度(0が未解放,1がNormalまで解放,2がHardまで解放)
	
	public BufferStrategy bstrategy;
	public JFrame frame1;
	public Sequencer midiseq = null;
	private int waittimer;
	
	
GameHoneGumi(int w, int h, String title){
	frame1 = new JFrame(title);
	frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//閉じるボタンが押された時の設定
	frame1.setBackground(Color.BLACK);//背景色
	frame1.setResizable(false);
	
	frame1.setVisible(true);
	Insets insets = frame1.getInsets();
	frame1.setSize(w + insets.left + insets.right, h + insets.top + insets.bottom);//(横幅＋左余白＋右余白、縦幅＋上余白＋下余白)
	frame1.setLocationRelativeTo(null);
	
	frame1.setIgnoreRepaint(false);//バッファを実行するかの判定
	frame1.createBufferStrategy(2);//バッファの枚数。今回はダブルバッファなので引数は2
	bstrategy = frame1.getBufferStrategy();
	
	frame1.addKeyListener(new MyKeyAdapter());//キーボード処理のメソッドの呼び出し
}
	
	public abstract void keyPressedGameMain(int keycode);//キーを押した操作
	public abstract void keyReleasedGameMain(int keycode);//キーを離した操作
	
///画面の初期化//////////////////////////////////////////////////////////////////////////////
	public abstract void initSelect();
	public abstract void initGameClear();
	public abstract void initGameOver();
	
////画面の切り替え///////////////////////////////////////////////////////////////////////////
	void goStartGamen() {
		gamestate = GS_STARTGAMEN;
		Timer t = new Timer();
		t.schedule(new MyTimerTask(), 10, 30);
	}
	
	void goSelect() {
		initSelect();
		gamestate = GS_SELECT;
	}
	
	void goGameMain() {
		gamestate = GS_GAMEMAIN;
	}
	
	void goStageClear() {
		initGameClear();
		gamestate = GS_GAMECLEAR;
	}

	void goGameOver() {
		initGameOver();
		gamestate = GS_GAMEOVER;
	}

//////////////////////////////////////////////////////////////////////////////////////////////
	
	class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent ev) {
			int keycode = ev.getKeyCode();
			switch(gamestate) {
				case GS_STARTGAMEN:
					if(keycode == KeyEvent.VK_P) {
						goSelect();
					}
					break;
					
				case GS_SELECT:
					if(clearrank >= 0)
					{
						if(keycode == KeyEvent.VK_E) {
							goGameMain();
						}
					}
					if(clearrank >= 1)
					{	
						if(keycode == KeyEvent.VK_N) {
							goGameMain();
						}
					}
					if(clearrank == 2)
					{
						if(keycode == KeyEvent.VK_H) {
							goGameMain();
						}
					}
					break;
					
				case GS_GAMEMAIN:
					keyReleasedGameMain(keycode);
					break;
					
				case GS_GAMECLEAR:
					if(keycode == KeyEvent.VK_R) {
						goSelect();
					}
					if(keycode == KeyEvent.VK_Q) {
						//exit();//ゲーム終了
					}
					break;
				case GS_GAMEOVER:
					if(keycode == KeyEvent.VK_R) {
						goSelect();
					}
					if(keycode == KeyEvent.VK_Q) {
						//exit();//ゲーム終了
					}
					break;              
			}	
		}
		public void keyReleased(KeyEvent ev) {
			
		}
	}

/////画面の描画////////////////////////////////////////////////////////////////////////////////
	public abstract void runStartGamen(Graphics g);
	public abstract void runSelect(Graphics g);
	public abstract void runGameClear(Graphics g);		
	public abstract void runGameMain(Graphics g);
	public abstract void runGameOver(Graphics g);
	
/////////////////////////////////////////////////////////////////////////////////////////////
	
	class MyTimerTask extends TimerTask{
		public void run() {
			Graphics g = bstrategy.getDrawGraphics();
			if(bstrategy.contentsLost() == false) {
				Insets insets = frame1.getInsets();
				g.translate(insets.left, insets.top);
				
				switch(gamestate) {
					case GS_STARTGAMEN:
						runStartGamen(g);
						break;
						
					case GS_SELECT:
						runSelect(g);
						break;
						
					case GS_GAMEMAIN:
						runGameMain(g);
						break;
						
					case GS_GAMECLEAR:
						runGameClear(g);
						break;
						
					case GS_GAMEOVER:
						runGameOver(g);
						break;
				}
				bstrategy.show();
				g.dispose();
			}
		}
	}
	
	void drawStringCenter(String str, int y, Graphics g) {
		int fw = frame1.getWidth()/2;		//ウィンドウ幅/2
		FontMetrics fm = g.getFontMetrics();//文字列幅/2
		int strw = fm.stringWidth(str) /2;	//文字表示
		g.drawString(str, fw-strw, y);
	}
	
	void midiYomikomi(String fname) {
		if(midiseq == null) {
			try {
				midiseq = MidiSystem.getSequencer();//Midiファイルを再生する機械
				midiseq.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		try {
			Sequence seq =
					MidiSystem.getSequence(getClass().getResource(fname));//Midiファイル
			midiseq.setSequence(seq);//機械とファイルをつなげる
		}catch(InvalidMidiDataException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	Clip otoYomikomi(String fname) {
		Clip clip = null;//tryブロック内の処理が「実行されていない可能性がある」ため
		try {
			AudioInputStream aistream = AudioSystem.getAudioInputStream(getClass().getResource(fname));
			DataLine.Info info = new DataLine.Info(Clip.class, aistream.getFormat());
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(aistream);
			return clip;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}
}
