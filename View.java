import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import javax.imageio.*;
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
  private final String imageS = "";//"Image: ";
  private final String fileS = "";//"File: ";
  private final int space = 8;
  private int type = 0; //0: nothing, 1: text, 2: image
  private int foundType = 0;
  private String foundText = "";
  private BufferedImage foundImg;
  //private CardLayout layout = new CardLayout();
  //private JPanel cardPanel = new JPanel(layout);
  private Container cp;
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menu = new JMenu("Menu");
  private JMenuItem mIEncry = new JMenuItem("Encryption");
  private JMenuItem mIDecry = new JMenuItem("Decryption");
  private JButton btContainerChoose = new JButton("Choose");
  private JButton btHideChoose = new JButton("Choose");
  private JButton btEncry = new JButton("Encrypt");
  private JButton btImageChooser = new JButton("Choose");
  private JButton btDecry = new JButton("Decrypt");
  private JButton btSave = new JButton("Save");
  private JLabel lbCPath = new JLabel(imageS);
  private JLabel lbHPath = new JLabel(fileS);
  private JLabel lbContainer = new JLabel("Container:");
  private JLabel lbHide = new JLabel("Hide:");
  private JLabel lbOr = new JLabel("Or:");
  private JLabel lbImage = new JLabel("Image:");
  private JLabel lbResult = new JLabel();
  private JTextArea taHide = new JTextArea();
  private JFileChooser containerChooser = new JFileChooser();
  private JFileChooser hideChooser = new JFileChooser();
  private JFileChooser decryChooser = new JFileChooser();
  private JFileChooser imgSaver = new JFileChooser();
  private JFileChooser txtSaver = new JFileChooser();
  private File containerFile;
  private File hideFile;
  private File decryFile;
  private File directory = containerChooser.getCurrentDirectory();
  private Steganography steg = new Steganography();

  //private Object[] order = {lbContainer, btContainerChoose, lbHide, btHideChoose};
  public View() {
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
    setJMenuBar(menuBar);
    menuBar.add(menu);
    menu.add(mIEncry);
    menu.add(mIDecry);
    mIEncry.addActionListener(this);
    mIDecry.addActionListener(this);

    containerChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png)", "jpg", "JPG", "jpeg", "JPEG", "png", "PNG"));
    containerChooser.setAcceptAllFileFilterUsed(false);
    decryChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png)", "jpg", "JPG", "jpeg", "JPEG", "png", "PNG"));
    decryChooser.setAcceptAllFileFilterUsed(false);
    hideChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png)", "jpg", "JPG", "jpeg", "JPEG", "png", "PNG"));
    hideChooser.setAcceptAllFileFilterUsed(false);
    imgSaver.setFileFilter(new FileNameExtensionFilter("Image", "png"));
    imgSaver.setAcceptAllFileFilterUsed(false);
    txtSaver.setFileFilter(new FileNameExtensionFilter("Textfile", "txt"));
    txtSaver.setAcceptAllFileFilterUsed(false);
    taHide.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
           if (taHide.getText().equals("")) {
             lbHPath.setEnabled(true);
             btHideChoose.setEnabled(true);
             lbOr.setEnabled(true);
             btEncry.setEnabled(false);
             type = 0;
           }
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
          if (!taHide.getText().equals("")) {
            lbHPath.setEnabled(false);
            btHideChoose.setEnabled(false);
            lbOr.setEnabled(false);
            btEncry.setEnabled(true);
            type = 1;
          }
        }
        @Override
        public void changedUpdate(DocumentEvent arg0) {
        }
    });
    lbContainer.setBounds(space, space, 70, 16);
    lbImage.setBounds(space, space, 70, 16);
    btContainerChoose.setBounds(space, 32, 80, 25);
    btContainerChoose.addActionListener(this);
    lbCPath.setBounds(96, 32, 200, 25);
    lbHide.setBounds(space, 71, 70, 16);
    taHide.setBounds(space, 94, 200, 120);
    taHide.setLineWrap(true);
    lbOr.setBounds(216, 189, 25, 25);
    btHideChoose.setBounds(249, 189, 80, 25);
    lbHPath.setBounds(336, 189, 170, 25);
    btEncry.setBounds(space, 230, 80, 30);
    btEncry.addActionListener(this);
    btImageChooser.setBounds(space, 32, 80, 25);
    btImageChooser.addActionListener(this);
    btDecry.setBounds(space, 65, 80, 30);
    btDecry.addActionListener(this);
    encryView();
    btHideChoose.addActionListener(this);
    lbResult.setBounds(space, 103, 465, 16);
    btSave.setBounds(space, 127, 80, 25);
    btSave.addActionListener(this);
    setVisible(true);
  }

  // start methods

  public static void main(String[] args) {
    new View();
  }

  /**
  * This method adds all items for the encryption-screen to the container and resets them.
  *
  */
  public void encryView() {
    cp.removeAll();
    cp.add(lbContainer);
    cp.add(btContainerChoose);
    lbCPath.setText(this.imageS);
    cp.add(lbCPath);
    lbHide.setEnabled(false);
    cp.add(lbHide);
    taHide.setEnabled(false);
    taHide.setText("");
    cp.add(taHide);
    lbOr.setEnabled(false);
    cp.add(lbOr);
    btHideChoose.setEnabled(false);
    cp.add(btHideChoose);
    lbHPath.setText(this.fileS);
    lbHPath.setEnabled(false);
    cp.add(lbHPath);
    btEncry.setEnabled(false);
    cp.add(btEncry);
    reset(containerChooser);
    reset(hideChooser);
    cp.repaint();
    validate();
    this.type = 0;
  }

  /**
  * This method adds all items for the decryption-screen to the container and resets them.
  *
  */
  public void decryView() {
    cp.removeAll();
    cp.add(lbImage);
    cp.add(btImageChooser);
    lbCPath.setText(this.imageS);
    cp.add(lbCPath);
    btDecry.setEnabled(false);
    cp.add(btDecry);
    lbResult.setText("");
    cp.add(lbResult);
    btSave.setEnabled(false);
    cp.add(lbResult);
    btSave.setEnabled(false);
    cp.add(btSave);

    this.foundImg = null;
    this.foundText = "";
    this.foundType = 0;


    cp.repaint();
    validate();
  }

  /**
  * This method resets the selected file and directory of a FileChooser.
  *
  * @param chooser the JFileChooser that will be resetted.
  */
  public void reset(JFileChooser chooser) {
    chooser.setSelectedFile(new File(""));
    chooser.setCurrentDirectory(this.directory);
  }

  /**
  * This method handles all the ActionListener of the Buttons.
  *
  */
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == this.btContainerChoose) {
      this.containerFile = openFile(containerChooser);
      if (this.containerFile != null) {
        lbCPath.setText(this.imageS + this.containerFile.getName());
        taHide.setText("");
        btHideChoose.setEnabled(true);
        taHide.setEnabled(true);
        lbHide.setEnabled(true);
        lbHPath.setEnabled(true);
        lbOr.setEnabled(true);
      }
    } else if (source == this.btHideChoose){
      this.hideFile = openFile(hideChooser);
      if (this.hideFile != null) {
        lbHPath.setText(this.fileS + this.hideFile.getName());
        taHide.setEnabled(false);
        btEncry.setEnabled(true);
        String ending = getType(this.hideFile);
        System.out.println(ending);
        //if (ending.equals("png") || ending.equals("PNG") || ending.equals("jpg") || ending.equals("JPG") || ending.equals("jpeg")) {
        this.type = 2;
        //}
      }
    } else if (source == this.btEncry) {
      System.out.println(this.type);
      BufferedImage container = readImg(this.containerFile);
      switch (this.type) {
        case 1:
          if (this.steg.hideText(container, taHide.getText())) {
            export(container, saveFile(imgSaver));
            reset(imgSaver);
          }
          break;
        case 2:
          if (container != null) {
            if (this.steg.hideImage(container, readImg(this.hideFile))) {
              export(container, saveFile(imgSaver));
              reset(imgSaver);
            }
          }
          break;
      }
    } else if (source == this.mIEncry) {
      encryView();
    } else if (source == this.mIDecry) {
      decryView();
    } else if (source == this.btImageChooser){
      this.decryFile = openFile(decryChooser);
      if (this.decryFile != null) {
        lbCPath.setText(this.imageS + this.decryFile.getName());
        btDecry.setEnabled(true);
      }
    } else if (source == this.btDecry) {
      if (decryFile != null) {
        BufferedImage decryImg = readImg(decryFile);
        this.foundType = this.steg.getType(decryImg);
        switch (this.foundType) {
          case 1:
            System.out.println("Text found");
            this.foundText = this.steg.discoverText(decryImg);
            lbResult.setText("Found Text: " + this.foundText);
            btSave.setEnabled(true);
            break;
          case 2:
            System.out.println("Image found");
            this.foundImg = this.steg.discoverImage(decryImg);
            lbResult.setText("Found Image: " + this.foundImg.getWidth() + " x " + this.foundImg.getHeight());
            btSave.setEnabled(true);
            break;
        }
      }
    } else if (source == this.btSave) {
      switch (this.foundType) {
        case 1:
          export(this.foundText, saveFile(txtSaver));
          reset(txtSaver);
          break;
        case 2:
          export(this.foundImg, saveFile(imgSaver));
          reset(imgSaver);
          break;
      }
    }
  }

  /**
  * This method lets the user load a file from a directory using a JFileChooser.
  *
  * @param chooser the JFileChooser that is being used for the loading
  */
  public File openFile(JFileChooser chooser) {
    int dialog = chooser.showOpenDialog(this);
    if (dialog == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    } else if (dialog == JFileChooser.CANCEL_OPTION) {
      return chooser.getSelectedFile();
    } else {
      return new File("");
    }
  }

  /**
  * This method lets the user pick a file in a directory using a JFileChooser for saving a file.
  *
  * @param chooser the JFileChooser that is being used
  */
  public File saveFile(JFileChooser chooser) {
   int dialog = chooser.showSaveDialog(this);
   if (dialog == JFileChooser.APPROVE_OPTION) {
     return chooser.getSelectedFile();
   } else if (dialog == JFileChooser.CANCEL_OPTION) {
     return chooser.getSelectedFile();
   } else {
     return new File("");
   }
  }

  /**
  * This method reads an image from a path and repaints it to an "argb"-type BufferedImage.
  *
  * @param inputFile the file to the image
  */
  public BufferedImage readImg(File inputFile) {
    System.out.println(" - Lesen des Bildes in " + inputFile.getPath());
    BufferedImage img = null;
    System.out.println(" - Umschreiben in ein BufferedImage im ARGB-Type - ");
    try {
      BufferedImage bildIn = ImageIO.read(inputFile);
      img = new BufferedImage(bildIn.getWidth(), bildIn.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = img.createGraphics();
      g.drawImage(bildIn, 0, 0, null);
      g.dispose();
    } catch(IOException e) {
      System.out.println(e);
    }
    return img;
  }

  /**
  * This method exports a BufferedImage to an imagefile.
  *
  * @param img the BufferedImage that should be exported
  * @param outputFile the File in which the image should be saved
  */
  public void export(BufferedImage img, File outputFile) {
    try {
      ImageIO.write(img, "png", outputFile);
    } catch(IOException e){
      System.out.println(e);
    }
  }

  /**
  * This method saves a String to a textfile.
  *
  * @param text the String that should be saved
  * @param outputFile the File in which the text should be saved
  */
  public void export(String text, File outputFile) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(outputFile);
      out.println(text);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
     if (out != null)
        out.close();
    }
  }

  /**
  * This method gives back the ending/format of a file.
  *
  * @param f the File of which the type should be given back
  */
  public String getType(File f) {
    String name = f.getName();
    String ending = "";
    if (f.length() > 4)
      ending = name.substring(name.length() - 3, name.length());
    return ending;
  }
}
