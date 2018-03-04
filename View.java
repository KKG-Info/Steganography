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
  // start attributes
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menu = new JMenu("Menu");
  private JMenuItem mIEncry = new JMenuItem("Encryption");
  private JMenuItem mIDecry = new JMenuItem("Decryption");
  private JButton btFileChoose = new JButton("Choose");
  private JLabel lbPath = new JLabel("");
  private File containerFile;
  
  private JFileChooser containerChooser = new JFileChooser();
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
    Container cp = getContentPane();
    cp.setLayout(null);
    // start components
    setJMenuBar(menuBar);
    menuBar.add(menu);
    menu.add(mIEncry);
    menu.add(mIDecry); 
    
    containerChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
    containerChooser.setAcceptAllFileFilterUsed(false);
    
    btFileChoose.setVisible(true);
    btFileChoose.setBounds(8, 8, 80, 25);
    btFileChoose.setText("Choose");
    btFileChoose.addActionListener(this);
    cp.add(btFileChoose);
    
    lbPath.setBounds(96, 8, 200, 25);
    cp.add(lbPath);
    
    
    setVisible(true);
  }
  
  // start methods
  
  public static void main(String[] args) {
    new View();
  }
  
  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.btFileChoose) {
      this.containerFile = openFile(containerChooser);
      lbPath.setText(this.containerFile.getName());
      
    }
  }

  public File openFile(JFileChooser chooser) {
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    } else {
      return null;
    }
  }

  // end methods
}

