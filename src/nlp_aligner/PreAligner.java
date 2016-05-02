/*
 * PreAligner.java: Find P-Match(), Variance, Scaling Factor from the datasheet files
 */
package nlp_aligner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author KALYAN
 */
public class PreAligner {
    private double variance,sclF;
    private String filesEng[] = {"chuti_eng.txt","lalu_eng.txt","kabuli_eng.txt","khudi_eng.txt","joyporajoy_eng.txt","thakur_eng.txt"};
    private String filesBng[] = {"chuti_bng.txt","lalu_bng.txt","kabuli_bng.txt","khudi_bng.txt","joyporajoy_bng.txt","thakur_bng.txt"};
    private String dataSheets[] = {"lalu_datasheet.txt","chuti_datasheet.txt","thakur_datasheet.txt","joyporajoy_datasheet.txt"};
    private int txtCount1,txtCount2;
    private int one_one,one_two,two_one,two_two,zero_one,one_zero;
    private double pone_one,pone_two,ptwo_one,ptwo_two,pzero_one,pone_zero;
    private int engWords[],bngWords[];
    private String alignPat = "c_word"; //change as needed
    PreAligner(){
        this.txtCount1 = 0;
        this.txtCount2 = 0;
        this.one_one = 0;
        this.one_two = 0;
        this.two_one = 0;
        this.two_two = 0;
        this.zero_one = 0;
        this.one_zero = 0;
        this.engWords = new int[200];
        this.bngWords = new int[200];
        setScaleFactor();
        readDataSheetFile();
        calcPMatch();
        getParaWordMatrix();
    }
    public double getVariance(){
        return variance;
    }
    public double getScaleFactor(){
        return sclF;
    }
    private void calcPMatch(){
        double total = one_one+one_two+two_one+two_two+zero_one+one_zero;
        this.pone_one = (double)one_one/total;
        this.pone_two = (double)one_two/total;
        this.ptwo_one = (double)two_one/total;
        this.ptwo_two = (double)two_two/total;
        this.pzero_one = (double)zero_one/total;
        this.pone_zero = (double)one_zero/total;
        System.out.println("1-1:"+this.pone_one);
        System.out.println("1-2:"+this.pone_two);
        System.out.println("2-1:"+this.ptwo_one);
        System.out.println("0-1:"+this.pzero_one);
        System.out.println("1-0:"+this.pone_zero);
        System.out.println("2-2:"+this.ptwo_two);
    }
    public double getPMatch(char ch){
        double pMatch = 0;
        switch (ch)
        {
            case 'a':
                pMatch = this.pone_one;
                break;
            case 'b':
                pMatch = this.pone_two;
                break;
            case 'c':
                pMatch = this.ptwo_one;
                break;
            case 'd':
                pMatch = this.ptwo_two;
                break;
            case 'e':
                pMatch = this.pzero_one;
                break;
            case 'f':
                pMatch = this.pone_zero;
                break;
        }
        return pMatch;
    }
    private void readDataSheetFile(){
        ReadText rdDSText;
        GenerateMatrix sheet[] = new GenerateMatrix[dataSheets.length];
        for(int i=0;i<dataSheets.length;i++)
        {
            rdDSText = new ReadText(dataSheets[i]);
            sheet[i] = new GenerateMatrix(rdDSText.getSource());
            this.one_one += sheet[i].one_one;
            this.one_two += sheet[i].one_two;
            this.two_one += sheet[i].two_one;
            this.two_two += sheet[i].two_two;
            this.zero_one += sheet[i].zero_one;
            this.one_zero += sheet[i].one_zero;
        }   
    }
    private void getParaWordMatrix(){
        GenerateXML genXML1,genXML2;
        Document xmlDoc1,xmlDoc2;
        BuildDocument buildXmlDoc1,buildXmlDoc2;
        ReadXML xmlRead1,xmlRead2;
        int engW[],bngW[];
        int k=0;
        for(int i=0;i<filesEng.length;i++)
        {
            genXML1 = new GenerateXML(filesEng[i]);
            xmlDoc1 = genXML1.getDocument();
            buildXmlDoc1 = new BuildDocument(xmlDoc1,filesEng[i]);
            buildXmlDoc1.buildXML();
            xmlRead1 = new ReadXML(buildXmlDoc1.getOutFileName());
            engW=getWordsFromXML(xmlRead1.getNodes()); //Change
            
            genXML2 = new GenerateXML(filesBng[i]);
            xmlDoc2 = genXML2.getDocument();
            buildXmlDoc2 = new BuildDocument(xmlDoc2,filesBng[i]);
            buildXmlDoc2.buildXML();
            xmlRead2 = new ReadXML(buildXmlDoc2.getOutFileName());
            bngW=getWordsFromXML(xmlRead2.getNodes()); //change
            for(int j=0;j<Math.max(engW.length,bngW.length);j++)
            {
                try{engWords[k] = engW[j];}catch(Exception e){}
                try{bngWords[k] = bngW[j];}catch(Exception e){}
                k++;
            }
        }
        setVariance(k);
        //for(int i=0;i<k;i++)
          //  System.out.println(bngWords[i]);
        
    }
    private void setVariance(int n){
        double y[] = new double[n];
        double sx = 0,sy = 0,sxy = 0,sxx = 0;
        double a[][] = new double[2][2];
        double b[] = new double[2];
        for(int i=0;i<n;i++)
        {
            if(this.engWords[i] == 0 || this.bngWords[i] == 0)
            {
                y[i] = 0;
            }
            else
            {
                y[i] = Math.pow((this.engWords[i]-this.bngWords[i]), 2);
                sx+=this.bngWords[i];
                sy+=y[i];
                sxy+=(bngWords[i]*y[i]);
                sxx+=(bngWords[i]*bngWords[i]);
            }
            
        }
        a[0][0]=n;
        a[0][1]=sx;
        a[1][0]=sx;
        a[1][1]=sxx;
        b[0]=sy;
        b[1]=sxy;
        this.variance = gaussElm(a,b);
    }
    private int[] getWordsFromXML(NodeList nodes){
        int arr[] = null;
        int len;
        len = nodes.getLength();
        arr = new int[len];
        for(int i=0;i<len;i++)
        {
            Element element = (Element) nodes.item(i);
            Attr w_len = element.getAttributeNode(this.alignPat);
            arr[i] = Integer.parseInt(w_len.getNodeValue());
        }
        return arr;
    }
    private int[] getCharsFromXML(NodeList nodes){
        int arr[] = null;
        int len;
        len = nodes.getLength();
        arr = new int[len];
        for(int i=0;i<len;i++)
        {
            Element element = (Element) nodes.item(i);
            Attr w_len = element.getAttributeNode("c_char");
            arr[i] = Integer.parseInt(w_len.getNodeValue());
        }
        return arr;
    }
    private void readTxtFiles(){
        ReadText rdText;
        for(int i=0;i<filesEng.length;i++)
        {
            rdText = new ReadText(filesEng[i]);
            txtCount1 += getWordCount(rdText.getSource()); // change here
            rdText = new ReadText(filesBng[i]);
            txtCount2 += getWordCount(rdText.getSource()); //change here
        }
    }
    private void setScaleFactor(){
        readTxtFiles();
        sclF = (double)txtCount1/txtCount2;  //changed
    }
    private int getWordCount(BufferedReader br){
        String s = "";
        int wordCount = 0;
        try {
            while((s = br.readLine()) != null){
                StringTokenizer st = new StringTokenizer(s);
                wordCount += st.countTokens();
            }
        } catch (IOException ex) {
            Logger.getLogger(PreAligner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wordCount;
        
    }
    private int getCharCount(BufferedReader br){
        String s = "";
        int charCount = 0;
        try {
            while((s = br.readLine()) != null){
                charCount += s.length();
            }
        } catch (IOException ex) {
            Logger.getLogger(PreAligner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return charCount;
        
    }

    private double gaussElm(double[][] a, double[] b) {
        double m1;
        double root[] = new double[2];
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<2;j++)
            {
                if(i!=j)
                {
                    m1=a[j][i]/a[i][i];
                    for(int k=0;k<2;k++)
                    {
                        a[j][k] = a[j][k]-a[i][k]*m1;
                    }
                    b[j] = b[j]-b[i]*m1;
                }
            }
        }
        //Back Substit
        root[1] = b[1]/a[1][1];
        double s;
        for(int i=2-2;i>=0;--i)
        {
            s=0;
            for(int j=i+1;j<2;++j)
            {
                s+=a[i][j]*root[j];
            }
            root[i]=(b[i]-s)/a[i][i];
        }
        System.out.println("Variance:"+root[1]);
        return root[1];
    }
}
