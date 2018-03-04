import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Steganography {

  // start attributes
  private boolean hideDebug = false;
  private boolean discoverDebug = true;
  private String prefix = "101111011110001010100101111101100101001010100101";
  private String prefixImg = "101111011110001010100101111101100101001010101010";
  // end attributes
  
  public Steganography(){}
  // start methods
  
    
  public void hide(BufferedImage container, BufferedImage hide) {
    if (hide.getWidth() * hide.getHeight() * 8 > container.getWidth() * container.getHeight()) {
      System.out.println("Image too big. Cannot be hidden.");
      return;
    }
    
    String text = parseImage(hide);
    String binary = this.prefixImg +  getBinSize(container, hide) + text;
    if (hideDebug)
      System.out.println("binary: " + binary);
    
    int pos = 0;   
    for (int y = 0; y < (binary.length() / 4) / container.getWidth() + 1; y++) {
      for (int x = 0; x < container.getWidth() && pos < binary.length(); x++) {   
        int rgb = container.getRGB(x, y);
        String bits = binary.substring(pos, pos+4);    
        container.setRGB(x, y, bitInPixel(bits, rgb)); 
        if (hideDebug) {
          System.out.println("bits: " + bits);
          System.out.println(Integer.toBinaryString(container.getRGB(x, y)));
        }
        pos+=4;
      }       
    }
    //return hideText(container, text);
  }

  
  public void hideText(BufferedImage img, String text){  
    if (text.length() > maxLength(img)) {
      System.out.println("Text zu lang!");                     
      return;
    } else if (text.length() == 0){
      System.out.println("No Text.");
      return;
    } // end of if-else
    String binary = this.prefix + getBinLen(img, text) + toBin(text);
    if (hideDebug)
      System.out.println("binary: " + binary);
    
    int pos = 0;   
    for (int y = 0; y < (binary.length() / 4) / img.getWidth() + 1; y++) {
      for (int x = 0; x < img.getWidth() && pos < binary.length(); x++) {   
        int rgb = img.getRGB(x, y);
        String bits = binary.substring(pos, pos+4);    
        img.setRGB(x, y, bitInPixel(bits, rgb)); 
        if (hideDebug) {
          System.out.println("bits: " + bits);
          System.out.println(Integer.toBinaryString(img.getRGB(x, y)));
        }
        pos+=4;
      }       
    }
    
    //return img;
  }
    
  public String discoverText(BufferedImage img){
    int position = 0;
    int wid = img.getWidth();
    int line = 0;
    int column = 0;
    
    String prefix = "";
    while (position < this.prefix.length() / 4) { 
      if (column == img.getWidth()) {
        column = 0;
        line++;
      }
      int rgb = img.getRGB(column, line); 
      prefix += pixelInBit(rgb);    
      column++;
      position++;
    }
    
    if (discoverDebug)
      System.out.println("präfix: " + prefix);
    
    if (!prefix.equals(this.prefix)) {
      System.out.println("Kein Text im Bild gefunden! Grund: Praefix nicht vorhanden.");
      return "";
    }     
    
    String lenBitfolge = "";
    while (position < this.prefix.length() / 4 + getBinLenLen(img)) { 
      if (column == img.getWidth()) {
        column = 0;
        line++;
      }
      
      int rgb = img.getRGB(column, line);
      lenBitfolge += pixelInBit(rgb);  
      column++;
      position++;
    }
    int len = Integer.parseInt(lenBitfolge,2);
    if (discoverDebug)
      System.out.println("len: " + len);
    
    String textBitfolge = "";
    while (position < this.prefix.length() / 4 + getBinLenLen(img) + len *2) {     
      if (column == img.getWidth()) {
        column = 0;
        line++;
      }
      int rgb = img.getRGB(column, line);
      textBitfolge += pixelInBit(rgb);    
      column++;
      position++;         
    } 
    if (discoverDebug)
      System.out.println("bitfolge: " + textBitfolge);
    
    return binToString(textBitfolge);
  }    
  
  public BufferedImage discoverImage(BufferedImage container) {
    int pos = 0;
    int[] data = new int[container.getHeight() * container.getWidth()];
    
    for (int y = 0; y < container.getHeight(); y++) {
      for (int x = 0; x < container.getWidth(); x++) {
        data[y*container.getWidth() + x] = container.getRGB(x, y);
        //System.out.println(Integer.toBinaryString(data[y*container.getWidth() + x]));
      }
    } 
    
    String prefix = "";
    for (int i = pos; i < pos + this.prefix.length() / 4; i++) {
      prefix += pixelInBit(data[i]);
    }
    pos += this.prefix.length() / 4;
    
    if (!prefix.equals(this.prefixImg)) {
      System.out.println("No Prefix!");
      return container;
    }
    
    
    String widthImgS = "";
    int widthBinLen = Integer.toBinaryString(container.getWidth()).length();
    for (int i = pos; i < pos + (widthBinLen + (4 - widthBinLen % 4)) / 4; i++) {
      widthImgS += pixelInBit(data[i]);
    }
    pos += (widthBinLen + (4 - widthBinLen % 4)) / 4;
    int widthImg = Integer.parseInt(widthImgS, 2);
    
    int heightBinLen = Integer.toBinaryString(container.getHeight()).length();
    String heightImgS = "";
    for (int i = pos; i < pos + (heightBinLen + (4 - heightBinLen % 4)) / 4; i++) {
      heightImgS += pixelInBit(data[i]);
    }
    pos += (heightBinLen + (4 - heightBinLen % 4)) / 4;
    int heightImg = Integer.parseInt(heightImgS, 2);
    
    //System.out.println("breite: " + widthImg + ", hoehe: " + heightImg);
    
    String[] dataImg = new String[widthImg * heightImg];
    int index;
    for (int i = pos; i < pos + widthImg * heightImg * 8; i++) {
      index = (i - pos) / 8;
      if ((i - pos) % 8 == 0) {
        dataImg[index] = pixelInBit(data[i]);
      } else {
        dataImg[index] += pixelInBit(data[i]);
      }
    }
    return parseImage(dataImg, widthImg, heightImg);
  }

    
  public String pixelInBit(int rgb) {
    int a = (rgb >> 24) & 0x1;
    int r = (rgb >> 16) & 0x1;
    int g = (rgb >> 8) & 0x1;
    int b = rgb & 0x1;
    
    return String.valueOf(a) + String.valueOf(r) + String.valueOf(g) + String.valueOf(b);
  }     
    
  public String getBinLen(BufferedImage img, String text) {
    int maxLen = maxLength(img);
    String lenS = Integer.toBinaryString(text.length());
    String maxLenS = Integer.toBinaryString(maxLen);
    if (text.length() <= maxLen) {
      lenS = auffuellen(lenS, maxLenS.length() + (4 - maxLenS.length() % 4));
    } else {
      System.out.println("Text zu lang!");
    }
    return lenS;
  }
  
  public String getBinSize(BufferedImage container, BufferedImage img) {
    if (tooBig(container, img)) {
      System.out.println("Image too big. Cannot be hidden.");
      return "";
    }
    
    String widthBin = Integer.toBinaryString(img.getWidth());
    String heightBin = Integer.toBinaryString(img.getHeight());
    
    int widthBinLen = Integer.toBinaryString(container.getWidth()).length();
    int heightBinLen = Integer.toBinaryString(container.getHeight()).length();
    
    widthBin = auffuellen(widthBin, widthBinLen + (4 - widthBinLen % 4));
    heightBin = auffuellen(heightBin, heightBinLen + (4 - heightBinLen % 4));
    
    String sizeS = widthBin + heightBin;
    
    return sizeS;
  } 
    
  public int getBinSizeLen(BufferedImage img) {
    String heightBin = Integer.toBinaryString(img.getHeight());
    String widthBin = Integer.toBinaryString(img.getWidth());
    return (heightBin.length() + widthBin.length());
  }            
    
  public int getBinLenLen(BufferedImage img) {
    int maxLen = maxLength(img);
    String maxLenS = Integer.toBinaryString(maxLen);
    return (maxLenS.length() + (4 - maxLenS.length() % 4)) / 4;
  }
    
  public String auffuellen(String bin, int len) {
    while (bin.length() < len) { 
      bin = "0" + bin;
    }
    return bin;  
  }
    
  public int maxLength(BufferedImage image) {
    int len = 0; 
    len = (image.getWidth() * image.getHeight() * 4 - this.prefix.length()) / 8;
    String lenS = Integer.toBinaryString(len);
    if (lenS.length() % 8 == 0) {
      len -= lenS.length() / 8;
    } else {
      len -= lenS.length() / 8 + 1;
    }
    return len - 1;
  }

  public boolean tooBig(BufferedImage container, BufferedImage img) {
    int sizeC = container.getHeight() * container.getWidth();
    int sizeI = img.getHeight() * img.getWidth();
    return (sizeC / 8 - prefixImg.length() - (Integer.toBinaryString(container.getHeight()) + Integer.toBinaryString(container.getWidth())).length()) <= sizeI;
  }
    
  public int bitInPixel(String bits, int rgb){
    int[] bitValues = new int[4];
    bitValues[0] = Integer.parseInt(bits.substring(0, 1));
    bitValues[1] = Integer.parseInt(bits.substring(1, 2));
    bitValues[2] = Integer.parseInt(bits.substring(2, 3));
    bitValues[3] = Integer.parseInt(bits.substring(3, 4));        
    
    int a = (rgb >>> 25)*2 + bitValues[0];
    int r = ((rgb >>> 17)*2 + bitValues[1]) & 0xFF; 
    int g = ((rgb >>> 9)*2 + bitValues[2]) & 0xFF;   
    int b = ((rgb >>> 1)*2 + bitValues[3]) & 0xFF;   
    
    
    if (hideDebug) {
      System.out.println("a: " + Integer.toBinaryString(a));
      System.out.println("r: " + Integer.toBinaryString(r));
      System.out.println("g: " + Integer.toBinaryString(g));
      System.out.println("b: " + Integer.toBinaryString(b));
    }
    
    rgb = (a << 24) + (r << 16) + (g << 8) + b;    
    return rgb;   
  }
    
  public String toBin(String characters) {
    String bin = "";
    for (int i = 0; i < characters.length(); i++) {
      int ascii = (int) characters.charAt(i);
      String asciiS = Integer.toBinaryString(ascii);
      while (asciiS.length() < 8) { 
        asciiS = "0" + asciiS;
      } // end of while
      bin += asciiS;
    }
    return bin;
  }     
    
  public String binToString(String bin) {
    String characters = "";
    if (bin.length() % 8 == 0) { 
      for (int i = 0; i < bin.length() / 8; i++) {
        String bits = bin.substring(i * 8, (i+1) * 8);
        int ascii = Integer.parseInt(bits, 2);
        char c = (char) ascii;
        characters += String.valueOf(c);
      }
    }
    return characters;
  }
    // end methods
    
  public String parseImage(BufferedImage img) {
    String data = "";
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        data += Integer.toBinaryString(img.getRGB(x, y));
      }
    }
    return data;
  }
    
    
  public BufferedImage parseImage(String[] data, int width, int height) {
    if (data.length > width * height) {
      System.out.println("Data is too long or size is not correct!");
      return null;
    }
    
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        long rgb = Long.parseLong(data[y*width + x], 2);
        img.setRGB(x, y, (int) rgb);
      }
    }
    return img;
  }

}
  
