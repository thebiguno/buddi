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
package org.tp23.gui.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
/**
 * A file chooser that makes a beter default if the default directory is not available.
* Basically the default looks for the suggested directory and if it does not find it moves back
* up the directories.  If still nothing is found (for example it may get to My Computer
* which you can not add file to) the home directory is selected, as in the default file chooser.
*
* To use this class call new DefaultingDirectoryChooser(boolean) and immediately call
* setDefaultDirectory(File) to determine the default. If you don't it behaves like the default JFileChooser
* Also if you call setCurrentDirectory(File) it will behave like JFileChooser
*
* This chooser has two modes CREATE_MODE and EXISTING_MODE.   <br/><br/>
*
* In existing mode it is assumed the chooser is being used to identify an existing directory
* the default directory should exist if not the directory shown will be the next existing parent.<br/><br/>
*
* In create mode it is
* assumed that the last directory in the default directory is to be created and may not
* exist yet. Therefor the default directory displayed is the parent (with suitable defaulting)
* and the text box will contain the name of the last directory.
* e.g. if default is C:\Program Files\MyApp  C:\Program Files will be displayed and
* MyApp will be shown in the text box (even if it does not exist yet)  If C:\Program Files does not exist
* C:\ will be shown as with existing mode.
* Choosing select can lead to the creation of the file.  It will not be automatically
* created that is the responsibility of the client code.<br/>
*
* This class uses FileSystemView and will only display folders that return true to fsv.isFileSystem()
*
* This class also uses a bit of a hack by in CREATE_MODE it sets the selectables to files and directories
* although only shows the directories this way a non-existing Directory can be chosen.  This is the only way I can
* get it to work on my Java version but I would not be supprised if it did not work on
* other versions of the Java API.  It also means that setFileSelectionMode() should
* never be called by clients as it will break this hack.
 * @author Paul Hinds
 * @version 1.0
 */
public class DefaultingDirectoryChooser extends JFileChooser{

    public static final boolean CREATE_MODE = true;
    public static final boolean EXISTING_MODE = false;

    /**
     * Determines that the default includes a directory name that is requried
     * e.g. C:\Program Files\MyApp where even if Program Files does not exist
     * MyApp should be part of the default even if it does not exist.
     */
    private boolean createMode = false;
    private File selectedFile = null;
    private String desiredName = null;
    
    public DefaultingDirectoryChooser(boolean createMode) {
        this.createMode=createMode;
        if(createMode){
            setFileSelectionMode(FILES_AND_DIRECTORIES);
            removeChoosableFileFilter(getAcceptAllFileFilter());
            addChoosableFileFilter(new FileFilter(){
                public boolean accept(File f) {
                    if(f.exists() && f.isDirectory())return true;
                    if(!f.exists() && getFileSystemView().getParentDirectory(f).isDirectory())return true;
                    return false;
                }
                public String getDescription() {
                    return "Directories";
                }
            });
            
            addPropertyChangeListener(JFileChooser.DIRECTORY_CHANGED_PROPERTY, new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent evt) {
					File directory = (File)evt.getNewValue();
					DefaultingDirectoryChooser.this.setCurrentDirectory(new File(directory, "newfolder"));
					setSelectedFile(new File(directory, desiredName));
				}            	
            });
        }
        else {
            setFileSelectionMode(DIRECTORIES_ONLY);
        }
    }


    /**
     * Sets the default directory.
     * @param dir the current directory to point to
     * @todo Implement this javax.swing.JFileChooser method
     */
    public void setDefaultDirectory(File dir) {
        if(dir==null)return;// called by the default consructor
        if(createMode){
        	if(desiredName == null)desiredName = dir.getName();
            File selected = determineCreateDir(dir);
            super.setCurrentDirectory(selected.getParentFile());
            setSelectedFile(selected);
        }else{
            super.setCurrentDirectory(determineExistingDir(dir));
        }
    }



    private File determineExistingDir(File defaultDir){
        if(defaultDir.exists())return defaultDir;
        FileSystemView fsv = this.getFileSystemView();
        File validParent = getFSVParent(fsv,defaultDir);
        if(validParent!=null) {
            return validParent;
        }
        else {
            return fsv.getHomeDirectory();
        }
    }
    /**
     * Select a base path for the default even if it does not exist.  The order of preference is as follows
     * if the directory exists use it
     * if the parent does not exist try finding the next parent
     * if the next parent is a root directory e.g. / or C:\ use the users home directory
     * @param defaultDir File
     * @return File
     */
    private File determineCreateDir(File defaultDir){
        if(defaultDir.exists())return defaultDir;
        FileSystemView fsv = this.getFileSystemView();
        String dirName = defaultDir.getName();
        File validParent = getFSVParent(fsv,defaultDir);
        if(validParent!=null){
            return new File(validParent, dirName);
        }
        else {
            return new File(fsv.getHomeDirectory(), dirName);
        }
    }
    /**
     * Step back up trying to find a parent if none if found return null.
     * null can be found if the path starts e:\ and e: does not exist
     * @param fsv FileSystemView
     * @param dir File
     * @return File
     */
    private File getFSVParent(FileSystemView fsv,File dir){
        File parent = dir.getParentFile();
        if(fsv.isRoot(parent) && !parent.exists()){
            return null;
        }
        if(!parent.exists() || !fsv.isFileSystem(parent)){
            parent = getFSVParent(fsv,parent);
        }
        return parent;
    }

    public static void main(String[] args) {
        DefaultingDirectoryChooser chooser = new DefaultingDirectoryChooser(CREATE_MODE);
        chooser.setDefaultDirectory(new File("/usr/local/dibble/MyApp"));

        chooser.showDialog(null,"Select");
        System.out.println("Selected:"+chooser.getSelectedFile().getAbsolutePath());
        System.exit(0);
    }
}
