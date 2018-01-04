/* 
 * Copyright 2005 Paul Hinds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tp23.antinstaller.selfextract;

import java.awt.HeadlessException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.tp23.antinstaller.InstallException;
import org.tp23.antinstaller.renderer.swing.plaf.LookAndFeelFactory;
import org.tp23.antinstaller.runtime.ExecInstall;
import org.tp23.antinstaller.runtime.exe.FilterChain;
import org.tp23.antinstaller.runtime.exe.FilterFactory;

/**
 *
 * <p>Finds a file reference to the Jar that loads this class and then extracts that Jar
 * to a temporary directory </p>
 * <p> </p>
 * @author Paul Hinds
 * @version $Id: SelfExtractor.java,v 1.10 2007/01/28 08:44:40 teknopaul Exp $
 */
public class SelfExtractor {

	public static final String CONFIG_RESOURCE = "/org/tp23/antinstaller/runtime/exe/selfextractor.fconfig";
	
	private File extractDir;
	private File archiveFile;
	private boolean overwrite = true;

	private static int DEFAULT_BUFFER_SIZE = 1024;
	private int BUFFER_SIZE = DEFAULT_BUFFER_SIZE;
	private static boolean graphicsEnv = false;
	private static String lookAndFeel = null;

	/**
     * returns the Jar that the reference object was loaded from.  If it was not
     * loaded from a jar this methods behaviour is undefined
     * @TODO define what happens
     * @param reference
     * @return A java.io.File reference to the Jar
	 */
	public static File getEnclosingJar(Object reference) {
		String thisClass = "/" + reference.getClass().getName().replace('.','/') + ".class";
		URL jarUrl = reference.getClass().getResource(thisClass);
		String stringForm = jarUrl.toString();
		//String fileForm = jarUrl.getFile();

		File file = null;
		int endIdx = stringForm.indexOf("!/");
		if(endIdx != -1){
			String unescaped = null;
			String fileNamePart = stringForm.substring("jar:file:".length(), endIdx);
			file = new File(fileNamePart);
			if ( ! file.exists()) {
				// try to unescape encase the URL Handler has escaped the " " to %20
				unescaped = unescape(fileNamePart);
				file = new File(unescaped);
			}
			return file;
		}
		throw new RuntimeException("Failed expanding Jar.");
	}

    /**
	 * Constructor for the SelfExtractor object.  Directly after constructing
	 * an instance the init() method should be called unless subclassing
	 */
	public SelfExtractor() {
	}

    /**
	 * This has been moved from the default constructor to facilitate subclassing
	 * @return true if the lookAndFeel worked
	 */
	public void init(){
		System.out.println("Loading self extractor...");
		archiveFile = getEnclosingJar(this);
		makeTempDir();
		try {
			JarFile thisJar = new JarFile(archiveFile);
			lookAndFeel = thisJar.getManifest().getMainAttributes().getValue("Look-And-Feel");
			lookAndFeel = LookAndFeelFactory.getLafFromToken(lookAndFeel);
			if(lookAndFeel != null) {
                UIManager.setLookAndFeel(lookAndFeel);
            }
		}
		catch (Throwable ex) {
			// not concerned about Look and Feel
		}
	}
	
    /**
     * Creates a new empty temporary directory for the file extraction
     * @return
     */
	protected File makeTempDir(){
		String tempDir = System.getProperty("java.io.tmpdir");
		extractDir = new File(tempDir, "antinstall");
		int idx = 0;
		while (extractDir.exists()) {
            extractDir = new File(tempDir, "antinstall" + (idx++));
        }
		extractDir.mkdirs();
		extractDir.deleteOnExit();
		return extractDir;
	}

    /**
	 *  Constructor for the SelfExtractor object that sets the buffersize in use.
	 *  The write buffer is the same size as the write buffer size because the read buffer reads
	 *  decompressed bytes
	 *  @param newBufferSize the size of the read buffer
	 */
	public SelfExtractor(int newBufferSize) {
		BUFFER_SIZE = newBufferSize;
		archiveFile = getEnclosingJar(this);
	}

    /**
	 *  Sets the Directory into which the file will be extracted
	 *
	 *@param  newExtractDir  The new extract directory
	 */
	public void setExtractDir(File newExtractDir) {
		extractDir = newExtractDir;
	}

