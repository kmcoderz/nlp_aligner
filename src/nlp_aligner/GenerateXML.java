/*
 * GenerateXML : Generate XML Corpora
 */
package nlp_aligner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author KALYAN
 */
public class GenerateXML extends ReadText{

    private BufferedReader br;
    private Document document;
    GenerateXML(String txtFile) {
        super(txtFile);
        try {
            br = getSource();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder  builder = factory.newDocumentBuilder();        
            document = builder.newDocument();
            document = builder.newDocument();
        } catch (ParserConfigurationException ex) {
            System.out.println("Parser Error");
        }
       
    }
    /*
    private void generateDocument(){
        Element root,para,sent;
        Attr id, c_char,c_word; //attribute of para tag
        //create root
        root = document.createElement("corpora");
        document.appendChild(root);
        String s = ""; //store eachline
        int i=1,j=1,k,flag;
        try {
            while((s = br.readLine()) != null) //check for empty lines
            {
                if(!s.isEmpty()) //check for empty lines
                {
                    //para tag
                    para = document.createElement("para");
                    root.appendChild(para);
                    StringTokenizer st = new StringTokenizer(s);
                    id = document.createAttribute("id");
                    id.setValue(Integer.toString(j++));
                    c_char = document.createAttribute("c_char");
                    c_char.setValue(Integer.toString(s.length()));
                    c_word = document.createAttribute("c_word");
                    c_word.setValue(Integer.toString(st.countTokens()));
                    para.setAttributeNode(id);
                    para.setAttributeNode(c_char);
                    para.setAttributeNode(c_word);
                    //stores each sentences
                    StringBuilder sbuff = new StringBuilder();
                    k=1;//sentence id
                    flag=0;
                    for(i=0;i<s.length();i++)
                    {
                        //recongnize each sentences with . / ? / ! / ।
                        if(s.charAt(i) ==  '।' || s.charAt(i) == '.' || s.charAt(i) == '?' || s.charAt(i)== '!' || s.charAt(i)=='\n' || (flag==0 && i==s.length()-1))
                        {
                            sbuff.append(s.charAt(i));
                                
                                flag=1;
                                //If in Quote i.e. Quote after .
                                try{
                                    while(s.charAt(i+1) == ' ')
                                        i++;
                                    if(s.charAt(i+1) == '"' || s.charAt(i+1) == '\'')
                                    {
                                        sbuff.append(s.charAt(i+1));
                                        i++;
                                    }
                                }
                                catch(Exception e){}
                                while(sbuff.charAt(0)==' ')
                                    sbuff.deleteCharAt(0); //delete empty white spaces from the front of sentence
                                //sentence tag
                                StringTokenizer sts = new StringTokenizer(sbuff.toString());
                                id = document.createAttribute("id");
                                id.setValue(Integer.toString(k++));
                                c_char = document.createAttribute("c_char");
                                c_char.setValue(Integer.toString(sbuff.length()));
                                c_word = document.createAttribute("c_word");
                                c_word.setValue(Integer.toString(sts.countTokens()));
                                sent = document.createElement("sent");
                                sent.setAttributeNode(id);
                                sent.setAttributeNode(c_char);
                                sent.setAttributeNode(c_word);
                                sent.appendChild(document.createTextNode(sbuff.toString()));  
                                para.appendChild(sent);
                                sbuff.delete(0, sbuff.length()); //clear buffer
                            
                        }
                        else
                            sbuff.append(s.charAt(i));
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IO Error");
        }
        
    }*/
    private void generateDocument(){
        Element root,para,sent;
        Attr id, c_char,c_word; //attribute of para tag
        //create root
        root = document.createElement("corpora");
        document.appendChild(root);
        String s = ""; //store eachline
        int i=1,j=1,k,flag;
        int count=0;
        try {
            while((s = br.readLine()) != null) //check for empty lines
            {
                if(!s.isEmpty()) //check for empty lines
                {
                    //para tag
                    para = document.createElement("para");
                    root.appendChild(para);
                    StringTokenizer st = new StringTokenizer(s);
                    id = document.createAttribute("id");
                    id.setValue(Integer.toString(j++));
                    c_char = document.createAttribute("c_char");
                    c_char.setValue(Integer.toString(s.length()));
                    c_word = document.createAttribute("c_word");
                    c_word.setValue(Integer.toString(st.countTokens()));
                    para.setAttributeNode(id);
                    para.setAttributeNode(c_char);
                    para.setAttributeNode(c_word);
                    //stores each sentences
                    StringBuilder sbuff = new StringBuilder();
                    k=1;//sentence id
                    flag=0;
                    for(i=0;i<s.length();i++)
                    {
                        if(s.charAt(i) == '“')
                            count++;
                        if(s.charAt(i) == '”')
                             count=0;
                            //System.out.println("count:"+count);
                        
                        //recongnize each sentences with . / ? / ! / ।
                        if(((s.charAt(i) ==  '।' || s.charAt(i) == '.' || s.charAt(i) == '?' || s.charAt(i)== '!' || s.charAt(i)=='\n' || (flag==0 && i==s.length()-1)) && count == 0) || ((s.charAt(i) ==  '।' || s.charAt(i) == '.' || s.charAt(i) == '?' || s.charAt(i)== '!' || s.charAt(i)=='\n' || (flag==0 && i==s.length()-1)) && s.charAt(i+1) == '”'))
                        {
                            count=0;
                            sbuff.append(s.charAt(i));
                                
                                flag=1;
                                //If in Quote i.e. Quote after .
                                try{
                                    while(s.charAt(i+1) == ' ')
                                        i++;
                                    if(s.charAt(i+1) == '"' || s.charAt(i+1) == '\'' || s.charAt(i+1) == '”')
                                    {
                                        sbuff.append(s.charAt(i+1));
                                        i++;
                                    }
                                }
                                catch(Exception e){}
                                while(sbuff.charAt(0)==' ')
                                    sbuff.deleteCharAt(0); //delete empty white spaces from the front of sentence
                                //sentence tag
                                StringTokenizer sts = new StringTokenizer(sbuff.toString());
                                id = document.createAttribute("id");
                                id.setValue(Integer.toString(k++));
                                c_char = document.createAttribute("c_char");
                                c_char.setValue(Integer.toString(sbuff.length()));
                                c_word = document.createAttribute("c_word");
                                c_word.setValue(Integer.toString(sts.countTokens()));
                                sent = document.createElement("sent");
                                sent.setAttributeNode(id);
                                sent.setAttributeNode(c_char);
                                sent.setAttributeNode(c_word);
                                sent.appendChild(document.createTextNode(sbuff.toString()));  
                                para.appendChild(sent);
                                sbuff.delete(0, sbuff.length()); //clear buffer
                            
                        }
                        else
                            sbuff.append(s.charAt(i));
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IO Error");
        }
    }
    public Document getDocument(){
        generateDocument();
        return document;
    }
            
}
