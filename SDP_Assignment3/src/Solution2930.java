import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//import static javax.swing.text.html.parser.DTDConstants.ID;

class UiElement {

    private String type,value;
    ;
    private String xValue,yValue,color , textSize;

    public UiElement(String type, String value, String xValue, String yValue,String color, String textSize) {
        this.type = type;
        this.value = value;
        this.xValue = xValue;
        this.yValue = yValue;
        this.color = color;
        this.textSize = textSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public String getyValue() {
        return yValue;
    }

    public void setyValue(String yValue) {
        this.yValue = yValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return "<" + type + ", " + value + ", " + xValue + ", " + yValue +  "," + color + "," + textSize + ">";
    }

}


interface  LoadConfig{
    public List<UiElement> configLoad() throws Exception;
}

abstract class ConfigManager{

    List<UiElement> itemList ;
    LoadConfig loadConfig;
    public boolean hasMoreItem() {
        return (!this.itemList.isEmpty());
    }

    public  UiElement nextItem(){
        UiElement item = this.itemList.get(0);
        itemList.remove(0);
        return item;

    }

    void configLoad()throws Exception{
        this.itemList = loadConfig.configLoad();
    }

}






class ConfigTextParser implements LoadConfig{

    BufferedReader bufferedReader ;
    List<UiElement> configType = new ArrayList<>();
    public ConfigTextParser(String textFileInput) throws Exception{
        File inputTextFile = new File(textFileInput);
        bufferedReader = new BufferedReader(new FileReader(inputTextFile));
    }

    @Override
    public List<UiElement> configLoad() throws Exception {

        LineNumberReader lr = new LineNumberReader(bufferedReader);
        String line ;
        while ((line = lr.readLine()) != null) {
            //System.out.println(line);
            String[] conFigMessage = line.split(",");

            //System.out.println(conFigMessage[0]);

            for(int i = 2; i < conFigMessage.length; i++) {
                conFigMessage[i] = conFigMessage[i].split(":")[1].trim();
            }
            configType.add(new UiElement(conFigMessage[0],conFigMessage[1],conFigMessage[2],conFigMessage[3],conFigMessage[4],conFigMessage[5] ));
        }

        return configType;
    }
}

class XmlDomParser{
     XmlDomParser() {};
    public  static List<UiElement> xmlFileParse(File xmlInputFile) throws Exception {
        List<UiElement> itemList = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        // parse the xml file
        Document xmlDocument = documentBuilder.parse(xmlInputFile);


        NodeList nodeList = xmlDocument.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                itemList.add(new UiElement(elem.getNodeName(), elem.getAttribute("value"), elem.getAttribute("X"), elem.getAttribute("Y"),elem.getAttribute("color"), elem.getAttribute("textSize")));
            }
        }


//        for(int i = 0; i < itemList.size(); i++) {
//            System.out.println(itemList.get(i));
//        }
        return itemList;
    }

}
class XmlConfigAdapter implements  LoadConfig{
    File configXmlFile;
    public XmlConfigAdapter(String xmlFile) throws IOException{
        configXmlFile = new File( xmlFile);
        System.out.println(configXmlFile);
    }
    @Override
    public List<UiElement> configLoad() throws Exception {

        return XmlDomParser.xmlFileParse(configXmlFile);
    }


}




