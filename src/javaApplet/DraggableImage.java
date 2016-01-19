package javaApplet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DraggableImage {

    public static void main(String[] args) {
        new DraggableImage();
    }

    public DraggableImage() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ImagePane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class ImagePane extends JPanel {

        private BufferedImage img;

        private Point offset = new Point(0, 0);

        public ImagePane() {
            try {
                img = ImageIO.read(new java.io.File("src/javaApplet/Inventory02_0002_items_bg_white_smaller_edited.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            MouseAdapter ma = new MouseAdapter() {

                private Point startPoint;

                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                    startPoint.x -= offset.x;
                    startPoint.y -= offset.y;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    startPoint = null;
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    Point p = e.getPoint();
                    int x = p.x - startPoint.x;
                    int y = p.y - startPoint.x;
                    offset = new Point(x, y);
                    repaint();
                }

            };

            addMouseListener(ma);
            addMouseMotionListener(ma);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                if (offset == null) {
                    offset = new Point(0, 0);
                }
                g2d.drawImage(img, offset.x, offset.y, this);
                g2d.dispose();
            }
        }

    }

}
//public class DragTest extends JFrame implements MouseMotionListener,
//        MouseListener {
//
//    private JPanel leftPanel = new JPanel(null);
//    private JPanel rightPanel = new JPanel(null);
//    JLabel dropLabel;
//
//    public DragTest() {
//        this.setLayout(new GridLayout(1, 2));
//
//        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
//        rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
//        this.add(leftPanel);
//        this.add(rightPanel);
//        leftPanel.addMouseListener(this);
//        leftPanel.addMouseMotionListener(this);
//
//        JTextArea area = new JTextArea();
//
//        rightPanel.setLayout(new GridLayout(1, 1));
//        rightPanel.add(area);
//
//        dropLabel = new JLabel("drop");
//        dropLabel.setTransferHandler(new TransferHandler("text"));
//
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        System.out.println("mousePressed");
//
//
//        Dimension labelSize = dropLabel.getPreferredSize();
//        dropLabel.setSize(labelSize);
//        int x = e.getX() - labelSize.width / 2;
//        int y = e.getY() - labelSize.height / 2;
//        dropLabel.setLocation(x, y);
//        leftPanel.add(dropLabel);
//
//
//        dropLabel.getTransferHandler().exportAsDrag(dropLabel, e,
//                TransferHandler.COPY);
//
//        repaint();
//
//    }
//
//    @Override
//    public void mouseDragged(MouseEvent me) {
//        System.out.println("mouseDragged");
//    }
//
//    @Override
//    public void mouseMoved(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        System.out.println("mouseClicked");
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//
//        System.out.println("mouseReleased");
//
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//
//    }
//
//    public static void main(String[] args) {
//
//        DragTest frame = new DragTest();
//        frame.setVisible(true);
//        frame.setSize(600, 400);
//        frame.setResizable(false);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//}