package z_Java_Video3;

import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_core.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class PrevFrame {

	static OpenCVFrameGrabber grabber;
	
	//Java2DFrameConverter con = new Java2DFrameConverter();
	
	static final int KNEE_JOINT = 1;
	static final int HIP_JOINT = 2;
	static final int ANKLE_JOINT = 3;
	static final int ANKLE_FINGERS = 4;
	static final int RIBS = 5;
	static int selector = 0;

	PrevFrame(File file) {
		grabber = new OpenCVFrameGrabber(file);
	}

	public void prevFrame() throws Exception {

		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

		grabber.start();

		IplImage frame = converter.convert(grabber.grab());
		IplImage tmp = cvCloneImage(frame);

		MainFrame.main.showImage(converter.convert(frame));

		MainFrame.main.getCanvas().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				

				switch (selector) {
					case KNEE_JOINT: {
						Video.knee = new CvRect(e.getX() - 25, e.getY() - 13, 50,
								26);
						cvCircle(tmp, new CvPoint(e.getX(),e.getY()),4,cvScalar(0, 0, 255, 0), -1, 8, 0);
						MainFrame.main.showImage(converter.convert(tmp));
						Video.selectorsArray.add(KNEE_JOINT);
						break;
					}
					case HIP_JOINT: {
						Video.hip = new CvRect(e.getX() - 25, e.getY() - 13, 50,
								26);
						cvCircle(tmp, new CvPoint(e.getX(),e.getY()),4,cvScalar(0, 0, 255, 0), -1, 8, 0);
						MainFrame.main.showImage(converter.convert(tmp));
						Video.selectorsArray.add(HIP_JOINT);
						break;
					}
					case ANKLE_JOINT:{
						Video.ankle = new CvRect(e.getX() - 25, e.getY() - 13, 50,
							26);
						cvCircle(tmp, new CvPoint(e.getX(),e.getY()),4,cvScalar(0, 0, 255, 0), -1, 8, 0);
						MainFrame.main.showImage(converter.convert(tmp));
						Video.selectorsArray.add(ANKLE_JOINT);
						break;
						}
					case ANKLE_FINGERS:{
						Video.ankleFingers = new CvRect(e.getX() - 25, e.getY() - 13, 50,
								26);
						cvCircle(tmp, new CvPoint(e.getX(),e.getY()),4,cvScalar(0, 0, 255, 0), -1, 8, 0);
						MainFrame.main.showImage(converter.convert(tmp));
						Video.selectorsArray.add(ANKLE_FINGERS);
							break;
					}
					case RIBS:{
						Video.ribs = new CvRect(e.getX() - 25, e.getY() - 13, 50,26);
						cvCircle(tmp, new CvPoint(e.getX(),e.getY()),4,cvScalar(0, 0, 255, 0), -1, 8, 0);
						MainFrame.main.showImage(converter.convert(tmp));
						Video.selectorsArray.add(RIBS);
						break;
					}
							
				}

			}
		});
			
		grabber.stop();
	}
}
