/*
 * ReadText : Read all the Text Documents
 */
package nlp_aligner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author KALYAN
 */
public class ReadText {
    private String txtFile;
    //public BufferedReader Read
    public ReadText(String txtFile){
        this.txtFile = txtFile;
    }
    public BufferedReader getSource() {
        FileInputStream fin = null;
        BufferedReader br = null;
        try {
            fin = new FileInputStream(this.txtFile);
            br = new BufferedReader(new InputStreamReader(fin,"UTF-16"));
            //fin.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Unsupported File Encoding");
        } finally {
            return br;
                //Check for fin close
        }
    }
}
