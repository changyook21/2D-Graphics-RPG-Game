package javaApplet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestJFrame extends JFrame
{
    private static final String INITIAL_TEXT = "Nothing Pressed";
    private static final String ADDED_TEXT = " was Pressed";
    private JLabel positionLabel;
    private JButton resetButton;
    private static int gridSize = 4;

    public TestJFrame()
    {
        super("Layout Example");
    }

    private void createAndDisplayGUI()
    {       
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        //contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        //contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));    
        JPanel labelPanel = new JPanel();
        positionLabel = new JLabel(INITIAL_TEXT, JLabel.CENTER);
        JPanel buttonLeftPanel = new JPanel();
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                positionLabel.setText(INITIAL_TEXT);
            }
        });
        labelPanel.add(positionLabel);
        buttonLeftPanel.add(resetButton);
        leftPanel.add(labelPanel);
        leftPanel.add(buttonLeftPanel);

        contentPane.add(leftPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(gridSize, gridSize, 10, 10));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        for (int i = 0; i < gridSize; i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                JButton button = new JButton("(" + i + ", " + j + ")");
                button.setActionCommand("(" + i + ", " + j + ")");
                button.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                        JButton but = (JButton) ae.getSource();
                        positionLabel.setText(
                            but.getActionCommand() + ADDED_TEXT);                           
                    }
                });
                buttonPanel.add(button);
            }
        }
        contentPane.add(buttonPanel);

        setContentPane(contentPane);
        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            gridSize = Integer.parseInt(args[0]);
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new TestJFrame().createAndDisplayGUI();
            }
        });
    }
}

//package javaApplet;
//
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//
//public class TestJFrame extends JFrame
//{
//    private static final String INITIAL_TEXT = "Nothing Pressed";
//    private static final String ADDED_TEXT = " was Pressed";
//    private JLabel positionLabel;
//    private JButton resetButton;
//    private static int gridSize = 4;
//
//    public TestJFrame()
//    {
//        super("Layout Example");
//    }
//
//    private void createAndDisplayGUI()
//    {       
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel contentPane = new JPanel();
//        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
//        contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
//
//        JPanel leftPanel = new JPanel();
//        leftPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
//        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));    
//        JPanel labelPanel = new JPanel();
//        positionLabel = new JLabel(INITIAL_TEXT, JLabel.CENTER);
//        JPanel buttonLeftPanel = new JPanel();
//        resetButton = new JButton("Reset");
//        resetButton.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent ae)
//            {
//                positionLabel.setText(INITIAL_TEXT);
//            }
//        });
//        labelPanel.add(positionLabel);
//        buttonLeftPanel.add(resetButton);
//        leftPanel.add(labelPanel);
//        leftPanel.add(buttonLeftPanel);
//
//        contentPane.add(leftPanel);
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridLayout(gridSize, gridSize, 10, 10));
//        for (int i = 0; i < gridSize; i++)
//        {
//            for (int j = 0; j < gridSize; j++)
//            {
//                JButton button = new JButton("(" + i + ", " + j + ")");
//                button.setActionCommand("(" + i + ", " + j + ")");
//                button.addActionListener(new ActionListener()
//                {
//                    public void actionPerformed(ActionEvent ae)
//                    {
//                        JButton but = (JButton) ae.getSource();
//                        positionLabel.setText(
//                            but.getActionCommand() + ADDED_TEXT);                           
//                    }
//                });
//                buttonPanel.add(button);
//            }
//        }
//        contentPane.add(buttonPanel);
//
//        setContentPane(contentPane);
//        pack();
//        setLocationByPlatform(true);
//        setVisible(true);
//    }
//
//    public static void main(String[] args)
//    {
//        if (args.length > 0)
//        {
//            gridSize = Integer.parseInt(args[0]);
//        }
//        SwingUtilities.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                new TestJFrame().createAndDisplayGUI();
//            }
//        });
//    }
//}

//package javaApplet;
//
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.geom.Ellipse2D;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//public class TestJFrame extends JPanel {
//
//	@Override
//	public void paint(Graphics g) {
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.RED);
//		g2d.fillOval(0, 0, 30, 30);
//		g2d.drawOval(0, 50, 30, 30);		
//		g2d.fillRect(50, 0, 30, 30);
//		g2d.drawRect(50, 50, 30, 30);
//
//		g2d.draw(new Ellipse2D.Double(0, 100, 30, 30));
//	}
//	
//	public static void main(String[] args) {
//		JFrame frame = new JFrame("Mini Tennis");
//		frame.add(new TestJFrame());
//		frame.setSize(300, 300);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
//}