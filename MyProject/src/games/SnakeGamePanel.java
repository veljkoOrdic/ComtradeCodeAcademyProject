package Games;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class SnakeGamePanel extends JPanel implements ActionListener{

	static final int screenWidth = 700;
	static final int screenHeight = 500;
	static final int unitSize = 35;
	static final int gameUnits = (screenWidth * screenHeight) / (unitSize * unitSize);
	static final int delay = 100;
	final int x[] = new int[gameUnits];
	final int y[] = new int[gameUnits];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	SnakeGamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}
	public void draw(Graphics graphics) {
		
		if(running) {
			// ako zelimo da imamo kockice po kojima se krece zmijica
//			for(int i = 0; i < screenWidth / unitSize; i++) {
//				graphics.drawLine(i * unitSize, 0, i * unitSize, screenHeight);
//				graphics.drawLine(0, i * unitSize, screenWidth, i * unitSize);
//			}
			
			graphics.setColor(Color.red);
			graphics.fillOval(appleX, appleY, unitSize, unitSize);
			
			
		
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					graphics.setColor(Color.yellow);
					graphics.fillRect(x[i], y[i], unitSize, unitSize);
				}
				else {
					graphics.setColor(new Color(255-255-70));
					graphics.fillRect(x[i], y[i], unitSize, unitSize);
				}			
			}
			graphics.setColor(Color.red);
			graphics.setFont( new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Score: " + applesEaten, 
					           (screenWidth - metrics.stringWidth("Score: " + applesEaten)) / 2,
					           graphics.getFont().getSize());
		}
		else {
			gameOver(graphics);
		}
		
	}
	public void newApple(){
		appleX = random.nextInt((int)(screenWidth / unitSize)) * unitSize;
		appleY = random.nextInt((int)(screenHeight / unitSize)) * unitSize;
	}
	public void move(){
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - unitSize;
			break;
		case 'D':
			y[0] = y[0] + unitSize;
			break;
		case 'L':
			x[0] = x[0] - unitSize;
			break;
		case 'R':
			x[0] = x[0] + unitSize;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		//ako ugrize zmijica sebe
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
	
		if(x[0] < 0) {
			running = false;
		}

		if(x[0] > screenWidth) {
			running = false;
		}

		if(y[0] < 0) {
			running = false;
		}

		if(y[0] > screenHeight) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics graphics) {
		graphics.setColor(Color.red);
		graphics.setFont( new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(graphics.getFont()); // da ovaj font izadje na ekranu
		graphics.drawString("Score: " + applesEaten, 
				           (screenWidth - metrics1.stringWidth("Score: " + applesEaten)) /2 ,
				           graphics.getFont().getSize());
		
		graphics.setColor(Color.red);
		graphics.setFont( new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(graphics.getFont());
		graphics.drawString("Game Over", (screenWidth - metrics2.stringWidth("Game Over")) / 2,
				                          screenHeight / 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent keyEvent) {
			switch(keyEvent.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}