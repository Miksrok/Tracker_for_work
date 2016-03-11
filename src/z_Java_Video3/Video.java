package z_Java_Video3;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class Video extends Thread {

	// MainFrame mf = new MainFrame();
	String file;
	CvScalar rgba_min = cvScalar(100, 0, 0, 0);
	CvScalar rgba_max = cvScalar(255, 147, 60, 0);
	Java2DFrameConverter con = new Java2DFrameConverter();
	//OpenCVFrameGrabber grabber; 
	OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	IplImage frame;
	IplImage diff;
	BufferedImage bi;
	Frame f;
	static CvRect knee, hip, ankle, ankleFingers, ribs;
	
	CvPoint kneeJoint; 
	CvPoint ankleJoint; 
	CvPoint hipJoint; 
	CvPoint ankleFingers_Joint; 
	CvPoint ribsJoint; 
	
	List<CvPoint> anklePoints = new ArrayList<>();
	List<CvPoint> kneePoints = new ArrayList<>();
	List<CvPoint> hipPoints = new ArrayList<>();
	List<CvPoint> ankleFingerspoints = new ArrayList<>();
	List<CvPoint> ribsPoints = new ArrayList<>();
	static List<Integer>selectorsArray = new ArrayList<>();
	int countFrame;
	static boolean stoper = true;
	int count = 0;
	int max = 0;
	
	/*
	Video(File file){
		PrevFrame.grabber = new OpenCVFrameGrabber(file);
	}*/
	

	public void run() {

		try {
			PrevFrame.grabber.start();
			frame = converter.convert(PrevFrame.grabber.grab());
			IplImage image = null;
			//System.out.println(Thread.currentThread().isDaemon());
			diff = IplImage.create(frame.width(), frame.height(), 8, 3);

			cvSet(diff, cvScalar(255, 255, 255, 0));

			CanvasFrame canvasFrame = new CanvasFrame("Graph");
			canvasFrame
					.setDefaultCloseOperation(javax.swing.JFrame.HIDE_ON_CLOSE);
			canvasFrame.setCanvasSize(480, 272);


			CvMemStorage storage = CvMemStorage.create();
			
			int width = 50;
			int height = 26;
			
			MainFrame.slider.setMaximum(PrevFrame.grabber.getLengthInFrames());
			
			while (canvasFrame.isVisible() && MainFrame.main.isVisible() 
					&& (frame = converter.convert(PrevFrame.grabber.grab())) != null) {
				if (!stoper) {
					try {
						Video.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {					
						assert false;
					}
					stoper = true;
				}
				
				cvClearMemStorage(storage);
				
				countFrame = PrevFrame.grabber.getFrameNumber();
				MainFrame.slider.setValue(PrevFrame.grabber.getFrameNumber());
				cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
				image = IplImage.create(frame.width(), frame.height(),
						IPL_DEPTH_8U, 1);
				
				cvInRangeS(frame, rgba_min, rgba_max, image);

				f = converter.convert(image);

				bi = con.convert(f);
				
				
				for (int i = 0; i < selectorsArray.size(); i++){
					PrevFrame.selector = selectorsArray.get(i);
					switch(PrevFrame.selector){
						
					case PrevFrame.KNEE_JOINT: {
						kneeJoint = whiteColorFinder(knee, bi);
						cvCircle(frame, kneeJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
						
						if (countFrame % 25 == 0 || countFrame == 4){
							kneePoints.add(kneeJoint);
							drawCircle(diff, kneePoints);
						}
						
						knee = new CvRect(kneeJoint.x() - width / 2, kneeJoint.y() - height / 2,
								width, height);
						break;
					}
					case PrevFrame.HIP_JOINT: {
						hipJoint = whiteColorFinder(hip, bi);
						cvCircle(frame, hipJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);

						if (countFrame % 25 == 0 || countFrame == 4){
							hipPoints.add(hipJoint);
							drawCircle(diff, hipPoints);
						}	
						
						hip= new CvRect(hipJoint.x() - width / 2, hipJoint.y() - height / 2,
								width, height);
						break;
					}
					case PrevFrame.ANKLE_JOINT:{
						ankleJoint = whiteColorFinder(ankle, bi);
						cvCircle(frame, ankleJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
						if (countFrame % 25 == 0 || countFrame == 4){
							anklePoints.add(ankleJoint);
							drawCircle(diff, anklePoints);
						}						
						ankle = new CvRect(ankleJoint.x() - width / 2, ankleJoint.y() - height / 2,
								width, height);
						break;
						}
					case PrevFrame.ANKLE_FINGERS:{
						ankleFingers_Joint = whiteColorFinder(ankleFingers, bi);
						cvCircle(frame, ankleFingers_Joint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
						
						if (countFrame % 25 == 0 || countFrame == 4){
							ankleFingerspoints.add(ankleFingers_Joint);
							drawCircle(diff, ankleFingerspoints);
							
						}		
						ankleFingers= new CvRect( ankleFingers_Joint.x() - width / 2,  ankleFingers_Joint.y() - height / 2,
								width, height);
							break;
					}
					case PrevFrame.RIBS:{
						ribsJoint = whiteColorFinder(ribs, bi);
						cvCircle(frame, ribsJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
						
						if (countFrame % 25 == 0 || countFrame == 4){
							ribsPoints.add(ribsJoint);
							drawCircle(diff, ribsPoints);
						}		
						
						ribs= new CvRect(ribsJoint.x() - width / 2, ribsJoint.y() - height / 2,
								width, height);
						break;
					}
					
					}
				
				}
				
				if (ribsJoint != null && hipJoint != null) {
					cvLine(frame, ribsJoint, hipJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					drawLine(diff, ribsPoints, hipPoints);
				}
				if (hipJoint != null && kneeJoint != null) {
					cvLine(frame, kneeJoint, hipJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					drawLine(diff, kneePoints, hipPoints);
				}
				if (kneeJoint != null && ankleJoint != null) {
					cvLine(frame, kneeJoint, ankleJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					drawLine(diff, kneePoints, anklePoints);
				}
				if (ankleJoint != null && ankleFingers_Joint != null) {
					cvLine(frame, ankleFingers_Joint, ankleJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					drawLine(diff, anklePoints, ankleFingerspoints);
				}

				/*if (hipJoint != null && ankleJoint != null && kneeJoint != null && ankleFingers_Joint != null && ribsJoint != null ) {
					cvCircle(frame, kneeJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
					cvCircle(frame, ankleJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
					cvCircle(frame, hipJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
					cvCircle(frame, ankleFingers_Joint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);
					cvCircle(frame, ribsJoint, 7, cvScalar(0, 0, 255, 0), -1, 8, 0);

					if (countFrame % 25 == 0 || countFrame == 4) {
						anklePoints.add(ankleJoint);
						kneePoints.add(kneeJoint);
						hipPoints.add(hipJoint);
						ankleFingerspoints.add(ankleFingers_Joint);
						ribsPoints.add(ribsJoint);
						
						drawCircle(diff, anklePoints);
						drawCircle(diff, kneePoints);
						drawCircle(diff, hipPoints);
						drawCircle(diff, ankleFingerspoints);
						drawCircle(diff, ribsPoints);
						
						
						drawLine(diff, ribsPoints, hipPoints);
						drawLine(diff, kneePoints, hipPoints);
						drawLine(diff, kneePoints, anklePoints);
						drawLine(diff, anklePoints, ankleFingerspoints);
						
					}
					
					cvSetImageROI(frame, knee);
					cvAddS(frame, cvScalar(255, 100, 150, 0), frame);
					
					cvSetImageROI(frame, hip);
					cvAddS(frame, cvScalar(255, 100, 150, 0), frame);

					cvSetImageROI(frame, ankle);
					cvAddS(frame, cvScalar(255, 100, 150, 0), frame);

					cvResetImageROI(frame);

					cvLine(frame, kneeJoint, hipJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					cvLine(frame, kneeJoint, ankleJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					cvLine(frame, ankleFingers_Joint, ankleJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					cvLine(frame, ribsJoint, hipJoint, cvScalar(0, 255, 0, 0), 2, 8, 0);
					
					knee = new CvRect(kneeJoint.x() - width / 2, kneeJoint.y() - height / 2,
							width, height);
					ankle = new CvRect(ankleJoint.x() - width / 2, ankleJoint.y() - height / 2,
							width, height);
					hip= new CvRect(hipJoint.x() - width / 2, hipJoint.y() - height / 2,
							width, height);
					ankleFingers= new CvRect( ankleFingers_Joint.x() - width / 2,  ankleFingers_Joint.y() - height / 2,
							width, height);
					ribs= new CvRect(ribsJoint.x() - width / 2, ribsJoint.y() - height / 2,
							width, height);

					Integer angle = (int) angle(ankleJoint, hipJoint, kneeJoint);
					Integer hipAngle=(int)angle(kneeJoint, ribsJoint, hipJoint);
					Integer angle_2 = 180-angle;
					if (angle_2>max){
						max=angle_2;
					}
					MainFrame.angleValue.setText(angle.toString()+"/"+angle_2.toString()+"/"+" max angle = "+max);
				}else if ( kneeJoint != null ){
					cvCircle(frame, kneeJoint, 2, cvScalar(0, 0, 255, 0), -1, 8, 0);
					kneePoints.add(kneeJoint);
					drawCircle(diff, kneePoints);
					knee = new CvRect(kneeJoint.x() - width / 2, kneeJoint.y() - height / 2,
							width, height);
				}

				countFrame++;
*/
				canvasFrame.showImage(converter.convert(diff));
				MainFrame.main.showImage(converter.convert(frame));
	
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				
				PrevFrame.grabber.stop();
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}

	//calculating angle between two vectors
	static double angle(CvPoint a, CvPoint b, CvPoint c) {
		
		CvPoint vecAC = new CvPoint(a.x() - c.x(), a.y() - c.y());
		CvPoint vecBC = new CvPoint(b.x() - c.x(), b.y() - c.y());

		double q = (vecAC.x() * vecBC.x()) + (vecAC.y() * vecBC.y());
		double ac = Math.sqrt(Math.pow(vecAC.x(), 2) + Math.pow(vecAC.y(), 2));
		double cd = Math.sqrt(Math.pow(vecBC.x(), 2) + Math.pow(vecBC.y(), 2));

		double ang = q / Math.abs((ac * cd));
		double rad = Math.acos(ang);
		double rad2 = Math.toDegrees(rad);

		return rad2;
	}
	
	static CvPoint whiteColorFinder(CvRect rect, BufferedImage image) {

		int _x = 0;
		int _y = 0;
		int _count = 0;

		for (int x = rect.x(); x < rect.x() + rect.width(); x++) {
			for (int y = rect.y(); y < rect.y() + rect.height(); y++) {
				WritableRaster raster = image.getRaster();
				ColorModel model = image.getColorModel();
				Object data = raster.getDataElements(x, y, null);
				int argb = model.getRGB(data);
				Color color = new Color(argb, true);

				if (color.getRGB() == Color.WHITE.getRGB()) {
					_x += x;
					_y += y;

					_count++;
				}
			}
		}
		
		if (_count != 0) {
			return new CvPoint(_x / _count, _y / _count);
		} else {
			return null;
		}
	}
	static void drawCircle(IplImage image, List <CvPoint> points){
		
		for (int i=0; i<points.size(); i++){
			cvCircle(image, points.get(i), 7,
				cvScalar(0, 0, 255, 0), -1, 8, 0);
		}
	}
	static void drawLine(IplImage image, List <CvPoint> firstPoint, List <CvPoint> secondPoint){
		
		for (int i=0; i<firstPoint.size() && i<secondPoint.size(); i++){
			cvLine(image,   firstPoint.get(i), secondPoint.get(i),
					cvScalar(0, 255, 0, 0), 2, 8, 0);
		}
		
	}
	
	
}
