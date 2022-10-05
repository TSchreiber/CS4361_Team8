import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class RubiksCube extends Canvas implements MouseMotionListener {


	RubiksCube() {
		this.addMouseMotionListener(this);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				float X = fx(evt.getX()), Y = fy(evt.getY());
				startX = iX(X);
				startY = iY(Y);
				System.out.println(startX + " " + startY);
			}
			
			public void mouseReleased(MouseEvent evt) {
				float xA = fx(evt.getX()), yA = fy(evt.getY());
				endX = iX(xA);
				endY = iY(yA);
				movements();
			}
		});

	}
		
	int[] top  = {0,0,0,0,0,0,0,0};
	int[] front = {1,1,1,1,1,1,1,1};
	int[] bottom = {2,2,2,2,2,2,2,2};
	int[] back = {3,3,3,3,3,3,3,3};
	int[] left = {4,4,4,4,4,4,4,4};
	int[] right = {5,5,5,5,5,5,5,5};
	int[][] faces = {top, front, bottom, back, left, right};
	
	float pixelSize, rWidth = 10.0F, rHeight = 20.0F, x0, y0;
	int startX, startY, endX, endY, centerX, centerY, A = 50, B = 100, C = 150, D = 200, E = 250, F = 300, G = 350, H = 400, I = 450, AA = 50, BB = 100, CC = 150, DD = 200, EE = 250, FF = 300, GG = 350, HH = 400, II = 450, JJ = 500, KK = 550, LL = 600;
	Color[] colors = {Color.orange, Color.white, Color.red, Color.yellow, Color.green, Color.blue};
	boolean inside = false, quitPressed = false;
	int[] X = new int[2];
	int[] Y = new int[2];

	
	
	void initgr() {
		Dimension d = getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		centerX = maxX / 2;
		centerY = maxY / 2;
	}

	int iX(float x) {
		return Math.round(centerX + x / pixelSize);
	}

	int iY(float y) {
		return Math.round(centerY - y / pixelSize);
	}

	float fx(int x) {
		return (x - centerX) * pixelSize;
	}

	float fy(int y) {
		return (centerY - y) * pixelSize;
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		float xA = fx(evt.getX()), yA = fy(evt.getY());
		endX = iX(xA);
		endY = iY(yA);
	
	}
	
	public void mouseReleased(MouseEvent evt) {
		float xA = fx(evt.getX()), yA = fy(evt.getY());
		endX = iX(xA);
		endY = iY(yA);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent evt) {
		float X = fx(evt.getX()), Y = fy(evt.getY());
		startX = iX(X);
		startY = iY(Y);
	}
	
	public void movements() {
		
		//rotate right side
		if(F <= startX && endX <= G) {
			if(startY - 50 > endY)
				cw_right();
			else if(startY < endY - 50){
				cw_right();
				cw_right();
				cw_right();
			}
		}
		
		//rotate left side
		if(D <= startX && endX <= E) {
			if(startY < endY - 50)
				cw_left();
			else if(startY - 50 > endY){
				cw_left();
				cw_left();
				cw_left();
			}
		}
		
		//rotate front side
		if(G <= startX && endX <= H) {
			if(startY < endY - 50)
				cw_front();
			else if(startY - 50 > endY){
				cw_front();
				cw_front();
				cw_front();
			}
		}
		if(C <= startX && endX <= D) {
			if(startY - 50 > endY)
				cw_front();
			else if(startY < endY - 50) {
				cw_front();
				cw_front();
				cw_front();
			}
		}
		if(CC <= startY && endY <= DD) {
			if(startX < endX - 50)
				cw_front();
			else if(startX - 50 > endX) {
				cw_front();
				cw_front();
				cw_front();
			}
		}
		if(GG <= startY && endY <= HH) {
			if(startX - 50 > endX)
				cw_front();
			else if(startX < endX - 50) {
				cw_front();
				cw_front();
				cw_front();
			}
		}
		
		//rotate back side
		if(II <= startY && endY <= JJ) {
			if(startX < endX - 50)
				cw_back();
			else if(startX - 50 > endX) {
				cw_back();
				cw_back();
				cw_back();
			}
		}
		if(AA <= startY && endY <= BB) {
			if(startX - 50 > endX)
				cw_back();
			else if(startX < endX - 50) {
				cw_back();
				cw_back();
				cw_back();
			}
		}
		
		//rotate bottom
		if(FF <= startY && endY <= GG) {
			if(startX < endX - 50)
				cw_bottom();
			else if(startX - 50 > endX) {
				cw_bottom();
				cw_bottom();
				cw_bottom();
			}
		}
		if(JJ <= startY && endY <= KK) {
			if(startX - 50 > endX)
				cw_bottom();
			else if(startX < endX - 50) {
				cw_bottom();
				cw_bottom();
				cw_bottom();
			}
		}
		
		//rotate top
		if(DD <= startY && endY <= EE) {
			if(startX - 50 > endX)
				cw_top();
			else if(startX < endX - 50) {
				cw_top();
				cw_top();
				cw_top();
			}
		}
	}

	
	public void cw_front() {
		int[] new_front = {front[6], front[7], front[0], front[1], front[2], front[3], front[4], front[5]};
		int[] new_bottom = {right[6], right[7], right[0], bottom[3], bottom[4], bottom[5], bottom[6], bottom[7]};
		int[] new_left = {left[0], left[1], bottom[0], bottom[1], bottom[2], left[5], left[6], left[7]};
		int[] new_top = {top[0], top[1], top[2], top[3], left[2], left[3], left[4], top[7]};
		int[] new_right = {top[6], right[1], right[2], right[3], right[4], right[5], top[4], top[5]};
		front = new_front;
		bottom = new_bottom;
		left = new_left;
		top = new_top;
		right = new_right;
		repaint();
	}
	
	public void cw_top() {
		int[] new_top = {top[6], top[7], top[0], top[1], top[2], top[3], top[4], top[5]};
		int[] new_left = {front[0], front[1], front[2], left[3], left[4], left[5], left[6], left[7]};
		int[] new_front = {right[0], right[1], right[2], front[3], front[4], front[5], front[6], front[7]};
		int[] new_right = {back[4], back[5], back[6], right[3], right[4], right[5], right[6], right[7]};
		int[] new_back = {back[0], back[1], back[2], back[3], left[0], left[1], left[2], back[7]};
		top = new_top;
		left = new_left;
		front = new_front;
		right = new_right;
		back = new_back;
		repaint();
	}

	public void cw_bottom() {
		int[] new_bottom = {bottom[6], bottom[7], bottom[0], bottom[1], bottom[2], bottom[3], bottom[4], bottom[5]};
		int[] new_front = {front[0], front[1], front[2], front[3], left[4], left[5], left[6], front[7]};
		int[] new_right = {right[0], right[1], right[2], right[3], front[4], front[5], front[6], right[7]};
		int[] new_left = {left[0], left[1], left[2], left[3], back[0], back[1], back[2], left[7]};
		int[] new_back = {right[4], right[5], right[6], back[3], back[4], back[5], back[6], back[7]};
		bottom = new_bottom;
		front = new_front;
		right = new_right;
		left = new_left;
		back = new_back;
		repaint();
	}
	
	public void cw_back() {
		int[] new_back = {back[6], back[7], back[0], back[1], back[2], back[3], back[4], back[5]};
		int[] new_top = {right[2], right[3], right[4], top[3], top[4], top[5], top[6], top[7]};
		int[] new_right = {right[0], right[1], bottom[4], bottom[5], bottom[6], right[5], right[6], right[7]};
		int[] new_bottom = {bottom[0], bottom[1], bottom[2], bottom[3], left[6], left[7], left[0], bottom[7]};
		int[] new_left = {top[2], left[1], left[2], left[3], left[4], left[5], top[0], top[1]};
		back = new_back;
		top = new_top;
		right = new_right;
		bottom = new_bottom;
		left = new_left;
		repaint();
	}
	
	public void cw_right() {
		int[] new_right = {right[6], right[7], right[0], right[1], right[2], right[3], right[4], right[5]};
		int[] new_front = {front[0], front[1], bottom[2], bottom[3], bottom[4], front[5], front[6], front[7]};
		int[] new_top = {top[0], top[1], front[2], front[3], front[4], top[5], top[6], top[7]};
		int[] new_bottom = {bottom[0], bottom[1], back[2], back[3], back[4], bottom[5], bottom[6], bottom[7]};
		int[] new_back = {back[0], back[1], top[2], top[3], top[4], back[5], back[6], back[7]};
		right = new_right;
		front = new_front;
		top = new_top;
		bottom = new_bottom;
		back = new_back;
		repaint();
	}
	
	public void cw_left() {
		int[] new_left = {left[6], left[7], left[0], left[1], left[2], left[3], left[4], left[5]};
		int[] new_front = {top[0], front[1], front[2], front[3], front[4], front[5], top[6], top[7]};
		int[] new_top = {back[0], top[1], top[2], top[3], top[4], top[5], back[6], back[7]};
		int[] new_bottom = {front[0], bottom[1], bottom[2], bottom[3], bottom[4], bottom[5], front[6], front[7]};
		int[] new_back = {bottom[0], back[1], back[2], back[3], back[4], back[5], bottom[6], bottom[7]};
		left = new_left;
		front = new_front;
		top = new_top;
		bottom = new_bottom;
		back = new_back;
		repaint();

	}
}
