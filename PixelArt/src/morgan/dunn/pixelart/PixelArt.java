package morgan.dunn.pixelart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import morgan.dunn.mycomponents.TitleLabel;

/**
 * Created: 10/26/18
 * Last Updated: 10/26/18
 * @author Morgan Dunn
 */

public class PixelArt extends JFrame {
//Main----------------------------------------
	public static void main(String[] args) 
	{
		//set up look and feel
		try
		{
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		}
		catch(Exception e) {}
		//run game
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new PixelArt();
			}
		});
	}
//Constants------------------------------------
	// serial ID for recreation
	private static final long serialVersionUID = 1L;
//Attributes-----------------------------------
	private GamePanel gamePanel;
	private InfoPanel infoPanel;
	private TitleScreen titlePanel;
	private EndScreen endPanel;
	private JPanel mainPanel;
	private Color c = new Color(137, 207, 240);
//Constructor----------------------------------
	public PixelArt()
	{
		//initialize gui
		initGUI();
		//get focus
		setFocusable(true);
		requestFocusInWindow();
		//Listen for certain keys
		addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				char key = e.getKeyChar();
				gamePanel.setKeyPressed(key);
			}	
			public void keyReleased(KeyEvent e)
			{
				char key = e.getKeyChar();
				gamePanel.setKeyReleased(key);
			}	
		});
		//Listen for mouse input
		addMouseListener(new MouseAdapter()
		{	//called when mouse is pressed
			public void mousePressed(MouseEvent e)
			{
				int mouseButton = e.getButton();
				gamePanel.setMousePressed(mouseButton);
			}
			//called when mouse is released	
			public void mouseReleased(MouseEvent e)
			{	
				int mouseButton = e.getButton();
				gamePanel.setMouseReleased(mouseButton);
			}
		});
		//initialize jframe
		setTitle("Pixel Art");
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
//Methods--------------------------------------
	public void initGUI()
	{
		//Title Panel
		TitleLabel titleLabel = new TitleLabel("Pixel Art", 137, 207, 240);
		add(titleLabel, BorderLayout.PAGE_START);
		//Title Screen-----------------
		titlePanel = new TitleScreen(this, c);
		//Main Panel-------------------
		mainPanel = new JPanel();
		mainPanel.setBackground(c);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//Game Panel
		gamePanel = new GamePanel(this);
		mainPanel.add(gamePanel);
		//Info Panel
		infoPanel = new InfoPanel(this);
		mainPanel.add(infoPanel);
		//End Panel--------------------
		endPanel = new EndScreen(this);
		endPanel.setBackground(c);
		add(titlePanel, BorderLayout.CENTER);
	}
	//add a button to end game
	public void fullPainting()
	{
		JButton endGame = new JButton("Finish Painting");
		
		endGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				endGame();
			}
		});
		
		endGame.setAlignmentX(RIGHT_ALIGNMENT);
		infoPanel.add(endGame);
		repaint();
		revalidate();
	}
	
	public void startGame()
	{
		remove(titlePanel);
		add(mainPanel, BorderLayout.CENTER);
		gamePanel.startTimer();
		pack();
		repaint();
		revalidate();
	}
	
	public void endGame()
	{
		remove(mainPanel);
		add(endPanel, BorderLayout.CENTER);
		endPanel.setPicture(gamePanel.getPicture());
		gamePanel.endMusic();
		gamePanel.stopTimer();
		pack();
		repaint();
		revalidate();
	}
	
	public void newGame()
	{
		remove(endPanel);
		remove(mainPanel);
		mainPanel.removeAll();
		gamePanel = new GamePanel(this);
		infoPanel = new InfoPanel(this);
		mainPanel.add(gamePanel);
		mainPanel.add(infoPanel);
		add(titlePanel, BorderLayout.CENTER);
		pack();
		repaint();
		revalidate();
	}
	
//Getters and Setters--------------------------
}