class ConfigDefaultTextManager extends ConfigManager{
    public ConfigDefaultTextManager(String textInputFile) throws IOException{
        try {
            this.loadConfig = new ConfigTextParser(textInputFile);
            this.configLoad();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

class ConfigXmlManager extends ConfigManager{
    public ConfigXmlManager(String inputXmlFile) throws IOException{
        try {
            this.loadConfig = new XmlConfigAdapter(inputXmlFile);
            this.configLoad();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
class SimpleButtonUiDesign extends JButton {

    public SimpleButtonUiDesign(String type, String value, String getxValue, String getyValue, String color, String textSize) {
        int xCordinate =Integer.parseInt(getxValue);
        int yCordinate = Integer.parseInt(getyValue);
        //JButton button = new JButton(value);
        System.out.println("simple button Text : " + value);
        this.setText(value);
        this.setBounds(xCordinate-100,yCordinate-150,200,300);
        if(!(color.equals("Yellow"))){
            this.setBackground(Color.YELLOW);
        }
        else{
            this.setBackground(Color.BLUE);
        }
    }
}
class SimpleEDitBoxUiDesign extends JEditorPane{

    public SimpleEDitBoxUiDesign(String type, String value, String getxValue, String getyValue, String color, String textSize) {
        int xCordinate = Integer.parseInt(getxValue);
        int yCordinate = Integer.parseInt(getyValue);
//        JTextPane jTextPaneBox = new JTextPane();
        this.setBounds(xCordinate+150,yCordinate+150, 200,300);
        this.setText(value);
        if(!(color.equals("Purple"))){
            this.setBackground(Color.PINK);
        }
        else{
            this.setBackground(Color.BLUE);
        }

    }
}
class SimpleTextBoxUiDesign extends JTextPane{

    public SimpleTextBoxUiDesign(String type, String value, String getxValue, String getyValue, String color, String textSize) {
        int xCordinate = Integer.parseInt(getxValue);
        int yCordinate = Integer.parseInt(getyValue);
//        JTextPane jTextPaneBox = new JTextPane();
        this.setBounds(xCordinate+400,yCordinate+100, 200,300);
        this.setText(value);
        if(!(color.equals("Yellow"))){
            this.setBackground(Color.YELLOW);
        }
        else{
            this.setBackground(Color.BLUE);
        }
        this.setEditable(false);

    }
}
class SimplisticUiDesignFactory implements AbstractUiDesignFactory{

    @Override
    public JComponent generateUiItem(UiElement uiItemList) throws Exception {

        if(Objects.equals(uiItemList.getType(), "Button")){

            return new SimpleButtonUiDesign(uiItemList.getType(),uiItemList.getValue(),uiItemList.getxValue(),uiItemList.getyValue(),uiItemList.getColor(),uiItemList.getTextSize());
        }
        if(Objects.equals(uiItemList.getType(), "EditBox")){
            System.out.println("EDitBox : " + uiItemList.getType());
            return new SimpleEDitBoxUiDesign(uiItemList.getType(),uiItemList.getValue(),uiItemList.getxValue(),uiItemList.getyValue(),uiItemList.getColor(),uiItemList.getTextSize());
        }
        if(Objects.equals(uiItemList.getType(), "TextBox")) return new SimpleTextBoxUiDesign(uiItemList.getType(),uiItemList.getValue(),uiItemList.getxValue(),uiItemList.getyValue(),uiItemList.getColor(),uiItemList.getTextSize());
        else throw new RuntimeException("Invalid Command");
    }
}
class HighlyDetailedButtonUiDesign extends  JButton{

    public HighlyDetailedButtonUiDesign(String type, String value, String getxValue, String getyValue, String color, String textSize) {

        int xCordinate = Integer.parseInt(getxValue);
        int yCordinate = Integer.parseInt(getyValue);
        //JButton button = new JButton(value);
        this.setText(value);
        this.setBounds(xCordinate-100,yCordinate-150,300,400);
        //this.setOpaque(true);
        if(color!="Green"){
            this.setBackground(Color.YELLOW);
        }
        else{
            this.setBackground(Color.BLUE);
        }
        if(textSize=="Large"){
            Font font = new Font("Alamanda", Font.BOLD, 26);
            this.setFont(font);
        }
        else if(textSize == "Small"){
            Font font = new Font("Alamanda", Font.BOLD, 13);
            this.setFont(font);
        }



    }
}
class HighlyDetailedTextBoxUiDesign extends JTextPane{

    public HighlyDetailedTextBoxUiDesign(String type, String value, String getxValue, String getyValue, String color, String textSize) {
        int xCordinate = Integer.parseInt(getxValue);
        int yCordinate = Integer.parseInt(getyValue);
//        JTextPane jTextPaneBox = new JTextPane();
        this.setBounds(xCordinate+400,yCordinate+100, 200,300);
        this.setText(value);
        if(!(color.equals("Yellow"))){
            this.setBackground(Color.YELLOW);
        }
        else{
            this.setBackground(Color.BLUE);
        }
        if(textSize=="Large"){
            Font font = new Font("Alamanda", Font.BOLD, 26);
            this.setFont(font);
        }
        else if(textSize == "Small"){
            Font font = new Font("Alamanda", Font.BOLD, 13);
            this.setFont(font);
        }
        this.setEditable(false);

    }
}
class HighlyDetailedEDitBoxUiDesign extends JEditorPane{

    public HighlyDetailedEDitBoxUiDesign(String type, String value, String getxValue, String getyValue, String color, String textSize) {

        int xCordinate = Integer.parseInt(getxValue);
        int yCordinate = Integer.parseInt(getyValue);
//        JTextPane jTextPaneBox = new JTextPane();
        this.setBounds(xCordinate+250,yCordinate+250, 200,300);
        this.setText(value);
        if(!(color.equals("Purple"))){
            this.setBackground(Color.PINK);
        }
        else{
            this.setBackground(Color.BLUE);
        }
        if(textSize=="Large"){
            Font font = new Font("Alamanda", Font.BOLD, 26);
            this.setFont(font);
        }
        else if(textSize == "Small"){
            Font font = new Font("Alamanda", Font.BOLD, 13);
            this.setFont(font);
        }
    }
}

class HighlyDetailedUiDesignFactory implements AbstractUiDesignFactory{

    @Override
    public JComponent generateUiItem(UiElement uiItemlIst) throws Exception {

        System.out.println(uiItemlIst);



        if(Objects.equals(uiItemlIst.getType(), "Button")) return new HighlyDetailedButtonUiDesign(uiItemlIst.getType(),uiItemlIst.getValue(),uiItemlIst.getxValue(),uiItemlIst.getyValue(),uiItemlIst.getColor(),uiItemlIst.getTextSize());
        if(Objects.equals(uiItemlIst.getType(), "EditBox")) return new HighlyDetailedEDitBoxUiDesign(uiItemlIst.getType(),uiItemlIst.getValue(),uiItemlIst.getxValue(),uiItemlIst.getyValue(),uiItemlIst.getColor(),uiItemlIst.getTextSize());
        if(Objects.equals(uiItemlIst.getType(), "TextBox")) return new HighlyDetailedTextBoxUiDesign(uiItemlIst.getType(),uiItemlIst.getValue(),uiItemlIst.getxValue(),uiItemlIst.getyValue(),uiItemlIst.getColor(),uiItemlIst.getTextSize());
        else throw  new RuntimeException("invalid command");
    }
}
interface AbstractUiDesignFactory{
    public JComponent generateUiItem(UiElement itemlist) throws  Exception;
}



class WindowManager{
    private static  WindowManager windowManager;
    private static  AbstractUiDesignFactory abstractUiDesignFactory;

    public  String designSimplisticOrHighlyDetailed;
    JFrame uiFrame;
    public WindowManager(String designSimplisticOrHighlyDetailed){
        uiFrame = new JFrame("UiWindowScreen_029_030");
        uiFrame.setSize(1800, 1600);
        uiFrame.setLayout(null);
        uiFrame.setVisible(true);
        if(designSimplisticOrHighlyDetailed == "simplisticdesign")   abstractUiDesignFactory = new SimplisticUiDesignFactory();
        if (designSimplisticOrHighlyDetailed == "highlydetaileddesign") abstractUiDesignFactory = new HighlyDetailedUiDesignFactory();

    }

    public static  WindowManager getInstance(String designSimplisticOrHighlyDetailed) throws Exception{
        if(windowManager == null){
            windowManager = new WindowManager(designSimplisticOrHighlyDetailed);
            //throw  new Exception();
        }
        return windowManager;
    }

    public void loadUI(ConfigManager configManager) throws Exception {
        while(configManager.hasMoreItem()) {
            UiElement element = configManager.nextItem();
            //System.out.println(element);
            JComponent uiComponent = abstractUiDesignFactory.generateUiItem(element);
            uiFrame.add(uiComponent);
            uiFrame.repaint();
            System.out.println(element);
        }
    }
}



public class Solution2930 {

    public static void main(String [] args) throws Exception {

        ConfigManager configManager;
        String fileName;

        System.out.println("Choose an option :  \n"+
                "1. .XML File \n" + "2..Config File \n" +
                "3. Exit\n");
        Scanner scanner = new Scanner(System.in);
        int xmlOrConfigOrExitOption = scanner.nextInt();
        switch(xmlOrConfigOrExitOption){
            case 1 :
                System.out.println("please give a xml file path : ");
                Scanner xmlScanner = new Scanner(System.in);
                fileName = xmlScanner.nextLine();
                System.out.println("Plaese Select UI Design Style Option(Press 1 for simplistick Design & 2 For High Detailed Design : \n" +
                        "1.SimplisTic Design \n" +
                        "2. High Detailed Design\n");
                Scanner optionScanner = new Scanner(System.in);
                int opiton = optionScanner.nextInt();

                if(opiton==1)
                WindowManager.getInstance("simplisticdesign").loadUI(new ConfigXmlManager("src/"+fileName));
                else if (opiton == 2)
                    WindowManager.getInstance("highlydetaileddesign").loadUI(new ConfigXmlManager("src/"+fileName));
                else
                    System.out.println("You Choose an InValid Design Style Option ");
                //XmlDomParser xmlDomParser = new XmlDomParser();
                break;
            case 2 :
                System.out.println("Config file choose");
                Scanner txtScanner = new Scanner(System.in);
                fileName = txtScanner.nextLine();
                System.out.println(fileName);
                System.out.println("Plaese Select UI Design Style Option(Press 1 for simplistick Design & 2 For High Detailed Design : \n" +
                        "1.SimplisTic Design \n" +
                        "2. High Detailed Design\n");
                Scanner optionConfigScanner = new Scanner(System.in);
                int opitonConfig = optionConfigScanner.nextInt();

                if(opitonConfig==1)
                 WindowManager.getInstance("simplisticdesign").loadUI(new ConfigDefaultTextManager("src/" + fileName));
                if(opitonConfig==2)
                    WindowManager.getInstance("highlydetaileddesign").loadUI(new ConfigDefaultTextManager("src/" + fileName));
                else
                    System.out.println("You choose an Invalid Design Style");
                break;
            case 3 :
                System.out.println("Tata bye bye gaya");
                break;
        }
    }


}

