/*
 * PONG GAME REQUIREMENTS
 * This simple "tennis like" game features two paddles and a ball, 
 * the goal is to defeat your opponent by being the first one to gain 3 point,
 *  a player gets a point once the opponent misses a ball. 
 *  The game can be played with two human players, one on the left and one on 
 *  the right. They use keyboard to start/restart game and control the paddles. 
 *  The ball and two paddles should be red and separating lines should be green. 
 *  Players score should be blue and background should be black.
 *  Keyboard requirements:
 *  + P key: start
 *  + Space key: restart
 *  + W/S key: move paddle up/down
 *  + Up/Down key: move paddle up/down
 *  
 *  Version: 0.5
 */
package vn.vanlanguni.ponggame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

//import vn.vanlanguni.ponggame.PongPanel;
//import vn.vanlanguni.ponggame.MyDialogResult;
//import vn.vanlanguni.ponggame.SecondWindow;
//import vn.vanlanguni.ponggame.Settings;
import vn.vanlanguni.ponggame.MyDialogResult;

/**
 * 
 * @author Invisible Man
 *
 */
public class PongPanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = -1097341635155021546L;
	private int interval = 1000 / 60;

	private boolean showTitleScreen = true;
	private boolean playing;
	private boolean gameOver;

	/** Background. */
	private Color backgroundColor = Color.BLACK;
	ImageIcon imaPlaying, imaStart, imaOver;
	/** Start game */
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	Color buttonColor = Color.BLUE;
	Rectangle rect;
	Rectangle rect2;
	ImageIcon btnIcon = new ImageIcon("imagePongGame/button.png");
	ImageIcon iconSet = new ImageIcon("imagePongGame/iconSetting.png");
	ImageIcon iconSet2 = new ImageIcon("imagePongGame/iconSetting2.png");
	boolean hover;
	boolean pressed;
	// boolean dragged;
	boolean hover2;
	boolean pressed2;
	int w = 30, h = 30, h2 = 30, w2 = 100;
	int x = 445, y = 25, x2 = 190, y2 = 350;
	// int dx, dy;
	JLabel lbluser1 = new JLabel("Player 1"), lbluser2 = new JLabel("Player 2");
	String user1, user2;

	// Random +/-
	private int timeToDisplay;
	private boolean showRandom;
	private int xRan;
	private int yRan;
	private int lastHitBall;

	/** State on the control keys. */
	private boolean upPressed;
	private boolean downPressed;
	private boolean wPressed;
	private boolean sPressed;

	/** The ball: position, diameter */
	private int ballX = 250;
	private int ballY = 250;
	private int diameter = 35;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;
	ImageIcon imaBall1, imaBall2, imaBall3, imaBall;
	JRadioButton radBall1 = new JRadioButton("", true), radBall2 = new JRadioButton(), radBall3 = new JRadioButton();
	ButtonGroup btngBall = new ButtonGroup();
	JPanel pnlSelect = new JPanel();

	/** Player 1's paddle: position and size */
	private int playerOneX = 0;
	private int playerOneY = 250;
	private int playerOneWidth = 10;
	private int playerOneHeight = 60;
	ImageIcon paddle1 = new ImageIcon("imagePongGame/paddle1.png");

	/** Player 2's paddle: position and size */
	private int playerTwoX = 484;
	private int playerTwoY = 250;
	private int playerTwoWidth = 10;
	private int playerTwoHeight = 60;
	ImageIcon paddle2 = new ImageIcon("imagePongGame/paddle2.png");

	/** Speed of the paddle - How fast the paddle move. */
	private int paddleSpeed = 5;

	/** Player score, show on upper left and right. */
	private int playerOneScore;
	private int playerTwoScore;

	/** Construct a PongPanel. */
	public PongPanel() {
		setBackground(backgroundColor);

		// listen to key presses
		setFocusable(true);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);

		// call step() 60 fps
		timeToDisplay = ThreadLocalRandom.current().nextInt(5, 15 + 1) * 1000;
		Timer timer = new Timer(interval, this);
		timer.start();
	}

	/** Implement actionPerformed */
	public void actionPerformed(ActionEvent e) {
		step();
		if (radBall1.isSelected()) {
			// System.out.println(1);

			imaStart = new ImageIcon("imagePongGame/nen neon.jpg");
			imaPlaying = new ImageIcon("imagePongGame/amnhac4.jpg");
			imaBall = new ImageIcon("imagePongGame/cau2.png");
			imaOver = new ImageIcon("imagePongGame/imaOver.gif");

		} else if (radBall2.isSelected()) {
			imaPlaying = new ImageIcon("imagePongGame/anhdongnoen1.gif");
			imaBall = new ImageIcon("imagePongGame/cau3.png");
			imaStart = new ImageIcon("imagePongGame/anhdongnoen.gif");
			imaOver = new ImageIcon("imagePongGame/imaOver.gif");
		} else if (radBall3.isSelected()) {
			imaPlaying = new ImageIcon("imagePongGame/amnhac6.jpg");
			imaBall = new ImageIcon("imagePongGame/cau4.png");
			imaStart = new ImageIcon("imagePongGame/anhdongnoen.gif");
			imaOver = new ImageIcon("imagePongGame/imaOver.gif");
		}
	}

	/** Repeated task */
	public void step() {

		if (playing) {

			/* Playing mode */

			// move player 1
			// Move up if after moving, paddle is not outside the screen
			if (wPressed && playerOneY - paddleSpeed > 0) {
				playerOneY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (sPressed && playerOneY + playerOneHeight + paddleSpeed < getHeight()) {
				playerOneY += paddleSpeed;
			}

			// move player 2
			// Move up if after moving paddle is not outside the screen
			if (upPressed && playerTwoY - paddleSpeed > 0) {
				playerTwoY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (downPressed && playerTwoY + playerTwoHeight + paddleSpeed < getHeight()) {
				playerTwoY += paddleSpeed;
			}

			/*
			 * where will the ball be after it moves? calculate 4 corners: Left,
			 * Right, Top, Bottom of the ball used to determine whether the ball
			 * was out yet
			 */
			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameter + ballDeltaX;
			// FIXME Something not quite right here
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameter + ballDeltaY;

			// Player 1's paddle position
			int playerOneRight = playerOneX + playerOneWidth;
			int playerOneTop = playerOneY;
			int playerOneBottom = playerOneY + playerOneHeight;

			// Player 2's paddle position
			float playerTwoLeft = playerTwoX;
			float playerTwoTop = playerTwoY;
			float playerTwoBottom = playerTwoY + playerTwoHeight;

			// ball bounces off top and bottom of screen
			if (nextBallTop < 0) {
				ballDeltaY = 3;
			} else if (nextBallBottom > 475) {
				ballDeltaY = -3;
			}

			// will the ball go off the left side?
			if (nextBallLeft < playerOneRight) {
				// is it going to miss the paddle?
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {

					playerTwoScore++;

					// Player 2 Win, restart the game
					if (playerTwoScore == 3) {
						playing = false;
						gameOver = true;
					}
					ballX = 250;
					ballY = 250;
				} else {
					// dieu chinh goc bat cua bong paddle 1
					if (ballDeltaY == -3) {
						if (nextBallLeft <= playerOneTop + 15 || nextBallLeft >= playerOneBottom - 15) {
							ballDeltaY = -5;
						} else if (nextBallLeft < playerOneTop + 30) {
							ballDeltaY = -4;
						} else if (nextBallLeft < playerOneTop + 45) {
							ballDeltaY = -3;
						}
					} else {
						if (nextBallLeft <= playerOneTop + 15 || nextBallLeft >= playerOneBottom - 15) {
							ballDeltaY = 5;
						} else if (nextBallLeft < playerOneTop + 30) {
							ballDeltaY = 4;
						} else if (nextBallLeft < playerOneTop + 45) {
							ballDeltaY = 3;
						}
					}
					// If the ball hitting the paddle, it will bounce back
					// FIXME Something wrong here
					ballDeltaX *= -1;
				}
			}

			// will the ball go off the right side?
			if (nextBallRight > playerTwoLeft) {
				// is it going to miss the paddle?
				if (nextBallTop > playerTwoBottom || nextBallBottom < playerTwoTop) {

					playerOneScore++;

					// Player 1 Win, restart the game
					if (playerOneScore == 3) {
						playing = false;
						gameOver = true;
					}
					ballX = 250;
					ballY = 250;
				} else {
					// dieu chinh goc bat cua bong paddle 2
					if (ballDeltaY == -3) {
						if (nextBallRight <= playerTwoTop + 15 || nextBallRight >= playerTwoBottom - 15) {
							ballDeltaY = -5;
						} else if (nextBallRight < playerTwoTop + 30) {
							ballDeltaY = -4;
						} else if (nextBallRight < playerTwoTop + 45) {
							ballDeltaY = -3;
						}

					} else {
						if (nextBallRight <= playerTwoTop + 15 || nextBallRight >= playerTwoBottom - 15) {
							ballDeltaY = 5;
						} else if (nextBallRight < playerTwoTop + 30) {
							ballDeltaY = 4;
						} else if (nextBallRight < playerTwoTop + 45) {
							ballDeltaY = 3;
						}

					}

					// If the ball hitting the paddle, it will bounce back
					// FIXME Something wrong here
					ballDeltaX *= -1;
					// ballDeltaY *= -1;
				}
			}

			// move the ball
			ballX += ballDeltaX;
			ballY += ballDeltaY;

			timeToDisplay -= interval;
			System.out.format("%d x: %d - y: %d\n", timeToDisplay, xRan, yRan);
			if (timeToDisplay < 0) {
				if (showRandom == false) {
					showRandom = true;
					xRan = ThreadLocalRandom.current().nextInt(50, 450 + 1);
					yRan = ThreadLocalRandom.current().nextInt(0, 450 + 1);
				} else {
					Point ballCenter = new Point(ballX + diameter / 2, ballY + diameter / 2);
					Point ranCenter = new Point(xRan + 15, yRan + 15);
					double distance = getPointDistance(ballCenter, ranCenter);
					if (distance < diameter / 2 + 15) {
						showRandom = false;
						timeToDisplay = ThreadLocalRandom.current().nextInt(5, 15 + 1) * 1000;
						if (lastHitBall == 1) {
							playerOneHeight /= 2;
						} else if (lastHitBall == 2) {
							playerTwoHeight /= 2;
						}
					}
				}
				if (timeToDisplay < -6000) {
					showRandom = false;
					timeToDisplay = ThreadLocalRandom.current().nextInt(5, 15 + 1) * 1000;
				}
			}

		}

		// stuff has moved, tell this JPanel to repaint itself
		repaint();
	}

	public double getPointDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	/** Paint the game screen. */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (showTitleScreen) {
			// select ball
			pnlSelect.setLayout(null);
			pnlSelect.setOpaque(false);
			btngBall.add(radBall1);
			btngBall.add(radBall2);
			btngBall.add(radBall3);

			pnlSelect.add(radBall1);
			pnlSelect.add(radBall2);
			pnlSelect.add(radBall3);

			radBall1.setBounds(0, 0, 20, 20);
			radBall2.setBounds(60, 0, 20, 20);
			radBall3.setBounds(120, 0, 20, 20);

			radBall1.setContentAreaFilled(false);
			radBall2.setContentAreaFilled(false);
			radBall3.setContentAreaFilled(false);

			this.add(pnlSelect);
			pnlSelect.setBounds(170, 310, 200, 25);
			pnlSelect.setVisible(true);
			// background screen
			g.drawImage(imaStart.getImage(), 0, 0, getWidth(), getHeight(), null);
			// draw ball list
			imaBall1 = new ImageIcon("imagePongGame/cau2.png");
			imaBall2 = new ImageIcon("imagePongGame/cau3.png");
			imaBall3 = new ImageIcon("imagePongGame/cau4.png");
			g.drawImage(imaBall1.getImage(), 160, 260, 40, 40, null);
			g.drawImage(imaBall2.getImage(), 220, 260, 40, 40, null);
			g.drawImage(imaBall3.getImage(), 280, 260, 40, 40, null);

			/* Setting username */
			rect = new Rectangle(x, y, w, h);
			if (hover) {
				if (pressed) {
					g.drawImage(iconSet.getImage(), x, y, null);
				} else {
					g.drawImage(iconSet.getImage(), x, y, null);
				}
			} else {
				g.drawImage(iconSet2.getImage(), x, y, null);
			}
			// draw Start game
			rect2 = new Rectangle(x2, y2, w2, h2);
			g.setFont(new Font("Tahoma", Font.BOLD, 15));
			if (hover2) {
				if (pressed2) {
					g.drawImage(btnIcon.getImage(), x2, y2, x2 + w2, y2 + h2, 0, 214, 371, 214 + 106, null);
					g.setColor(Color.RED);

				} else {
					g.drawImage(btnIcon.getImage(), x2, y2, x2 + w2, y2 + h2, 0, 0, 371, 108, null);
					g.setColor(Color.WHITE);
				}
			} else {
				g.drawImage(btnIcon.getImage(), x2, y2, x2 + w2, y2 + h2, 0, 108, 371, 108 + 106, null);
				g.setColor(Color.WHITE);
			}
			g.setFont(new Font("Tahoma", Font.BOLD, 15));
			g.drawString("Start!!!", x2 + 30, y2 + 19);

			/* Show welcome screen */

			// Draw game title and start message
			g.setColor(Color.BLUE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString("Pong Game", 130, 100);

			// FIXME Wellcome message below show smaller than game title
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
			g.drawString("Press 'P' to play.", 180, 400);
			//
			lbluser1.setVisible(false);
			lbluser2.setVisible(false);
		} else if (playing) {
			// disable select ball
			pnlSelect.setVisible(false);
			// background playing
			g.drawImage(imaPlaying.getImage(), 0, 0, 500, 500, Color.black, null);

			/* Game is playing */

			// set the coordinate limit
			int playerOneRight = playerOneX + playerOneWidth;
			int playerTwoLeft = playerTwoX - 1;

			// draw dashed line down center
			for (int lineY = 0; lineY < getHeight(); lineY += 50) {
				g.setColor(Color.GREEN);
				g.drawLine(250, lineY, 250, lineY + 25);
			}

			// draw "goal lines" on each side
			g.drawLine(playerOneRight, 0, playerOneRight, getHeight());
			g.drawLine(playerTwoLeft, 0, playerTwoLeft, getHeight());

			// draw the scores
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.BLUE);
			g.drawString(String.valueOf(playerOneScore), 100, 100); // Player 1
																	// score
			g.drawString(String.valueOf(playerTwoScore), 400, 100); // Player 2
																	// score
			// set username
			add(lbluser1);
			add(lbluser2);
			lbluser1.setBounds(40, 20, 100, 40);
			lbluser2.setBounds(420, 20, 100, 40);
			lbluser1.setVisible(true);
			lbluser2.setVisible(true);
			// draw the ball
			g.drawImage(imaBall.getImage(), ballX, ballY, diameter, diameter, null);

			// draw the paddles

			g.fillRect(playerOneX, playerOneY, playerOneWidth, playerOneHeight);
			g.fillRect(playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight);

			// g.fillRect(playerOneX, playerOneY, playerOneWidth,
			// playerOneHeight);
			g.drawImage(paddle1.getImage(), playerOneX, playerOneY, playerOneWidth, playerOneHeight, Color.BLACK, null);
			// g.fillRect(playerTwoX, playerTwoY, playerTwoWidth,
			// playerTwoHeight);
			g.drawImage(paddle2.getImage(), playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight, Color.BLACK, null);
			if (showRandom) {
				g.fillOval(xRan, yRan, 30, 30);
			}

		} else if (gameOver) {
			// set username
			lbluser1.setForeground(Color.BLUE);
			lbluser2.setForeground(Color.BLUE);
			lbluser1.setBounds(40, 20, 100, 40);
			lbluser2.setBounds(420, 20, 100, 40);
			lbluser1.setVisible(true);
			lbluser2.setVisible(true);
			// disable select ball
			pnlSelect.setVisible(false);
			// background gameOver
			g.drawImage(imaOver.getImage(), 0, 0, getWidth(), getHeight(), null);

			/* Show End game screen with winner name and score */

			// Draw scores
			// TODO Set Blue color

			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.BLUE);
			g.drawString(String.valueOf(playerOneScore), 100, 100);
			g.drawString(String.valueOf(playerTwoScore), 400, 100);

			// Draw the winner name
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.drawString(user1 + " Wins!", 140, 200);
			} else {
				g.drawString(user2 + " Wins!", 140, 200);
			}

			// Draw Restart message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			// TODO Draw a restart message
			g.drawString("Press 'SpaceBar' to restart.", 130, 400);
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (showTitleScreen) {
			if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
				showTitleScreen = false;
				playing = true;
			}
		} else if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = true;
			}
		} else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameOver = false;
			showTitleScreen = true;
			playerOneY = 250;
			playerTwoY = 250;
			ballX = 250;
			ballY = 250;
			playerOneScore = 0;
			playerTwoScore = 0;
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			wPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			sPressed = false;
		}
	}

	public void Setting() {
		SecondWindow w = new SecondWindow();
		w.setLocationRelativeTo(PongPanel.this);
		w.setVisible(true);
		Settings s = w.getSetings();

		// Stop and wait for user input

		if (w.dialogResult == MyDialogResult.YES) {
			user1 = s.getUserName1();
			user2 = s.getUserName2();
			if (user1.length() > 0) {
				lbluser1.setText(user1);
			}
			if (user2.length() > 0) {
				lbluser2.setText(user2);
			}

		} else {
			System.out.println("User chose to cancel");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (rect.contains(e.getPoint())) {
			Setting();
		} else if (rect2.contains(e.getPoint())) {
			showTitleScreen = false;
			playing = true;
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// pressed = true;
		// if (rect.contains(e.getX(), e.getY())) {
		// dx = e.getX() - x;
		// dy = e.getY() - y;
		// dragged = true;
		// }
		// repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		// pressed = false;
		// dragged = false;
		// repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		// if (dragged && PongPanel.this.getBounds().contains(e.getPoint())) {
		// x = e.getX() - dx;
		// y = e.getY() - dy;
		// repaint();
		// System.out.format("Mouse x: %d , Mouse y: %d, dx: %d, dx: %d",
		// e.getX(), e.getY(), dx, dy);
		// }
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (rect.contains(e.getX(), e.getY())) {
			hover = true;
			// setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			hover = false;
			// setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		repaint();
		if (rect2.contains(e.getX(), e.getY())) {
			buttonColor = Color.RED;
			hover2 = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			buttonColor = Color.BLUE;
			hover2 = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		repaint();

	}

}
