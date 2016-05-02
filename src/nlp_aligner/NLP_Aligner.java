/*
 * NLP_Aligner.java: [Main Class] that takes input filename as input
 */
package nlp_aligner;

import org.w3c.dom.Document;

/**
 *
 * @author KALYAN
 */
public class NLP_Aligner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Input File Name
        String txtFile1 = "chuti_eng.txt";
        String txtFile2 = "chuti_bng.txt";
        String corpora1 = "corpora_chuti_eng.xml";
        String corpora2 = "corpora_chuti_bng.xml";
        String sysAligned = "chuti_datasheet_sys.txt";
        String handAligned = "chuti_datasheet.txt";
        
        
        GenerateXML xmlGen1,xmlGen2;
        Document xmlDoc1,xmlDoc2;
        BuildDocument buildXmlDoc1,buildXmlDoc2;
        ReadXML xmlRead1,xmlRead2;
        Aligner Align;
        PreAligner pAlign;
        GenerateAlignment alignText;
        Evaluation evaluate;
        
        xmlGen1 = new GenerateXML(txtFile1);
        xmlDoc1 = xmlGen1.getDocument();
        buildXmlDoc1 = new BuildDocument(xmlDoc1,txtFile1);
        buildXmlDoc1.buildXML();
        
        
        xmlGen2 = new GenerateXML(txtFile2);
        xmlDoc2 = xmlGen2.getDocument();
        buildXmlDoc2 = new BuildDocument(xmlDoc2,txtFile2);
        buildXmlDoc2.buildXML();
        
        
        xmlRead1 = new ReadXML(corpora1);
        xmlRead2 = new ReadXML(corpora2);
        pAlign = new PreAligner();
        Align = new Aligner(xmlRead1.getNodes(),xmlRead2.getNodes(),pAlign);
        Align.computeAlignment();
        
        alignText = new GenerateAlignment(sysAligned,Align.getAlignmentText());
        alignText.generateText();
        
        evaluate =  new Evaluation(handAligned,sysAligned);
        evaluate.evaluate();
    }
}
