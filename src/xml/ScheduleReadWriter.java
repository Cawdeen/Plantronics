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
import weatherfx.ScheduleProfile;
import weatherfx.ScheduledFunction;
import weatherfx.ScheduledFunction.HardwareType;

/**
 *
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class ScheduleReadWriter {
    
    private String schedulePath;
    
    public ScheduleReadWriter()
    {
        schedulePath = "schedules/";
    }

    public ArrayList<String> getScheduleFileNames() {
        ArrayList<String> xmlList = new ArrayList<String>();
        File folder = new File(schedulePath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null)
        {
            System.out.println("No files in the directory");
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

    public ScheduleProfile readScheduleFile(String filename) {
        ScheduleProfile profile = null;
        File xmlFile = new File(schedulePath+filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            Element elemSchedule = doc.getDocumentElement();                      //root element is the climate
            String sName = doc.getDocumentElement().getAttribute("scheduleName"); //name is an attribute of climate
            NodeList nodeList = doc.getElementsByTagName("function");
            ArrayList<ScheduledFunction> functions = new ArrayList<ScheduledFunction>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                functions.add(getFunction(nodeList.item(i), doc));
            }
            
            //doc.getDocumentElement().normalize();         
            
            //now XML is loaded as Document in memory, lets convert it to Object List
            
            profile = new ScheduleProfile(functions);
            profile.setName(sName);
            
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
        return profile;
    }
    
    public void deleteScheduleFile(String scheduleName)
    {
        File f = new File(schedulePath+scheduleName+".xml");
        f.delete();
    }
    
    public void writeScheduleFile(String newScheduleName, ArrayList<ScheduledFunction> functions) throws TransformerConfigurationException, TransformerException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // root elements
            Document doc = docBuilder.newDocument();
            Element scheduleRootElement = doc.createElement("schedule");
            doc.appendChild(scheduleRootElement); 
            
            // schedule name attribute
            Attr scheduleName = doc.createAttribute("scheduleName");
            scheduleName.setValue(newScheduleName);
            scheduleRootElement.setAttributeNode(scheduleName);
            
            // function elements
            for(int i = 0; i<functions.size(); i++)
            {
                Element function = doc.createElement("function");
                scheduleRootElement.appendChild(function);
                
                // function name attribute
		Attr name = doc.createAttribute("name");
		String fName = functions.get(i).getName();
                name.setValue(fName);
		function.setAttributeNode(name);
                
                // function type attribute
		Attr type = doc.createAttribute("type");
		String fType = functions.get(i).getTypeString();
                type.setValue(fType);
		function.setAttributeNode(type);
                
                // startHr attribute
		Attr startHr = doc.createAttribute("startHr");
		String stringHr = String.valueOf(functions.get(i).getStartHr());
                startHr.setValue(stringHr);
		function.setAttributeNode(startHr);
                
                // startMn attribute
		Attr startMn = doc.createAttribute("startMn");
		String stringMn = String.valueOf(functions.get(i).getStartMn());
                startMn.setValue(stringMn);
		function.setAttributeNode(startMn);
                
                // duration attribute
		Attr duration = doc.createAttribute("duration");
		String stringDur = String.valueOf(functions.get(i).getDurationSec());
                duration.setValue(stringDur);
		function.setAttributeNode(duration);
            }
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(schedulePath+newScheduleName+".xml"));
            transformer.transform(domSource, streamResult);         
        }
        catch (ParserConfigurationException ex) {
            Logger.getLogger(ClimateReadWriter.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("failure writing file");
        }
    }

    private ScheduledFunction getFunction(Node node, Document doc) {
        
        Element element = (Element) node; //this is the season node
        NodeList nodes = node.getChildNodes(); //list of elements/attributes in the function node
        
        //name, type, startHr, startMn, duration        
        String name = element.getAttribute("name");
        String type = element.getAttribute("type");
        String startHr = element.getAttribute("startHr");
        String startMn = element.getAttribute("startMn");
        String duration = element.getAttribute("duration");

        int intStartHr = Integer.parseInt(startHr);
        int intStartMn = Integer.parseInt(startMn);
        int intDuration = Integer.parseInt(duration);
        
        ScheduledFunction function = new ScheduledFunction(name, type, intStartHr, intStartMn, intDuration);
        
        return function;
    }
   
    
}
