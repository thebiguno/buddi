package org.tp23.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

/**
 *
 * <p>Title: Example Application</p>
 * <p>Description: Prints Hello World, trys to do it in a pop up box
* if a Throwable is thrown (e.g. no X) it prints to System.out</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: tp23</p>
 * @author Paul Hinds
 * @version 1.0
 */
public class Demonstration {
	private static ImageIcon icon ;
	static{
		try {
			InputStream in = Demonstration.class.getResourceAsStream("/resources/demo.png");
			byte[] data = new byte[11437];
			for (int i = 0; i < data.length; i++) {
				data[i]=(byte)in.read();
			}
			icon = new ImageIcon(data);
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public static void main(String[] args) {
		try{
			JOptionPane.showMessageDialog(null,
							"Demo Application \n\n" +
							"Deployed with: "+
							"http://antinstaller.sourceforge.net \n" +
							"Built by: "+
							"http://ant.apache.org \n" +
							"See also: "+
							"http://httpfileserver.sourceforge.net \n\n" +
							"This project is Apache licensed \n\n" +
							"Properties selected while installing \n\n" +
							initProps(),
							"About - Demo App",
							JOptionPane.INFORMATION_MESSAGE,
							icon);
		}catch(Throwable t){
			System.out.println("Hello World");
		}
		System.exit(0);
	}
	private static String initProps() {
		StringBuffer userProperties = new StringBuffer();
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(new File("./config/demo.properties")));
			Iterator iter = props.keySet().iterator();
			while(iter.hasNext()){
				String key = (String)iter.next();
				userProperties.append(key)
				.append("=")
				.append(props.getProperty(key))
				.append('\n');
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return userProperties.toString();
	}

}
