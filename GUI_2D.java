import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GUI_2D extends Frame {

	boolean ready = true, rotate = false;
	float x0, y0, rWidth = 10.0F, rHeight = 7.5F, pixelSize;

	public static void main(String[] args) {
		new GUI_2D();
	}

	GUI_2D() {
		super("Rubik's Cube");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(600, 900);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add("Center", new mainGame());
		setVisible(true);
	}
}

@SuppressWarnings("serial")
class mainGame extends RubiksCube {

	public void paint(Graphics g) {
		initgr();
				
		g.drawRect(D,AA,50,50);
		g.drawRect(E,AA,50,50);
		g.drawRect(F,AA,50,50);

		g.drawRect(D,BB,50,50);
		g.drawRect(E,BB,50,50);
		g.drawRect(F,BB,50,50);
		
		g.drawRect(D,CC,50,50);
		g.drawRect(E,CC,50,50);
		g.drawRect(F,CC,50,50);
		
		g.drawRect(A,DD,50,50);
		g.drawRect(B,DD,50,50);
		g.drawRect(C,DD,50,50);
		g.drawRect(D,DD,50,50);
		g.drawRect(E,DD,50,50);
		g.drawRect(F,DD,50,50);
		g.drawRect(G,DD,50,50);
		g.drawRect(H,DD,50,50);
		g.drawRect(I,DD,50,50);
		
		g.drawRect(A,EE,50,50);
		g.drawRect(B,EE,50,50);
		g.drawRect(C,EE,50,50);
		g.drawRect(D,EE,50,50);
		g.drawRect(E,EE,50,50);
		g.drawRect(F,EE,50,50);
		g.drawRect(G,EE,50,50);
		g.drawRect(H,EE,50,50);
		g.drawRect(I,EE,50,50);
		
		g.drawRect(A,FF,50,50);
		g.drawRect(B,FF,50,50);
		g.drawRect(C,FF,50,50);
		g.drawRect(D,FF,50,50);
		g.drawRect(E,FF,50,50);
		g.drawRect(F,FF,50,50);
		g.drawRect(G,FF,50,50);
		g.drawRect(H,FF,50,50);
		g.drawRect(I,FF,50,50);
		
		g.drawRect(D,GG,50,50);
		g.drawRect(E,GG,50,50);
		g.drawRect(F,GG,50,50);

		g.drawRect(D,HH,50,50);
		g.drawRect(E,HH,50,50);
		g.drawRect(F,HH,50,50);
		
		g.drawRect(D,II,50,50);
		g.drawRect(E,II,50,50);
		g.drawRect(F,II,50,50);
		
		g.drawRect(D,JJ,50,50);
		g.drawRect(E,JJ,50,50);
		g.drawRect(F,JJ,50,50);

		g.drawRect(D,KK,50,50);
		g.drawRect(E,KK,50,50);
		g.drawRect(F,KK,50,50);
		
		g.drawRect(D,LL,50,50);
		g.drawRect(E,LL,50,50);
		g.drawRect(F,LL,50,50);
		
		g.setColor(colors[top[0]]);
		g.fillRect(D+1,AA+1,49,49);
		g.setColor(colors[top[1]]);
		g.fillRect(E+1,AA+1,49,49);
		g.setColor(colors[top[2]]);
		g.fillRect(F+1,AA+1,49,49);
		g.setColor(colors[top[7]]);
		g.fillRect(D+1,BB+1,49,49);
		g.setColor(Color.orange);
		g.fillRect(E+1,BB+1,49,49);
		g.setColor(colors[top[3]]);
		g.fillRect(F+1,BB+1,49,49);
		g.setColor(colors[top[6]]);
		g.fillRect(D+1,CC+1,49,49);
		g.setColor(colors[top[5]]);
		g.fillRect(E+1,CC+1,49,49);
		g.setColor(colors[top[4]]);
		g.fillRect(F+1,CC+1,49,49);
		
		g.setColor(colors[front[0]]);
		g.fillRect(D+1,DD+1,49,49);
		g.setColor(colors[front[1]]);
		g.fillRect(E+1,DD+1,49,49);
		g.setColor(colors[front[2]]);
		g.fillRect(F+1,DD+1,49,49);
		g.setColor(colors[front[7]]);
		g.fillRect(D+1,EE+1,49,49);
		g.setColor(Color.white);
		g.fillRect(E+1,EE+1,49,49);
		g.setColor(colors[front[3]]);
		g.fillRect(F+1,EE+1,49,49);
		g.setColor(colors[front[6]]);
		g.fillRect(D+1,FF+1,49,49);
		g.setColor(colors[front[5]]);
		g.fillRect(E+1,FF+1,49,49);
		g.setColor(colors[front[4]]);
		g.fillRect(F+1,FF+1,49,49);
		
		g.setColor(colors[bottom[0]]);
		g.fillRect(D+1,GG+1,49,49);
		g.setColor(colors[bottom[1]]);
		g.fillRect(E+1,GG+1,49,49);
		g.setColor(colors[bottom[2]]);
		g.fillRect(F+1,GG+1,49,49);
		g.setColor(colors[bottom[7]]);
		g.fillRect(D+1,HH+1,49,49);
		g.setColor(Color.red);
		g.fillRect(E+1,HH+1,49,49);
		g.setColor(colors[bottom[3]]);
		g.fillRect(F+1,HH+1,49,49);
		g.setColor(colors[bottom[6]]);
		g.fillRect(D+1,II+1,49,49);
		g.setColor(colors[bottom[5]]);
		g.fillRect(E+1,II+1,49,49);
		g.setColor(colors[bottom[4]]);
		g.fillRect(F+1,II+1,49,49);
		
		g.setColor(colors[back[0]]);
		g.fillRect(D+1,JJ+1,49,49);
		g.setColor(colors[back[1]]);
		g.fillRect(E+1,JJ+1,49,49);
		g.setColor(colors[back[2]]);
		g.fillRect(F+1,JJ+1,49,49);
		g.setColor(colors[back[7]]);
		g.fillRect(D+1,KK+1,49,49);
		g.setColor(Color.yellow);
		g.fillRect(E+1,KK+1,49,49);
		g.setColor(colors[back[3]]);
		g.fillRect(F+1,KK+1,49,49);
		g.setColor(colors[back[6]]);
		g.fillRect(D+1,LL+1,49,49);
		g.setColor(colors[back[5]]);
		g.fillRect(E+1,LL+1,49,49);
		g.setColor(colors[back[4]]);
		g.fillRect(F+1,LL+1,49,49);
		
		g.setColor(colors[left[0]]);
		g.fillRect(A+1,DD+1,49,49);
		g.setColor(colors[left[1]]);
		g.fillRect(B+1,DD+1,49,49);
		g.setColor(colors[left[2]]);
		g.fillRect(C+1,DD+1,49,49);
		g.setColor(colors[left[7]]);
		g.fillRect(A+1,EE+1,49,49);
		g.setColor(Color.green);
		g.fillRect(B+1,EE+1,49,49);
		g.setColor(colors[left[3]]);
		g.fillRect(C+1,EE+1,49,49);
		g.setColor(colors[left[6]]);
		g.fillRect(A+1,FF+1,49,49);
		g.setColor(colors[left[5]]);
		g.fillRect(B+1,FF+1,49,49);
		g.setColor(colors[left[4]]);
		g.fillRect(C+1,FF+1,49,49);
		
		g.setColor(colors[right[0]]);
		g.fillRect(G+1,DD+1,49,49);
		g.setColor(colors[right[1]]);
		g.fillRect(H+1,DD+1,49,49);
		g.setColor(colors[right[2]]);
		g.fillRect(I+1,DD+1,49,49);
		g.setColor(colors[right[7]]);
		g.fillRect(G+1,EE+1,49,49);
		g.setColor(Color.blue);
		g.fillRect(H+1,EE+1,49,49);
		g.setColor(colors[right[3]]);
		g.fillRect(I+1,EE+1,49,49);
		g.setColor(colors[right[6]]);
		g.fillRect(G+1,FF+1,49,49);
		g.setColor(colors[right[5]]);
		g.fillRect(H+1,FF+1,49,49);
		g.setColor(colors[right[4]]);
		g.fillRect(I+1,FF+1,49,49);
	
		
	}

}
