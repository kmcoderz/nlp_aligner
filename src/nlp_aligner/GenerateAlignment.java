/*
 * GenerateAlignment.java: Generate Alignment Text File
 */
package nlp_aligner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KALYAN
 */
public class GenerateAlignment {
    private String fileName;
    private StringBuilder outText;
    GenerateAlignment(String fileName, StringBuilder outText){
        this.fileName = fileName;
        this.outText = outText;
    }
    public void generateText(){
        try {
            FileOutputStream fout = new FileOutputStream(this.fileName);
            OutputStreamWriter fos = new OutputStreamWriter(fout,"UTF-16");
            BufferedWriter bw = new BufferedWriter(fos);
            bw.write(outText.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(GenerateAlignment.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    }
            
}
