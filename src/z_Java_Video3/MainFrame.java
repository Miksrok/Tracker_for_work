package z_Java_Video3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber.Exception;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{

	//JFrame main; 
	static CanvasFrame main;
	
	JPanel mainPanel, textPanel, buttonsPanel;
	JButton startButton;
	JButton pauseButton;
	JButton resumeButton, file;
	JButton hip, knee, ankle, ankleFingers, ribs;
	File fileToOpen;
	JPanel panelForText, panelForAngle;
	static JSlider slider;
	Video video;
	JLabel text;
	JLabel angle; 
	static JLabel angleValue;
	JTextArea ta;
	/*static CvScalar min_BGR;
	static CvScalar max_BGR;
	static CvScalar mainColor;*/
	
	
	MainFrame(){}
	
	private void init(){
		main = new CanvasFrame("Main Frame");
		main.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		textPanel = new JPanel();
		panelForText = new JPanel();
		panelForAngle = new JPanel();
		buttonsPanel = new JPanel();
		startButton = new JButton("Start");
		pauseButton = new JButton("Pause");
		resumeButton = new JButton("Resume");
		hip = new JButton("hip");
		knee = new JButton("knee");
		ankle = new JButton("ankle");
		ankleFingers = new JButton("fingers");
		ribs = new JButton("ribs");
		file = new JButton("File");
		text = new JLabel("select file");
		slider = new JSlider();
		angle = new JLabel("angle:");
		angleValue = new JLabel("0");
		//video = new Video();
	}
	
	public void lf(){
		
		init();
		
		main.add(mainPanel, BorderLayout.SOUTH);
		main.add(textPanel, BorderLayout.NORTH);
		main.add(buttonsPanel, BorderLayout.EAST);
		buttonsPanel.setLayout(new GridLayout(3,2));
		textPanel.setLayout(new GridLayout(0,2));
		
		main.setSize(800, 500);
		//main.setCanvasSize(500, 300);
		mainPanel.add(startButton);
		mainPanel.add(pauseButton);
		mainPanel.add(resumeButton);
		mainPanel.add(file);
		mainPanel.add(slider);
		
		textPanel.add(panelForText);
		textPanel.add(panelForAngle);
		panelForText.add(text);
		panelForAngle.add(angle);
		panelForAngle.add(angleValue);
		buttonsPanel.add(hip);
		hip.setEnabled(false);
		buttonsPanel.add(knee);
		knee.setEnabled(false);
		buttonsPanel.add(ankle);
		ankle.setEnabled(false);
		buttonsPanel.add(ankleFingers);
		ankleFingers.setEnabled(false);
		buttonsPanel.add(ribs);
		ribs.setEnabled(false);

		listeners();
		
	}
	
	private void listeners(){
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				video = new Video(/*fileToOpen*/);
				video.setDaemon(true);
				video.start();
				
			}
		});
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Video.stoper = false;
			}
		});
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				video.interrupt();
			}
		});
		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileopen = new JFileChooser();
				int ret = fileopen.showDialog(null, "Открыть файл");				
				if (ret == JFileChooser.APPROVE_OPTION) {
					fileToOpen = fileopen.getSelectedFile();
					try {						
						PrevFrame pf = new PrevFrame(fileToOpen);
						pf.prevFrame();
						text.setText(fileToOpen.getName() + " is open");
						hip.setEnabled(true);
						knee.setEnabled(true);
						ankle.setEnabled(true);
						ankleFingers.setEnabled(true);
						ribs.setEnabled(true);
					} catch (Exception e) {
						text.setText("file not found");
						e.printStackTrace();
					}
				}
				if (Video.knee != null) Video.knee=null;
				if (Video.hip != null) Video.hip=null;
				if (Video.ankle != null) Video.ankle=null;
				if (Video.ankleFingers != null) Video.ankleFingers=null;
				if (Video.ribs != null) Video.ribs=null;
			}
		});
		hip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrevFrame.selector=PrevFrame.HIP_JOINT;	
				text.setText("select hip joint");
				hip.setEnabled(false);
			}
		});
		knee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrevFrame.selector=PrevFrame.KNEE_JOINT;	
				text.setText("select knee joint");
				knee.setEnabled(false);
			}
		});
		ankle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrevFrame.selector=PrevFrame.ANKLE_JOINT;	
				text.setText("select ankle joint");
				ankle.setEnabled(false);
			}
		});
		ankleFingers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrevFrame.selector=PrevFrame.ANKLE_FINGERS;	
				text.setText("select fingers joint");
				ankleFingers.setEnabled(false);
			}
		});
		ribs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrevFrame.selector=PrevFrame.RIBS;	
				text.setText("select ribs joint");
				ribs.setEnabled(false);
			}
		});
		
		/*slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				
				JSlider source = (JSlider)e.getSource();
				System.out.println(source.getName());
				
			}
		});*/
	}
	
}

