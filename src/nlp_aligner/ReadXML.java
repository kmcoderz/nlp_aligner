/*
 * ReadXML.java: read all the XML document
 */
package nlp_aligner;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author KALYAN
 */
public class ReadXML {
    private String xmlFile;
    private NodeList nodes;
    public ReadXML(String xmlFile){
        this.xmlFile = xmlFile;
    }
    private void extractSourceFile(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = factory.newDocumentBuilder().parse(xmlFile);
            nodes = document.getElementsByTagName("para");
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ReadXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ReadXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public NodeList getNodes(){
        extractSourceFile();
        return nodes;
    }
}
