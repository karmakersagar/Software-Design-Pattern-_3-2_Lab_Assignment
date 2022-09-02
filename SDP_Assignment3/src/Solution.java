import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;

//import static javax.swing.text.html.parser.DTDConstants.ID;

class UiElement {

    private String type,value;
    ;
    private String xValue,yValue;

    public UiElement(String type, String value, String xValue, String yValue) {
        this.type = type;
        this.value = value;
        this.xValue = xValue;
        this.yValue = yValue;
    }

    @Override
    public String toString() {
        return "<" + type + ", " + value + ", " + xValue + ", " + yValue + ">";
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
    public ConfigTextParser(String textFileInput) throws Exception{
        File inputTextFile = new File(textFileInput);
        bufferedReader = new BufferedReader(new FileReader(inputTextFile));
    }

    @Override
    public List<UiElement> configLoad() throws Exception {
        List<UiElement> configType = new ArrayList<>();
        LineNumberReader lr = new LineNumberReader(bufferedReader);
        String line ;
        while ((line = lr.readLine()) != null) {
            //System.out.println(line);
            String[] conFigMessage = line.split(",");

            //System.out.println(conFigMessage[0]);
            configType.add(new UiElement(conFigMessage[0],conFigMessage[1],conFigMessage[2],conFigMessage[3]));
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
                itemList.add(new UiElement(elem.getNodeName(), elem.getAttribute("value"), elem.getAttribute("X"), elem.getAttribute("Y")));
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

class WindowManager{
    private static  WindowManager windowManager;
    private  WindowManager(){

    }

    public static  WindowManager getInstance(){
        if(windowManager == null){
            windowManager = new WindowManager();
        }
        return windowManager;
    }

    public void loadUI(ConfigManager configManager) {
        while(configManager.hasMoreItem()) {
            UiElement element = configManager.nextItem();
            System.out.println(element);
        }
    }
}



public class Solution {

    public static void main(String [] args) throws Exception {
        ///ConfigManagerAdapter configManagerAdapter = new XmlConfig();
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
                System.out.println(fileName);
                WindowManager.getInstance().loadUI(new ConfigXmlManager("src/"+fileName));
                //XmlDomParser xmlDomParser = new XmlDomParser();
                break;
            case 2 :
                System.out.println("Config file choose");
                Scanner txtScanner = new Scanner(System.in);
                fileName = txtScanner.nextLine();
                System.out.println(fileName);
                WindowManager.getInstance().loadUI(new ConfigDefaultTextManager("src/" + fileName));
                break;
            case 3 :
                System.out.println("Tata bye bye gaya");
                break;
        }
    }


}

