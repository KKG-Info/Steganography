import java.io.File;
import java.util.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;
import java.lang.Object;
import java.awt.Graphics2D;

public class Tester {

  static String formatOut = "jpg";
  static IIOMetadata imageMetadata = null;
       
  public static void main(String args[]) {
    Steganography steg = new Steganography();
    String location = "C:\\Users\\User\\Documents\\Steganography\\";
    
    File inputFile = new File(location + "index.png");
    BufferedImage img = readImg(inputFile);
    printData(img);

    String text = "";
    System.out.println("Zu schreibender Text: " + text);

    System.out.println(" - Verstecken des Textes im BufferedImage - ");
    steg.hideText(img, text);

    System.out.println(" - Auslesen des Textes aus dem BufferedImage - ");
    String discoverText = steg.discoverText(img);
    System.out.println("text: " + discoverText);
    if (text.equals(discoverText)) {
      System.out.println(" - Texte stimmen ueberein! - ");
    } else {
      System.out.println(" - Texte stimmen nicht ueberein! - ");
    }
    System.out.println();
     
    System.out.println(" - Exportieren des bearbeiteten Bildes in " + location + " - ");
    String outputFileName = "output.png";
    File outputImage = new File(location + outputFileName);
    export(img, outputImage);


    System.out.println(" - Lesen des bearbeiteten Bildes in " + location + " - ");
    File outputFile = new File(location + outputFileName);
    img = readImg(outputFile);
    printData(img);

    System.out.println(" - Auslesen des Textes aus dem exportierten Bild - ");
    discoverText = steg.discoverText(img);
    System.out.println("text: " + discoverText);
    if (text.equals(discoverText)) {
      System.out.println(" - Texte stimmen ueberein! - ");
    } else {
      System.out.println(" - Texte stimmen nicht ueberein! - ");
    }


    System.out.println(" - Bild in Bild - ");
    BufferedImage container = readImg(new File(location + "katze.jpg"));
    BufferedImage hide = readImg(new File(location + "ui.png"));
    steg.hide(container, hide);
    export(container, new File(location + "katze.png"));
    container = readImg(new File(location + "katze.png"));
    export(steg.discoverImage(container), new File(location + "hide - container.png")); 
    System.out.println(" - Bild exportiert - ");      
  }
  public static void printData(BufferedImage img) {
    Steganography steg = new Steganography();

    System.out.println();
    System.out.println("Bild:");
    System.out.println("  Breite: " + img.getWidth());
    System.out.println("  Hoehe:  " + img.getHeight());
    System.out.println("  MaxLen: " + steg.maxLength(img));
    System.out.println();

  }

  public static void printPixel(BufferedImage img) {
    System.out.println("pixel: ");
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        System.out.println(Integer.toBinaryString(img.getRGB(x, y)));
      }
    }
    System.out.println("...pixel");
  }
  
  public static BufferedImage readImg(File inputFile) {
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

  public static void export(BufferedImage img, File outputFile) {
    try{
      // Erstellen eines passenden IIOImage (Klasse für Bilddateien mitsamt Metadaten und ...) für das Schreiben
      IIOImage imgOut = new IIOImage(img, null, imageMetadata);
      
      // Writer dem definierten Ausgabeformat entsprechend erstellen
      ImageWriter write = ImageIO.getImageWritersBySuffix("png").next();
      ImageWriteParam param = write.getDefaultWriteParam();
      
      if (formatOut=="jpg") {
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1f);
      } // end of if
      else {
      } // end of if-else

      // Laden eines Output-Streams mit der Ausgabdatei als Vorgabe
      FileImageOutputStream fios = new FileImageOutputStream(outputFile);
      
      write.setOutput(fios); // Einsetzen des erstellten Streams zur Ausgabe
      
      write.write(null, imgOut, param); // Schreiben
      
      write.dispose(); // Dispose
    }catch(IOException e){
      System.out.println(e);
    }
  }

}
