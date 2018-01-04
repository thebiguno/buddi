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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * <p>This subclass of PrintStream is designed to be compatible with System.out and System.err but it provides
* its own functionality and does not borrow from PrintStream. All methods are overridden.  The calls to each method
* behave as expected with the exception of flush() which appears to insert a new line for each call. flush() sends
* the current line to the JTextArea with a call to JTextArea.append(String) which adds a
* line in the buffer.</p>
 * @author Paul Hinds
 * @version 1.0
 */
public class JTextAreaPrintStream extends PrintStream{

    private JTextArea textArea;
    private StringBuffer line;

    public JTextAreaPrintStream(JTextArea textArea) {
        super(new ByteArrayOutputStream(0));// wasted all methods are overridden
        this.textArea = textArea;
        line = new StringBuffer();
    }

    /**
     * The stream can not be closed
     */
    public void close(){
    }

    public synchronized void flush(){
        if(line.length()>0){
			textArea.append(line.toString());
            line = new StringBuffer();
        }
    }

    public synchronized void write(byte[] b){
        line.append(new String(b));
    }

    public synchronized void write(byte[] b, int off, int len){
        line.append(new String(b,off,len));
    }


    public synchronized void write(int c){
        line.append(c);
    }

    public synchronized boolean checkError() {
        return false;
    }

    public synchronized void print(Object obj) {
        line.append(obj);
    }


    public synchronized void print(String s) {
        line.append(s);
    }


    public synchronized void print(boolean b) {
        line.append(b);
    }


    public synchronized void print(char c) {
        line.append(c);
    }


    public synchronized void print(char[] s) {
        line.append(s);
    }


    public synchronized void print(double d) {
        line.append(d);
    }


    public synchronized void print(float f) {
        line.append(f);
    }


    public synchronized void print(int i) {
        line.append(i);
    }


    public synchronized void print(long l) {
        line.append(l);
    }


    public synchronized void println() {
    	line.append('\n');
        flush();
    }


    public synchronized void println(Object x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(String x) {
        if(line.length()>0){
            textArea.append(line.append(x).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(x);
            textArea.append("\n");
        }
    }

    public synchronized void println(boolean x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(char x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(char[] x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(double x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(float x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(int x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    public synchronized void println(long x) {
        if(line.length()>0){
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        }
        else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }
    }


    protected void setError() {
    }


}
