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
	//1target[] target = new Target[3];//クラスの配列の定義
	//Clip clip1,clip2;	//otoYomikomiメソッドを呼び出す処理
	long stagetimer;
	int tekikazu = 1;
	boolean shift = false;
	boolean up = false;
	boolean down  = false;
	boolean left = false;
	boolean right = false;

	public RifleRange() {
		super(600, 400, "RIFLE・RANGE");//コンストラクタの先頭に必ず書く
		try {
			Start_img = ImageIO.read(getClass().getResource("Start_back.png"));//スタート画面
			Usage = ImageIO.read(getClass().getResource("Usage_prohibited.png"));//使用不可のイラスト
			Glock = ImageIO.read(getClass().getResource("Glock.png"));//難易度Easyのアイコン
			Colt = ImageIO.read(getClass().getResource("ColtGavament.png"));//難易度Normalのアイコン
			Beretta = ImageIO.read(getClass().getResource("Beretta.png"));//難易度Hardのアイコン
			Scope = ImageIO.read(getClass().getResource("Scope.png"));//スコープのイラスト
			Hit = ImageIO.read(getClass().getResource("Hit.png"));//銃痕のイラスト
			Target = ImageIO.read(getClass().getResource("Target.png"));//的のイラスト
			Background = ImageIO.read(getClass().getResource("Background.jpg"));//背景のイラスト
			GameClear = ImageIO.read(getClass().getResource("GameClear.png"));//文字「GAMECLEAR」
			GameClear_img = ImageIO.read(getClass().getResource("GameClear_img.png"));//勝利した人のイラスト
			GameOver = ImageIO.read(getClass().getResource("GameOver.png"));//文字「GAMEOVER」
			GameOver_img = ImageIO.read(getClass().getResource("GameOver_img.png"));//落ち込んでいる人のイラスト
		}catch(IOException e) {
			e.printStackTrace();
		}
		goStartGamen();//タイマーがスタート
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
		stagetimer = System.currentTimeMillis();//現在時刻の代入
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
				clip2.setFramePosition(0);//巻き戻し
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
			goGameOver();//GameOver画面に移動
		}
	
		
		g.drawImage(Background, 0, 0, frame1);//背景画像の読み込み
		g.drawImage(Scope, 270, cy, frame1);//スコープの読み込み

	}
	
	@Override
	public void runGameClear(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 400);
		g.setColor(Color.BLUE);
		g.setFont(new Font("SansSerif", Font.BOLD, 60));
		drawStringCenter("ゲームクリア",220,g);
	}

	@Override
	public void runGameOver(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 400);
		g.setColor(Color.BLUE);
		g.setFont(new Font("SansSerif", Font.BOLD, 60));
		drawStringCenter("ゲームオーバー",220,g);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		RifleRange fgm = new RifleRange();
	}

}
