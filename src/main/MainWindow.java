package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainWindow {

	public class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand() == "navi back") {

				BufferedImage img;
				try {
					img = ImageIO.read(new URL(urlHistory.get(--currentElement)));
					imgLabel.setText("");
					imgLabel.setIcon(new ImageIcon(img));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (currentElement == 0)
					btnBack.setEnabled(false);

				btnFront.setEnabled(true);
			}

			if (e.getActionCommand() == "navi front") {

				BufferedImage img;
				try {
					img = ImageIO.read(new URL(urlHistory.get(++currentElement)));
					imgLabel.setText("");
					imgLabel.setIcon(new ImageIcon(img));

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (currentElement >= urlHistory.size() - 1)
					btnFront.setEnabled(false);
			}

			if (e.getActionCommand() == "random") {

				try {
					if (baseText.getText().isEmpty())
						generateUrl();
					else
						generateUrl(baseText.getText());

					System.out.println(urlString);
					Document doc = Jsoup.connect(urlString).userAgent("Mozilla").get();

					//System.out.println(doc.html());
					Element img_element = doc.getElementById("twitter:image:src");

					System.out.println(img_element.html());
					String img_url = img_element.attr("src");

					System.out.println(img_url);

					BufferedImage img = ImageIO.read(new URL(img_url));

					imgLabel.setText("");
					imgLabel.setIcon(new ImageIcon(img));

					if (img_url.compareTo(removedImgUrl) != 0) {
						addToQueue(img_url);
						btnFront.setEnabled(false);
						if (urlHistory.size() > 1)
							btnBack.setEnabled(true);
					}

				} catch (IOException ex) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
					System.out.println(ex.getMessage());

				} catch (NullPointerException ex2) {
					imgLabel.setText("NO IMAGE");
					imgLabel.setIcon(null);
					System.out.println(ex2.getMessage());

				} finally {
					curText.setText(urlString);
				}
			}

			frame.pack();

			System.out.println(currentElement);

		}

	}

	private JFrame frame;
	private JButton btnRandom;
	private JLabel imgLabel;
	private String urlString;
	private String urlBase;
	private ArrayList<String> urlHistory;
	private int queueSize;
	private int currentElement;
	static String removedImgUrl = "http://i.imgur.com/8tdUI8N.png";
	private boolean auto;
	private Timer timer;

	private Random random;
	private JPanel panel;
	private JPanel panel_1;
	private JTextField baseText;
	private JLabel lblNewLabel;
	private JButton btnNewButton;
	private JPanel panel_2;
	private JLabel lblCurrentUrl;
	private JTextField curText;
	private JButton btnNewButton_1;
	private JPanel panel_3;
	private JButton btnNewButton_2;
	private JPanel panel_4;
	private JButton btnBack;
	private JPanel panel_5;
	private JButton btnFront;
	private JPanel panel_6;
	private JButton btnAuto;
	private JPanel panel_7;
	private JSpinner spinner;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 250, 154));
		frame.setBounds(100, 100, 756, 644);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setTitle("Randshot!");

		ActionHandler actionHandler = new ActionHandler();
		urlHistory = new ArrayList<String>();
		queueSize = 0;
		currentElement = -1;
		auto = false;
		timer = new Timer();

		imgLabel = new JLabel("");
		imgLabel.setIcon(new ImageIcon(MainWindow.class.getResource("/img/randback.png")));
		imgLabel.setBorder(new LineBorder(new Color(100, 149, 237), 5));
		imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(imgLabel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setBackground(new Color(178, 34, 34));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(240, 230, 140));
		panel_1.setBorder(new LineBorder(new Color(0, 128, 128), 5));
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));

		baseText = new JTextField();
		baseText.setMargin(new Insets(4, 15, 4, 15));
		baseText.setMinimumSize(new Dimension(12, 20));
		panel_1.add(baseText, BorderLayout.CENTER);
		baseText.setColumns(10);

		lblNewLabel = new JLabel("    Base URL: ");
		panel_1.add(lblNewLabel, BorderLayout.WEST);

		btnNewButton = new JButton("Clear");
		btnNewButton.setBackground(new Color(255, 182, 193));
		btnNewButton.setMargin(new Insets(2, 26, 2, 26));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				baseText.setText("");
				imgLabel.setIcon(new ImageIcon(MainWindow.class.getResource("/img/randback.png")));
				frame.pack();
			}
		});
		panel_1.add(btnNewButton, BorderLayout.EAST);

		panel_2 = new JPanel();
		panel_2.setBackground(new Color(238, 232, 170));
		panel_1.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		lblCurrentUrl = new JLabel("  Current Url: ");
		panel_2.add(lblCurrentUrl, BorderLayout.WEST);

		curText = new JTextField();
		curText.setMargin(new Insets(2, 15, 2, 15));
		curText.setMinimumSize(new Dimension(6, 30));
		panel_2.add(curText, BorderLayout.CENTER);
		curText.setColumns(10);

		panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new BorderLayout(0, 0));

		btnNewButton_1 = new JButton("Copy");
		btnNewButton_1.setBackground(new Color(255, 182, 193));
		panel_3.add(btnNewButton_1);

		btnNewButton_2 = new JButton("^");
		btnNewButton_2.setBackground(new Color(255, 182, 193));
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				baseText.setText(curText.getText());

			}
		});
		btnNewButton_2.setMargin(new Insets(2, 5, 2, 5));
		panel_3.add(btnNewButton_2, BorderLayout.EAST);

		panel_4 = new JPanel();
		panel_4.setBackground(new Color(0, 255, 255));
		panel.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		panel_5 = new JPanel();
		panel_5.setBackground(new Color(173, 216, 230));
		panel_4.add(panel_5, BorderLayout.WEST);
		panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnBack = new JButton("");
		panel_5.add(btnBack);
		btnBack.setBackground(new Color(0, 191, 255));
		btnBack.setBorder(new LineBorder(new Color(60, 179, 113), 5));
		btnBack.setMargin(new Insets(0, 0, 0, 0));
		btnBack.setIcon(new ImageIcon(MainWindow.class.getResource("/img/back-button.png")));
		btnBack.setActionCommand("navi back");
		btnBack.addActionListener(actionHandler);

		btnFront = new JButton("");
		btnFront.setMargin(new Insets(0, 14, 0, 0));
		btnFront.setBorder(new LineBorder(new Color(60, 179, 113), 5));
		btnFront.setBackground(new Color(0, 191, 255));
		btnFront.setIcon(new ImageIcon(MainWindow.class.getResource("/img/front--button.png")));
		btnFront.setActionCommand("navi front");
		btnFront.addActionListener(actionHandler);

		panel_5.add(btnFront);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(new StringSelection(curText.getText()), null);
			}
		});

		btnBack.setEnabled(false);
		btnFront.setEnabled(false);

		panel_6 = new JPanel();
		panel_4.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));

		btnRandom = new JButton("*CLICK ME*");
		panel_6.add(btnRandom);
		btnRandom.setPreferredSize(new Dimension(250, 60));
		btnRandom.setBackground(new Color(100, 149, 237));
		btnRandom.setBorder(new LineBorder(new Color(46, 139, 87), 5));
		btnRandom.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		btnRandom.setMargin(new Insets(10, 20, 10, 20));
		btnRandom.setActionCommand("random");

		panel_7 = new JPanel();
		panel_7.setBackground(new Color(175, 238, 238));
		panel_6.add(panel_7, BorderLayout.EAST);

		btnAuto = new JButton("A");
		btnAuto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (auto == false) {
					auto = true;
					btnAuto.setBackground(Color.GREEN);
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {

						@Override
						public void run() {
							btnRandom.doClick();
						}
					}, 0, (int) spinner.getValue() * 1000);
				} else {
					auto = false;
					btnAuto.setBackground(Color.RED);
					timer.cancel();
				}

			}
		});
		panel_7.setLayout(new BorderLayout(0, 0));
		btnAuto.setBackground(Color.RED);
		btnAuto.setBorder(new LineBorder(new Color(107, 142, 35), 4));
		btnAuto.setPreferredSize(new Dimension(35, 35));
		btnAuto.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		panel_7.add(btnAuto, BorderLayout.CENTER);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(3, 1, 60, 1));
		spinner.setBorder(new LineBorder(new Color(46, 139, 87), 5));
		panel_7.add(spinner, BorderLayout.NORTH);
		btnRandom.addActionListener(actionHandler);
		// urlString = "http://prntscr.com/4n80h7";
		random = new Random(System.currentTimeMillis());

		generateUrl();
		System.out.println("SHIT:" + baseText.getText() + "@");

		frame.pack();
	}

	private void generateUrl() {

		StringBuilder builder = new StringBuilder();

		builder.append("http://prnt.sc/");

		for (int i = 0; i < 6; i++)
			builder.append(randomDigit());

		urlString = builder.toString();
		System.out.println("SKETO: urlString");

	}

	private void generateUrl(String base) {

		StringBuilder b = new StringBuilder();
		if (base.startsWith("http"))
			b.append(base.substring(base.length() - 6));
		else
			b.append(base);

		System.out.println("BB:  " + b.toString());

		int pos = random.nextInt(6);
		StringBuilder builder = new StringBuilder();
		builder.append("http://prnt.sc/");

		for (int i = 0; i < 6; i++) {

			if (i != pos)
				builder.append(b.toString().charAt(i));
			else
				builder.append(randomDigit());
		}

		urlString = builder.toString();
		// baseText.setText(urlString);

		System.out.println("OLOGGG: urlString");

	}

	private char randomDigit() {

		int digitType = random.nextInt(2);
		char digit = 0;

		if (digitType == 0)
			digit = (char) (random.nextInt(10) + '0'); // Number
		else
			digit = (char) (random.nextInt(26) + 'a'); // Lower Case Letter

		return digit;
	}

	private void addToQueue(String element) {
		if (currentElement >= urlHistory.size() - 1) {
			urlHistory.add(element);
			currentElement++;
		} else
			urlHistory.set(currentElement++, element);

		if (currentElement > 500)
			currentElement = 0;
	}

}
