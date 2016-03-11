package z_Java_Video3;

import java.awt.EventQueue;

public class Launcher {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame r = new MainFrame();
					r.lf();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
