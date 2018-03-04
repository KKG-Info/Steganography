import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.image.BufferedImage;

/**
 *
 * Description
 *
 * @version 1.0 from 26/02/2018
 * @author 
 */

public class View extends JFrame implements ActionListener {
  private final String imageS = "Image: ";
  private final String fileS = "File: ";
  private final int space = 8;
  private Container cp;
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menu = new JMenu("Menu");
  private JMenuItem mIEncry = new JMenuItem("Encryption");
  private JMenuItem mIDecry = new JMenuItem("Decryption");
  private JButton btContainerChoose = new JButton("Choose");
  private JButton btHideChoose = new JButton("Choose");
  private JButton btEncry = new JButton("Encrypt");
  private JLabel lbCPath = new JLabel(imageS);
  private JLabel lbHPath = new JLabel(fileS);
  private JLabel lbContainer = new JLabel("Container:");
  private JLabel lbHide = new JLabel("Hide:");
  private JLabel lbOr = new JLabel("Or:");
  private JTextArea taHide = new JTextArea();
  private JFileChooser containerChooser = new JFileChooser();
  private JFileChooser hideChooser = new JFileChooser();
  private File containerFile;
  private File hideFile;
  private File directory = containerChooser.getCurrentDirectory();
  
  private Object[] order = {lbContainer, btContainerChoose, lbHide, btHideChoose};
 
  //private FileFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
  
  // end attributes
  
  public View() { 
    // Frame-Init
    super();
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 506; 
    int frameHeight = 335;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setTitle("Steganography");
    setResizable(false);
    cp = getContentPane();
    cp.setLayout(null);
    // start components
    setJMenuBar(menuBar);
    menuBar.add(menu);
    menu.add(mIEncry);
    menu.add(mIDecry); 
    mIEncry.addActionListener(this);
    mIDecry.addActionListener(this);
    
    containerChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png)", "jpg", "png"));
    containerChooser.setAcceptAllFileFilterUsed(false);
    
    hideChooser.setFileFilter(new FileNameExtensionFilter("Selectable Files (jpg, png, txt)", "jpg", "png", "txt"));
    hideChooser.setAcceptAllFileFilterUsed(false);
    
    lbContainer.setBounds(space, space, 70, 16);
    
    btContainerChoose.setVisible(true);
    btContainerChoose.setBounds(space, 32, 80, 25);
    btContainerChoose.addActionListener(this);
    
    lbCPath.setBounds(96, 32, 200, 25);
    
    lbHide.setBounds(space, 71, 70, 16);
    lbHide.setEnabled(false);
    
    taHide.setBounds(space, 94, 200, 120);
    taHide.setLineWrap(true);
    taHide.setEnabled(false);
    
    lbOr.setBounds(216, 189, 25, 25);
    lbOr.setEnabled(false);
    
    btHideChoose.setBounds(249, 189, 80, 25);
    btHideChoose.setEnabled(false);
    
    lbHPath.setBounds(336, 189, 170, 25);
    lbHPath.setEnabled(false);
    
    btEncry.setBounds(space, 230, 80, 30);
    btEncry.setEnabled(false);
    
    
    
    encryView();
    btHideChoose.addActionListener(this);
    
    setVisible(true);
  }
  
  // start methods
  
  public static void main(String[] args) {
    new View();
  }
  
  public void encryView() {
    cp.removeAll();
    cp.add(lbContainer);
    cp.add(btContainerChoose);
    lbCPath.setText(this.imageS);
    cp.add(lbCPath);
    cp.add(lbHide);
    cp.add(taHide);
    cp.add(lbOr);           
    cp.add(btHideChoose);
    lbHPath.setText(this.fileS);
    cp.add(lbHPath);    
    cp.add(btEncry);
    reset(containerChooser);
    reset(hideChooser);
    
    cp.repaint();
    validate();
  }
  
  public void decryView() {
    cp.removeAll();
    cp.repaint();
    validate();
  }

  public void reset(JFileChooser chooser) {
    chooser.setSelectedFile(new File(""));
    chooser.setCurrentDirectory(this.directory);
  }

  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.btContainerChoose) {
      containerFile = openFile(containerChooser);
      lbCPath.setText(this.imageS + this.containerFile.getName());
      btHideChoose.setEnabled(true);
      taHide.setEnabled(true);
      lbHide.setEnabled(true);
      lbHPath.setEnabled(true);
      lbOr.setEnabled(true);
    } else if (e.getSource() == this.btHideChoose){
      hideFile = openFile(hideChooser);
      lbHPath.setText(this.fileS + this.hideFile.getName());
      taHide.setEnabled(false);
    } else if (e.getSource() == this.mIEncry) {
      encryView();
    } else if (e.getSource() == this.mIDecry) {
      decryView();
    }
  }

  public File openFile(JFileChooser chooser) {
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    } else {
      return new File("");
    }
  }                 
   /*
  private int calculateY(Object obj) {
    int y = 0;
    if (obj.equals(this.lbContainer)) {
      int index = indexOf(this.order, lbContainer);
      if (index != 0) {
        y = this.order[index - 1].getY() + this.order[index - 1].getHeight() + this.space;
      } else {
        y = this.space;
      }
    } else if (obj.equals(this.btContainerChoose)) {
      int index = indexOf(this.order, btContainerChoose);
      if (index != 0) {
        y = this.order[index - 1].getY() + this.order[index - 1].getHeight() + this.space;
      } else {
        y = this.space;
      }
      //y = this.lbContainer.getY() + this.lbContainer.getHeight() + this.space;
    } else if (obj.equals(this.lbPath)) {
      
    }
 
    return y;
  }  */
    
  private int indexOf(Object[] order, Object obj) {
    for (int i = 0; i < order.length; i++) {
      if (order[i].equals(obj)) {
        return i;
      }
      
    }
    return -1;
  }


  // end methods
}

