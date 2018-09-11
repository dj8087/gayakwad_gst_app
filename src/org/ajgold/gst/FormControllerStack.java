/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ajgold.gst;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Dheeraj
 */
public class FormControllerStack {

    private static final Stack<JFrame> frameQ = new Stack<JFrame>();

    public static void pushMe(JFrame frm){
        if(!frameQ.empty())
            frameQ.peek().setVisible(false);
        frameQ.add(frm);
        System.out.println("Pushing : "+frm.getName());
    }

    public static void popMe(){
        System.out.println("Popping:"+frameQ.peek().getName());
        frameQ.pop().dispose();
         if(!frameQ.empty())
             frameQ.peek().setVisible(true);
         else
             System.exit(1);
    }
}
