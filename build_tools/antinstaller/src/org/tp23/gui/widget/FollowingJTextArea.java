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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

public class FollowingJTextArea extends JTextArea{

    private boolean follow = true;

    public FollowingJTextArea() {
        jInit();
    }


    private void jInit(){
        final JPopupMenu popUp = getPopupMenu();
        this.add(popUp);
        this.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON3) {
                    popUp.show(FollowingJTextArea.this,e.getX(),e.getY());
                }
            }
        });
    }

    public boolean isFollow() {
        return follow;
    }
    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    private void scrollToEnd(){
        setCaretPosition(getDocument().getLength());
    }
    private void toggleFollow(){
        setFollow(!isFollow());
    }

    /**
     * Appends the given text to the end of the document.
     *
     * @param str the text to insert
     * @todo Implement this javax.swing.JTextArea method
     */
    public void append(String str) {
        super.append(str);
        if(follow)scrollToEnd();
    }
    private JPopupMenu getPopupMenu() {
        JPopupMenu contextMenu = new JPopupMenu("Options");
        JMenuItem saveMenu = new JMenuItem("Save Text");
        saveMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SelectFileAction action = new SelectFileAction("Save Output", null, null);
                try {
                    action.actionPerformed(new ActionEvent(this, 0, "Save Output"));
                    if (action.selectedFile != null) {
                        FileWriter fos = new FileWriter(action.selectedFile);
                        fos.write(getText());
                        fos.close();
                    }

                }
                catch (FileNotFoundException ex) {
                    System.err.println("FileNotFoundException");
                }
                catch (IOException ex) {
                    System.err.println("IOException");
                }
            }
        });
        contextMenu.add(saveMenu);

        JMenuItem toggleFollowMenu = new JMenuItem("Toggle Follow");
        toggleFollowMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleFollow();
            }
        });
        contextMenu.add(toggleFollowMenu);

        JMenuItem jumpToEndMenu = new JMenuItem("Jump To End");
        jumpToEndMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCaretPosition(getDocument().getLength());
            }
        });
        contextMenu.add(toggleFollowMenu);

        JMenuItem clearTextMenu = new JMenuItem("Clear Text");
        clearTextMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText("");
            }
        });
        contextMenu.add(clearTextMenu);
        return contextMenu;
    }
}