    /**
	 *  changes the archive to be extracted
	 *@param  newArchiveFile  The new archiveFile value
	 */
	public void setArchiveFile(File newArchiveFile) {
		archiveFile = newArchiveFile;
	}

    /**
	 *  Gets the Directory into which the files will be extracted that
	 *  is currently set in the ZipExtractor object
	 *@return    The extract directory value
	 */
	public File getExtractDir() {
		return extractDir;
	}

    /**
	 *  Gets the set in the ZipExtractor
	 *@return    The archiveFile value
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

    /**
	 *  Gets the Directory into which the files will be extracted that
	 *  is currently set in the ZipExtractor object
	 *@return    The extract directory value
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

    /**
	 *  Gets the set in the ZipExtractor
	 *@return    The archiveFile value
	 */
	public File getArchiveFile() {
		return archiveFile;
	}

    /**
	 *  Opens up the zip and gets a list of the files in it.  If the zip file
	 *  or the temp file have not been set NullPointerExceptions will get thrown
	 *@param  vebose  if true Prints out a list of the zips
	 *      contents on to the command line
	 *@return  an ArrayList of String objects that will
	 *         be as per the path in the zip
	 *@exception  FileNotFoundException  Description of Exception
	 *@exception  IOException            Description of Exception
	 */
	public ArrayList getList(boolean vebose) throws FileNotFoundException, IOException {
		JarInputStream zis = new JarInputStream(new FileInputStream(archiveFile));
		JarEntry entry = null;
		ArrayList result = new ArrayList();
		while ( (entry = zis.getNextJarEntry()) != null) {
			if (vebose) {
				System.out.println(entry.getName());
			}
			result.add(entry.getName());
		}
		return result;
	}

    /**
     * @return the number of files in the jar
     * @throws FileNotFoundException
     * @throws IOException
     */
	public int getFileCount() throws FileNotFoundException, IOException {
		JarInputStream zis = new JarInputStream(new FileInputStream(archiveFile));
		int count = 0;
		while (  zis.getNextJarEntry() != null) {
			count++;
		}
		return count;
	}

    /**
	 *  Opens up the zip and extracts the files to the temp dir.
	 *
	 *@param  vebose  if true Prints out a list of the zips contents on to System.out
	 *@return an ArrayList of java.io.File objects that
	 *      will be as per the path in the zip with the root being the temp dir
	 *@exception  FileNotFoundException
	 *@exception  IOException
	 */
	public ArrayList extract(boolean vebose, boolean isX) throws FileNotFoundException, IOException {
		int fileCount = getFileCount();
		ProgressIndicator indicator = null;
        if(isX){
            try {
                    indicator = new ProgressIndicator(fileCount);
                    indicator.show();
            }
            catch ( Exception exc ) {
                /*
                 * Chances are, there are problems with the graphics environment
                 * so trying falling back to text mode
                 */
                graphicsEnv = false;
                isX = false;
            }

        }
		JarInputStream zis = new JarInputStream(new FileInputStream(archiveFile));
		JarEntry entry = null;
		ArrayList result = new ArrayList();
		while ( (entry = zis.getNextJarEntry()) != null) {
			if (vebose) {
				System.out.println("Extracting:" + entry.getName());
			}
			result.add(extract(zis, entry));
			if (isX) {
				indicator.tick();
			}
		}
		if (isX) {
			indicator.hide();
		}
		zis.close();
		return result;
	}



