package jp.riflerange.minigame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
//import javax.sound.sampled.Clip;

public class RifleRange extends GameHoneGumi {
	int cy = 200;
	boolean spkey = false;
	BufferedImage Start_img, Usage, Glock, Colt, Beretta, Scope, Hit, Target, Background, GameClear, GameClear_img, GameOver, GameOver_img;
	double speed;
	//1target[] target = new Target[3];//�N���X�̔z��̒�`
	//Clip clip1,clip2;	//otoYomikomi���\�b�h���Ăяo������
	long stagetimer;
	int tekikazu = 1;
	boolean shift = false;
	boolean up = false;
	boolean down  = false;
	boolean left = false;
	boolean right = false;

	public RifleRange() {
		super(600, 400, "RIFLE�ERANGE");//�R���X�g���N�^�̐擪�ɕK������
		try {
			Start_img = ImageIO.read(getClass().getResource("Start_back.png"));//�X�^�[�g���
			Usage = ImageIO.read(getClass().getResource("Usage_prohibited.png"));//�g�p�s�̃C���X�g
			Glock = ImageIO.read(getClass().getResource("Glock.png"));//��ՓxEasy�̃A�C�R��
			Colt = ImageIO.read(getClass().getResource("ColtGavament.png"));//��ՓxNormal�̃A�C�R��
			Beretta = ImageIO.read(getClass().getResource("Beretta.png"));//��ՓxHard�̃A�C�R��
			Scope = ImageIO.read(getClass().getResource("Scope.png"));//�X�R�[�v�̃C���X�g
			Hit = ImageIO.read(getClass().getResource("Hit.png"));//�e���̃C���X�g
			Target = ImageIO.read(getClass().getResource("Target.png"));//�I�̃C���X�g
			Background = ImageIO.read(getClass().getResource("Background.jpg"));//�w�i�̃C���X�g
			GameClear = ImageIO.read(getClass().getResource("GameClear.png"));//�����uGAMECLEAR�v
			GameClear_img = ImageIO.read(getClass().getResource("GameClear_img.png"));//���������l�̃C���X�g
			GameOver = ImageIO.read(getClass().getResource("GameOver.png"));//�����uGAMEOVER�v
			GameOver_img = ImageIO.read(getClass().getResource("GameOver_img.png"));//��������ł���l�̃C���X�g
		}catch(IOException e) {
			e.printStackTrace();
		}
		goStartGamen();//�^�C�}�[���X�^�[�g
	}

	@Override
	public void keyPressedGameMain(int keycode) {
		if(keycode == KeyEvent.VK_SPACE) {
			spkey = true;
		}
		if(keycode == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if(keycode == KeyEvent.VK_UP) {
			up = true;
		}
		if(keycode == KeyEvent.VK_DOWN) {
			down = true;
		}
		if(keycode == KeyEvent.VK_LEFT) {
			left = true;
		}
		if(keycode == KeyEvent.VK_RIGHT) {
			right = true;
		}
	}

	@Override
	public void keyReleasedGameMain(int keycode) {
		if(keycode == KeyEvent.VK_SPACE) {
			spkey = false;
		}	
		if(keycode == KeyEvent.VK_SHIFT) {
			shift = false;
		}
		if(keycode == KeyEvent.VK_UP) {
			up = false;
		}
		if(keycode == KeyEvent.VK_DOWN) {
			down = false;
		}
		if(keycode == KeyEvent.VK_LEFT) {
			left = false;
		}
		if(keycode == KeyEvent.VK_RIGHT) {
			right = false;
		}
	}

	@Override
	public void initSelect() {
		cy = 200;
		speed = 0;
		spkey = false;
		stagetimer = System.currentTimeMillis();//���ݎ����̑��
	}

	@Override
	public void initGameClear() {
	}

	@Override
	public void initGameOver() {
	}

	@Override
	public void runStartGamen(Graphics g) {
		g.drawImage(Start_img, 0, 0, frame1);
	}
	

	
	@Override
	public void runSelect(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 400);
	}
	
	@Override
	public void runGameMain(Graphics g) {
		if(spkey == true) {
			speed = speed - 0.25;
			/*
			if(clip2.isRunning() == false) {
				clip2.setFramePosition(0);//�����߂�
				clip2.start();
			}
			*/			
		}else {
			speed = speed + 0.25;
		}
		
		if(speed < -6) {
			speed = -6;
		}
		if(speed > 6) {
			speed = 6;
		}
		cy = cy + (int)speed;
		if(cy < 0) {
			cy = 0;
		}
		if(cy > 448) {
			goGameOver();//GameOver��ʂɈړ�
		}
	
		
		g.drawImage(Background, 0, 0, frame1);//�w�i�摜�̓ǂݍ���
		g.drawImage(Scope, 270, cy, frame1);//�X�R�[�v�̓ǂݍ���

	}
	
	@Override
	public void runGameClear(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 400);
		g.setColor(Color.BLUE);
		g.setFont(new Font("SansSerif", Font.BOLD, 60));
		drawStringCenter("�Q�[���N���A",220,g);
	}

	@Override
	public void runGameOver(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 400);
		g.setColor(Color.BLUE);
		g.setFont(new Font("SansSerif", Font.BOLD, 60));
		drawStringCenter("�Q�[���I�[�o�[",220,g);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		RifleRange fgm = new RifleRange();
	}

}
