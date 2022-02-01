/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xml;


import climates.Climate;
import climates.Season;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ClimateReadWriter {
    
    private String climatePath;
    
    public ClimateReadWriter(){
        climatePath = "climates/";
        //climatePath = "";
    }
    
    public Climate readClimateFile(String filename)
    {
        Climate climate = null;
        File xmlFile = new File(climatePath+filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            Element elemClimate = doc.getDocumentElement();                      //root element is the climate
            String cName = doc.getDocumentElement().getAttribute("climateName"); //name is an attribute of climate
            NodeList nodeList = doc.getElementsByTagName("season");
            ArrayList<Season> seasons = new ArrayList<Season>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                seasons.add(getSeason(nodeList.item(i), doc));
            }
            
            //doc.getDocumentElement().normalize();         
            
            //now XML is loaded as Document in memory, lets convert it to Object List
            
            climate = new Climate(seasons);
            climate.setName(cName);
            
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
        return climate;
    }
    
    public void deleteClimateFile(String climateName)
    {
        File f = new File(climatePath+climateName+".xml");
        boolean b = f.delete();
    }
    
    public void updateClimateFile(String newClimateName, String oldClimateName, ArrayList<Season> seasons)
    {
        File f1 = new File(climatePath+oldClimateName+".xml");
        boolean b = f1.delete();
        try {
            writeClimateFile(newClimateName,seasons);
        } catch (TransformerException ex) {
            Logger.getLogger(ClimateReadWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeClimateFile(String newClimateName, ArrayList<Season> seasons) throws TransformerConfigurationException, TransformerException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // root elements
            Document doc = docBuilder.newDocument();
            Element climateRootElement = doc.createElement("climate");
            doc.appendChild(climateRootElement);
            
            
            
            // climate name attribute
            Attr climateName = doc.createAttribute("climateName");
            climateName.setValue(newClimateName);
            climateRootElement.setAttributeNode(climateName);
            
            // season elements
            for(int i = 0; i<seasons.size(); i++)
            {  
                Element season = doc.createElement("season");
                climateRootElement.appendChild(season);
                
                // season name elements
		Attr name = doc.createAttribute("name");
		String sName = seasons.get(i).getName();
                name.setValue(sName);
		season.setAttributeNode(name);
                
                //rainDays attribute
                Attr rainDays = doc.createAttribute("rainDays");
                rainDays.setValue(Integer.toString(seasons.get(i).getRainDays()));
                season.setAttributeNode(rainDays);
                   
                // tempRange element
		Element tempRange = doc.createElement("tempRange");
		tempRange.appendChild(doc.createTextNode("tempRange"));
		season.appendChild(tempRange);
                
                //startDate attribute
                Attr startDate = doc.createAttribute("startDate");
                LocalDate tempDate = seasons.get(i).getStartDate();
                startDate.setValue(tempDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                season.setAttributeNode(startDate);
                
                //tempRange min and max
                Attr minTemp = doc.createAttribute("minTemp");
                minTemp.setValue(Integer.toString(seasons.get(i).getTempRange().getMin()));
                tempRange.setAttributeNode(minTemp);
                Attr maxTemp = doc.createAttribute("maxTemp");
                maxTemp.setValue(Integer.toString(seasons.get(i).getTempRange().getMax()));
                tempRange.setAttributeNode(maxTemp);
                
                //humidRange element
                Element humidRange = doc.createElement("humidRange");
                humidRange.appendChild(doc.createTextNode("humidRange"));
                season.appendChild(humidRange);
                
                //humidRange min and max
                Attr minHumid = doc.createAttribute("minHumid");
                minHumid.setValue(Integer.toString(seasons.get(i).getHumidRange().getMin()));
                humidRange.setAttributeNode(minHumid);
                Attr maxHumid = doc.createAttribute("maxHumid");
                maxHumid.setValue(Integer.toString(seasons.get(i).getHumidRange().getMax()));
                humidRange.setAttributeNodeNS(maxHumid);
                
                //lightDuration element
                Element lightDuration = doc.createElement("lightDuration");
                lightDuration.appendChild(doc.createTextNode("lightDuration"));
                season.appendChild(lightDuration);
                
                //lightDuration times
                Attr start1 = doc.createAttribute("start1");
                start1.setValue(Integer.toString(seasons.get(i).getLightDuration().getStart1()));
                lightDuration.setAttributeNode(start1);
                Attr start2 = doc.createAttribute("start2");
                start2.setValue(Integer.toString(seasons.get(i).getLightDuration().getStart2()));
                lightDuration.setAttributeNode(start2);
                Attr end1 = doc.createAttribute("end1");
                end1.setValue(Integer.toString(seasons.get(i).getLightDuration().getEnd1()));
                lightDuration.setAttributeNode(end1);
                Attr end2 = doc.createAttribute("end2");
                end2.setValue(Integer.toString(seasons.get(i).getLightDuration().getEnd2()));
                lightDuration.setAttributeNode(end2);
            }
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(climatePath+newClimateName+".xml"));
            transformer.transform(domSource, streamResult);            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ClimateReadWriter.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("failure writing file");
        }
    }
    
    public Season getSeason(Node node, Document doc)
    {
        Element element = (Element) node; //this is the season node
        
        /*
        String name = getTagValue("name", doc);
        System.out.println("Season name: "+name);     
        */
        NodeList nodes = node.getChildNodes(); //list of elements in the season node
        
        //name, tempRange, humidRange, lightDuration
        String name = "";
        String minTempT = "";
        String maxTempT = "";
        String minHumidT = "";
        String maxHumidT = "";
        String start1T = "";
        String start2T = "";
        String end1T = "";
        String end2T = "";
        String rDays = "";
        String startDateT = "";
        for(int i = 0; i < nodes.getLength(); i++)//iterate through elements of season
        {
            Node iNode = nodes.item(i);
            Element iElem = (Element) iNode;
            
            if(iNode.getNodeName() == "tempRange")
            {
                minTempT = iElem.getAttribute("minTemp");
                maxTempT = iElem.getAttribute("maxTemp");
            }
            else if(iNode.getNodeName() == "humidRange")
            {
                minHumidT = iElem.getAttribute("minHumid");
                maxHumidT = iElem.getAttribute("maxHumid");
            }
            else if(iNode.getNodeName() == "lightDuration")
            {
                start1T = iElem.getAttribute("start1");
                start2T = iElem.getAttribute("start2");
                end1T = iElem.getAttribute("end1");
                end2T = iElem.getAttribute("end2");
            }
        }
        int minTemp = Integer.parseInt(minTempT);
        int maxTemp = Integer.parseInt(maxTempT);
        int minHumid = Integer.parseInt(minHumidT);
        int maxHumid = Integer.parseInt(maxHumidT);
        int startHr = Integer.parseInt(start1T);
        int startMn = Integer.parseInt(start2T);
        int endHr = Integer.parseInt(end1T);
        int endMn = Integer.parseInt(end2T);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        startDateT = element.getAttribute("startDate");
        name = element.getAttribute("name");
        rDays = element.getAttribute("rainDays");
        int rainDays = Integer.parseInt(rDays);
        LocalDate dt = LocalDate.parse(startDateT,dtf);
        Season season = new Season(name,minTemp,maxTemp,minHumid,maxHumid,startHr,startMn,endHr,endMn, rainDays);
        season.setStartDate(dt);
        return season;
    }
    
    public ArrayList<String> getClimateFileNames() {
        
        ArrayList<String> xmlList = new ArrayList<String>();
        File folder = new File(climatePath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null)
        {
            return null;
        }
        else
        {
            ArrayList<String> loadedFiles = new ArrayList<String>();
            for(int i = 0; i < listOfFiles.length; i++){
                String filename = listOfFiles[i].getName();
                if(filename.endsWith(".xml")||filename.endsWith(".XML"))
                {
                    boolean loadFile = true;
                    for(int j = 0; j < loadedFiles.size(); j++)
                    {
                        if(filename == loadedFiles.get(j))
                        {
                            loadFile = false;
                        }
                    }
                    if(loadFile == true)
                    {
                        xmlList.add(filename);
                        loadedFiles.add(filename);
                    }
                }
            }
        }
        return xmlList;
    }
        
    private static String getTagValue(String tag, Document doc) {
        NodeList nodeList = doc.getElementsByTagName(tag);
        Node node = (Node) nodeList.item(0);
        return node.getTextContent();
    }
}