    /**
	 *  Extract a single file from the stream. N.B. the stream must be in the correct
	 *  position for this to work
	 *@param  zis                        ZipInputStream open and ready
	 *@param  entry                      A valid entry read from the stream
	 *@return                            The inflated file generated in the temp dir
	 *@exception  FileNotFoundException
	 *@exception  IOException
	 */
	private File extract(JarInputStream zis, JarEntry entry) throws FileNotFoundException, IOException {
		createPath(entry.getName());
		File fileToUse = new File(extractDir, entry.getName());
		if (fileToUse.exists()) {
			if (!overwrite) {
                return fileToUse;
            }
		}
		else {
			fileToUse.createNewFile();
		}
		if (fileToUse.isDirectory()) {
            return fileToUse;
        }

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileToUse), BUFFER_SIZE);
		byte[] bytes = new byte[BUFFER_SIZE];
		int len = 0;
		while ( (len = zis.read(bytes)) >= 0) {
			bos.write(bytes, 0, len);
		}
		bos.close();
		zis.closeEntry();
		return fileToUse;
	}

    /**
	 *  This adds all the necessary directories in the root of the zip path to the
	 *  temp dir.
	 *@param  entryName        The string name in the Zip file (virtual path)
	 *@exception  IOException  if the directories can not be made
	 */
	private void createPath(String entryName) throws IOException {
		int slashIdx = entryName.lastIndexOf('/');
		if (slashIdx >= 0) {
			// there is path info
			String firstPath = entryName.substring(0, slashIdx);
			File dir = new File(extractDir, firstPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}

    /**
	 * Run method to use from the command line. This is fired via an entry in the 
	 * MANIFEST.MF in the Jar
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {
		testX();
		// FIXME move after parseArgs() and set graphicsEnv if text selected
		// will need to test SelfExtractor and comment parseArgs() to ensure
		// no side effects in the future.
		SelfExtractor extractor = null;
		try {
			boolean verbose = false;
			extractor = new SelfExtractor();
			extractor.init();
			extractor.extract(verbose, graphicsEnv);
		}
		catch (Exception e) {
			e.printStackTrace();
			String tempDir = "unknown";
			if(extractor != null){
				tempDir = extractor.getExtractDir().getAbsolutePath();
			}
			String warning = "Could not extract Jar file to directory:" + tempDir;
			printXorTextWarning(warning);
		}
		
		try {
			FilterChain chain = FilterFactory.factory(CONFIG_RESOURCE);		
			ExecInstall installExec = new ExecInstall(chain);
			installExec.parseArgs(args, false);
			installExec.setInstallRoot(extractor.getExtractDir());
			// removes files on exit
			installExec.setTempRoot(extractor.getExtractDir());

            installExec.exec();
		}
		catch (InstallException e1) {
			System.out.println("Cant load filter chain:/org/tp23/antinstaller/runtime/exe/selfextractor.fconfig");
			e1.printStackTrace();
		}
	}

	/**
	 * This tests for the existence of a  graphics environment and sets an
	 * internal flag so the test does not have to be repeated, it may be expensive.
	 * Prior to running this method the isGraphicsEnv() method will be invalid.
	 */
	protected static void testX(){
		try {
			java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                boolean headless = java.awt.GraphicsEnvironment.isHeadless();
                if(headless) {
                    graphicsEnv = false;
                    return;
                }
            } catch (Throwable e) {
                // JDK 1.3 does not have the isHeadless() method but may still work in other situations
            }
			graphicsEnv = true;
		}
		catch (Throwable e) {
            // thus graphicsEnv stays false;
		}
	}
    
	/**
	 * @see #testX()
	 * @return true if an X or windows environment is available
	 */
	protected boolean isGraphicsEnv(){
		return graphicsEnv;
	}

	protected static void printXorTextWarning(String warning){
		if(graphicsEnv){
            try {
                JOptionPane.showMessageDialog(null, warning);
            }
            catch( HeadlessException headlessExc ) {
                graphicsEnv = false;
                System.out.println(warning);
            }
        }
		else {
			System.out.println(warning);
		}
	}

	public static int deleteRecursive(File directory) {
		int count = 0;
		File[] files = directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return!file.isDirectory();
			}
		});
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
			count++;
		}
		File[] dirs = directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		for (int i = 0; i < dirs.length; i++) {
			count += deleteRecursive(dirs[i]);
		}
		directory.delete();
		return count;
	}

    /**
	 * UN-URL encode string
     * TODO should this not support UNICODE escapes
	 */
	private static String unescape(final String s) {
		StringBuffer sb = new StringBuffer(s.length());

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '%': {
					try {
						sb.append( (char) Integer.parseInt(s.substring(i + 1, i + 3), 16));
						i += 2;
						break;
					}
					catch (NumberFormatException nfe) {
						throw new IllegalArgumentException();
					}
					catch (StringIndexOutOfBoundsException siob) {
						String end = s.substring(i);
						sb.append(end);
						if (end.length() == 2) i++;
					}
					break;
				}
				default: {
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

}
