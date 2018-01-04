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
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JScrollPane;

/**
 *
 * <p>Title: A JTextArea that can be set to show System.out or System.err</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Paul Hinds
 * @version 1.0
 */

public class SystemOutJTextArea extends JScrollPane{

    private JTextAreaPrintStream stream;
    private FollowingJTextArea textArea = new FollowingJTextArea();

    public SystemOutJTextArea() {
        jInit();
    }

    private void jInit(){
        this.getViewport().setView(textArea);
        textArea.setEditable(false);
        JButton jumpButton = new JButton("");
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        setCorner(this.LOWER_RIGHT_CORNER,jumpButton);
        jumpButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
        jumpButton.setToolTipText("Click to jump to the end");


    }

    public synchronized PrintStream getOut(){
        if(stream==null){
            stream = new JTextAreaPrintStream(textArea);
        }
        return stream;
    }
    public void setAsSystemErr(){
        System.setErr(getOut());
    }
    public void setAsSystemOut(){
        System.setOut(getOut());
    }


}
