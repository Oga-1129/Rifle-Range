package jp.riflerange.minigame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Target {
		private static final Image Target = null;
		int char_x, char_y;//座標
		BufferedImage bimage1;
		int waittime;
		boolean spkey = true;
		double speed;
		
		void syutugen() {
			char_x = 600;
			char_y = (int)(Math.random()*400);
			waittime = waittime + (int)(Math.random() * 30);
		}
		
		void draw(Graphics g, ImageObserver io) {
			if(waittime>0) {
				waittime = waittime - 1;
				return;
			}
			g.drawImage(Target, char_x, char_y, io);//表示
			char_x = char_x - 4;					 //移動
			if(char_x < -48) {						 //左端から出た時
				syutugen();
			}
			
			if(spkey == true) {
				speed = speed - 0.15;
			}else {
				speed = speed + 0.15;
			}
			if(speed > 4 || speed < -4) {
				spkey = !spkey;
			}
			char_y = char_y + (int)speed;
		}
		
		boolean isAtari(int x, int y) {//当たり判定
			int ax1 = x + 12;
			int ay1 = y + 12;
			int ax2 = x + 36;
			int ay2 = y + 36;
			int bx1 = char_x + 12;
			int by1 = char_y + 12;
			int bx2 = char_x + 36;
			int by2 = char_y + 36;
			
			if( ( ax1 < bx2) && (bx1 < ax2) && (ay1 < by2) && (by1 < ay2)) {
				return true;
			}else {
				return false;
			}
		}
	}
