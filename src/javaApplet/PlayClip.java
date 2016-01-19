package javaApplet;

import java.applet.AudioClip;
import java.applet.Applet;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.net.URL;
import java.net.MalformedURLException;

public class PlayClip extends JPanel {

    AudioClip clip;

    PlayClip(String source) {
        super(new GridLayout(1,0,10,10));
        setBorder( new EmptyBorder(20,20,20,20) );
        JButton play = new JButton("Play");
        play.addActionListener( new ActionListener(){
                public void actionPerformed(ActionEvent ae) {
                    clip.loop();
                }
            } );
        add( play );

        JButton stop = new JButton("Stop");
        stop.addActionListener( new ActionListener(){
                public void actionPerformed(ActionEvent ae) {
                    clip.stop();
                }
            } );
        add( stop );

        try {
            URL url = new URL(source);
            clip = Applet.newAudioClip(url);
        } catch(MalformedURLException murle) {
            murle.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread() {
            public void run() {
                JFrame frame = new JFrame();
                String source = JOptionPane.showInputDialog(
                    null,
                    "URL of clip?",
                    "file:///Users/Nickjjj114/Documents/workspace/javaApplet/src/javaApplet/POL-illusion-castle-short.wav");
//                		"file://./cartoon002.wav");
                if (source.trim().length()>0) {
                    PlayClip pc = new PlayClip(source);
                    frame.getContentPane().add( pc );
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(
                        JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
            }
        };
        SwingUtilities.invokeLater(t);
    }
}

//import javax.media.*;
//import java.net.URL;
// 
//public class PlayClip extends Thread {
// 
// private String filename;
// Player player;
// 
// public PlayClip(String mp3Filename) {
//    this.filename = mp3Filename;
// }
// 
// public void run() {
//    try {
////       URL url = this.getClass().getClassLoader().getResource("8-bit-music-loop.mp3");
//       URL url = this.getClass().getClassLoader().getResource("/Users/Nickjjj114/Documents/workspace/javaApplet/src/javaApplet/8-bit-music-loop.mp3");
//   
//       MediaLocator locator = new MediaLocator(url);
//       player = Manager.createPlayer(locator);
//       player.addControllerListener(new ControllerListener() {
//          public void controllerUpdate(ControllerEvent event) {
//             if (event instanceof EndOfMediaEvent) {
//                player.stop();
//                player.close();
//             }
//          }
//       });
//       player.realize();
//       player.start();
//    } catch (Exception e) {
//       e.printStackTrace();
//    }
// }
// 
// public static void main(String[] args) {
//    new PlayClip("song.mp3").start();
// }
//}