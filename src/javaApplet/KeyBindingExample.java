package javaApplet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/* MyLayeredPaneDemo.java requires no other files. */
public class KeyBindingExample {
    private JFrame frame;
    private JLayeredPane mainPanel;
    private JPanel constantLayer;
    private JPanel floatingLayer;
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private KeyBindingExample() {}
    private void createAndShowGUI() {
        //Create and set up the window.
        this.frame = new JFrame("MyLayeredPaneDemo");
        mainPanel = new JLayeredPane();
        
        constantLayer = new JPanel();
        floatingLayer = new JPanel();
        constantLayer.setBackground(Color.BLUE);

        floatingLayer.setBackground(Color.YELLOW);
        floatingLayer.setVisible(true);

        mainPanel.add(constantLayer, new Integer(0));
        constantLayer.setBounds(0,0,640,480);
        mainPanel.add(floatingLayer, new Integer(-1));
        floatingLayer.setBounds(100, 360, 300, 90 );

        frame.add(mainPanel);

        //Display the window.
        mapKeyToAction(frame.getRootPane(), 
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0),
                "Hide Layer", 
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("F7 pressed");
                        mainPanel.setLayer(floatingLayer, new Integer(-1));
                    }       
                }); 
        mapKeyToAction(frame.getRootPane(), 
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0),
                "Show Layer", 
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("F8 pressed");
                        mainPanel.setLayer(floatingLayer, new Integer(1));
                    }       
                }); 
        frame.pack();
        frame.setVisible(true);
        frame.getRootPane().setFocusable(true);
        boolean ok = frame.getRootPane().requestFocusInWindow();
        System.out.println("focus ok: " + ok);

    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new KeyBindingExample().createAndShowGUI();
            }
        });
    }

    private static void mapKeyToAction(JComponent component, 
            int whichMap, KeyStroke keystroke,String key, Action action) {
        component.getInputMap(whichMap).put(keystroke, key);
        component.getActionMap().put(key, action);
    }
}

//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//
//public class KeyBindingExample {
//
//  public static void main(final String args[]) {
//    final String labels[] = { "A", "B", "C", "D", "E" };
//    JFrame frame = new JFrame("Editable JComboBox");
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//    final JComboBox comboBox = new JComboBox(labels);
//    comboBox.setMaximumRowCount(5);
//    comboBox.setEditable(true);
//    frame.add(comboBox, BorderLayout.NORTH);
//
//    ActionListener actionListener = new ActionListener() {
//      public void actionPerformed(ActionEvent actionEvent) {
//        System.out.println("Selected: " + comboBox.getSelectedItem());
//        System.out.println(", Position: " + comboBox.getSelectedIndex());
//      }
//    };
//    comboBox.addActionListener(actionListener);
//
//    frame.setSize(300, 200);
//    frame.setVisible(true);
//
//  }
//
//}


//
//import java.awt.event.ActionEvent;
//
//import java.awt.event.ActionListener;
//import javax.swing.AbstractAction;
//import javax.swing.Action;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.KeyStroke;
//
//public class KeyBindingExample {
//    private static JFrame mainFrame;
//    private static JTextField dataField;
//    private static JButton enterButton;
//    private static JPanel mainPanel;
//    private static Action enterAction;
//    private static ButtonListener buttonListener;
//    // the main() method creates a simple JFrame to demonstrate the
//    // key binding of the enter key to the component button "enter"
//    public static void main( String[] args )
//    {
//        mainFrame = new JFrame( "Key Binding Example" ); 
//        mainFrame.add( makePanel() );
//        mainFrame.setLocationRelativeTo( null );
//        mainFrame.setSize( 200, 100 );
//        mainFrame.setResizable( false );
//        mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//        mainFrame.setVisible( true );
//    } // end method main()
//    static JPanel makePanel()
//    {
//        mainPanel = new JPanel();
//        buttonListener = new ButtonListener();
//        dataField = new JTextField( 15 );
//        
//        enterButton = new JButton( "Enter" );
//        enterButton.addActionListener( buttonListener );
//        
//        enterAction = new EnterAction();
//        
//        dataField.getInputMap().put( KeyStroke.getKeyStroke( "ENTER" ), "doEnterAction" );
//        dataField.getActionMap().put( "doEnterAction", enterAction );
//        mainPanel.add( dataField );
////        mainPanel.add( enterButton );
//        return mainPanel;
//    }
//// class EnterAction is an AbstractAction that defines what will occur
//    // when the enter key is pressed.
//    static class EnterAction extends AbstractAction {
//    	public void actionPerformed( ActionEvent tf ) {
//    		System.out.println( "The Enter key has been pressed : " + dataField.getText());
//            enterButton.doClick();
//            dataField.setText("");
//    	}
//    }
//            
//    static class ButtonListener implements ActionListener {
//    	public void actionPerformed( ActionEvent bp ) {
//    	System.out.println( "The enter button was pressed." );
//
//    	dataField.requestFocusInWindow();
//    	dataField.selectAll();
//        } // end method actionPerformed()
//    } // end class ButtonListener
//} // end class KeyBindingExample
