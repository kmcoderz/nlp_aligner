/*
 * BuildDocument.java: Build the XML Document with UTF-16 encoding
 */
package nlp_aligner;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author KALYAN
 */
public class BuildDocument {
    private String outFile;
    private Transformer tFormer;
    private final String ENCODING = "UTF-16";
    private final String INDENT = "yes";
    Document document;
    BuildDocument(Document document,String inFile){
       
            outFile = "corpora_"+inFile.substring(0, inFile.lastIndexOf('.'))+".xml";
            this.document = document;
            //Write DOM to file
            TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            tFormer = tFactory.newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(BuildDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void buildXML(){
        document.normalize();
        Source source = new DOMSource(document);
        Result dest = new StreamResult(new File(outFile)); //Write Back
        tFormer.setOutputProperty(OutputKeys.INDENT, INDENT); //for indentation
        tFormer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
        try {    
            tFormer.transform(source, dest);
        } catch (TransformerException ex) {
            Logger.getLogger(BuildDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getOutFileName(){
        return outFile;
    }
}
