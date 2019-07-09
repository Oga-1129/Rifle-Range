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
	private int gamestate;//�Q�[���̏�Ԃ�\��
	private int difficulty;//�Q�[���̓�Փx
	private int clearrank = 0;//�����Փx(0�������,1��Normal�܂ŉ��,2��Hard�܂ŉ��)
	
	public BufferStrategy bstrategy;
	public JFrame frame1;
	public Sequencer midiseq = null;
	private int waittimer;
	
	
GameHoneGumi(int w, int h, String title){
	frame1 = new JFrame(title);
	frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//����{�^���������ꂽ���̐ݒ�
	frame1.setBackground(Color.BLACK);//�w�i�F
	frame1.setResizable(false);
	
	frame1.setVisible(true);
	Insets insets = frame1.getInsets();
	frame1.setSize(w + insets.left + insets.right, h + insets.top + insets.bottom);//(�����{���]���{�E�]���A�c���{��]���{���]��)
	frame1.setLocationRelativeTo(null);
	
	frame1.setIgnoreRepaint(false);//�o�b�t�@�����s���邩�̔���
	frame1.createBufferStrategy(2);//�o�b�t�@�̖����B����̓_�u���o�b�t�@�Ȃ̂ň�����2
	bstrategy = frame1.getBufferStrategy();
	
	frame1.addKeyListener(new MyKeyAdapter());//�L�[�{�[�h�����̃��\�b�h�̌Ăяo��
}
	
	public abstract void keyPressedGameMain(int keycode);//�L�[������������
	public abstract void keyReleasedGameMain(int keycode);//�L�[�𗣂�������
	
///��ʂ̏�����//////////////////////////////////////////////////////////////////////////////
	public abstract void initSelect();
	public abstract void initGameClear();
	public abstract void initGameOver();
	
////��ʂ̐؂�ւ�///////////////////////////////////////////////////////////////////////////
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
						//exit();//�Q�[���I��
					}
					break;
				case GS_GAMEOVER:
					if(keycode == KeyEvent.VK_R) {
						goSelect();
					}
					if(keycode == KeyEvent.VK_Q) {
						//exit();//�Q�[���I��
					}
					break;              
			}	
		}
		public void keyReleased(KeyEvent ev) {
			
		}
	}

/////��ʂ̕`��////////////////////////////////////////////////////////////////////////////////
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
		int fw = frame1.getWidth()/2;		//�E�B���h�E��/2
		FontMetrics fm = g.getFontMetrics();//������/2
		int strw = fm.stringWidth(str) /2;	//�����\��
		g.drawString(str, fw-strw, y);
	}
	
	void midiYomikomi(String fname) {
		if(midiseq == null) {
			try {
				midiseq = MidiSystem.getSequencer();//Midi�t�@�C�����Đ�����@�B
				midiseq.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		try {
			Sequence seq =
					MidiSystem.getSequence(getClass().getResource(fname));//Midi�t�@�C��
			midiseq.setSequence(seq);//�@�B�ƃt�@�C�����Ȃ���
		}catch(InvalidMidiDataException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	Clip otoYomikomi(String fname) {
		Clip clip = null;//try�u���b�N���̏������u���s����Ă��Ȃ��\��������v����
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
