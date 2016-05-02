/*
 * Aligner.java : The Distance Measure Implementation , paragraph Align, sentences align, calculate all the pre requirements
 * for the computation of Alignment
 */
package nlp_aligner;

import java.util.ArrayList;
import java.util.Collections;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author KALYAN
 */
public class Aligner {
    private NodeList nodes1,nodes2;
    private int arr1[],arr2[];
    private PreAligner pAlign;
    private StringBuilder aligned;
    private String alignPat = "c_word"; //change as needed
    private double min_dist;
    private Node min_node;
    Aligner(NodeList nodes1,NodeList nodes2,PreAligner pAlign){
        this.nodes1 = nodes1;
        this.nodes2 = nodes2;
        this.pAlign = pAlign;
        aligned = new StringBuilder();
    }
    public void computeAlignment(){
        getParaLength();
        //System.out.println("Length"+arr2.length);
        alignPara();
    }
    private int[][] align(int a[],int b[]){
        double dist[][] = new double[a.length+1][b.length+1];
        char seq[][] = new char[a.length][b.length];
        int outMat[][] = null;
        int correctMat[][] = null;
        double d[] = new double[6];
        for(int i=1;i<=a.length;i++)
        {
            for(int j=1;j<=b.length;j++)
            {
                d[0] = dist[i][j-1] + distanceM(0,b[j-1],0,0); //0-1
                d[1] = dist[i-1][j] + distanceM(a[i-1],0,0,0); //1-0
                d[2] = dist[i-1][j-1] + distanceM(a[i-1],b[j-1],0,0); //1-1
                if(j>1)
                    d[3] = dist[i-1][j-2] + distanceM(a[i-1],b[j-1],0,b[j-2]); //1-2
                else
                    d[3] = 99999;
                if(i>1)
                    d[4] = dist[i-2][j-1] + distanceM(a[i-1],b[j-1],a[i-2],0); //2-1
                else
                    d[4] = 99999;
                if(i>1 && j>1)
                    d[5] = dist[i-2][j-2] + distanceM(a[i-1],b[j-1],a[i-2],b[j-2]); //2-2
                else
                    d[5] = 99999;
                //Find min
                double min = d[0];
                for(int k=1;k<6;k++)
                {
                    if(d[k]<min)
                        min = d[k];
                }
                dist[i][j] = min;
                if(min == d[0])
                {
                    seq[i-1][j-1]='b';
                }
                else
                if(min == d[1])
                {
                    seq[i-1][j-1]='c';
                }
                else
                if(min == d[2])
                {
                    seq[i-1][j-1]='a';
                }
                else
                if(min == d[3])
                {
                    seq[i-1][j-1]='d';
                }
                else
                if(min == d[4])
                {
                    seq[i-1][j-1]='e';
                }
                else
                if(min == d[5])
                {
                    seq[i-1][j-1]='f';
                }
            }
        }
        
        int i=a.length-1;
        int j=b.length-1;
        int k=0;
        outMat = new int[35][2];
        while(i>=0 && j>=0)
        {
            if(seq[i][j] == 'a')
            {
                //1-1
                outMat[k][0] = i+1;
                outMat[k][1] = j+1;
                k++;
                i--;
                j--;
            }
            else
            if(seq[i][j] == 'b')
            {
                //0-1
                outMat[k][0] = 0;
                outMat[k][1] = j+1;
                k++;
                j--;
            }
            else
            if(seq[i][j] == 'c')
            {
                //1-0
                outMat[k][0] = i+1;
                outMat[k][1] = 0;
                k++;
                i--;
            }
            else
            if(seq[i][j] == 'd')
            {
                //1-2
                outMat[k][0] = i+1;
                outMat[k][1] = j+1;
                outMat[k+1][0] = i+1;
                outMat[k+1][1] = j;
                k+=2;
                i--;
                j-=2;
            }
            else
            if(seq[i][j] == 'e')
            {
                //2-1
                outMat[k][0] = i+1;
                outMat[k][1] = j+1;
                outMat[k+1][0] = i;
                outMat[k+1][1] = j+1;
                k+=2;
                i-=2;
                j--;
            }
            else
            if(seq[i][j] == 'f')
            {
                //2-2
                outMat[k][0] = i+1;
                outMat[k+1][0] = i;
                outMat[k][1] = j+1;
                outMat[k+1][1] = j;
                k+=2;
                i-=2;
                j-=2;
            }   
        }
        if(i>=0)
        {
            for(int p=0;p<=i;p++)
            {
                outMat[k][0] = i+1-p;
                outMat[k][1] = 1;
                k++;
            }
        }
        if(j>=0)
        {
            for(int p=0;p<=j;p++)
            {
                outMat[k][0] = 1;
                outMat[k][1] = j+1-p;
                k++;
            }
        }
        correctMat = new int[k][2];
        for(int x=0;x<k;x++)
        {
            correctMat[x][0]=outMat[x][0];
            correctMat[x][1]=outMat[x][1];
        }
        return correctMat;
    }
    private int[][] align_graph(int a[],int b[]){
        int correctMat[][] = null;
        Node root = new Node(0);
        this.min_dist = 99999;
        this.min_node = null;
        creategraph(root,0,0,a,b);
        correctMat = new int[this.min_node.corp1.size()][2];
        int k=0;
        for(int i=this.min_node.corp1.size()-1;i>=0;i--)
        {
            correctMat[i][0]=(int) this.min_node.corp1.get(k);
            correctMat[i][1]=(int) this.min_node.corp2.get(k);
            k++;
        }
        int mb = 1024*1024;
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Free Memory:"+ runtime.freeMemory() / mb+"\tTotal: "+runtime.totalMemory() / mb);
        root = null;
        return correctMat;
    }

    private void creategraph(Node root,int s1,int s2,int a[],int b[]){
        if(s1<a.length && s2<b.length){
        root.one_one = new Node(root.dist+distanceM(a[s1],b[s2],0,0));
        root.one_one.corp1.addAll(root.corp1);
        root.one_one.corp2.addAll(root.corp2);
        root.one_one.corp1.add(s1+1);
        root.one_one.corp2.add(s2+1);
        if(s1+1==a.length && s2+1==b.length && this.min_dist > root.one_one.dist)
        {
            this.min_dist = root.one_one.dist;
            this.min_node = root.one_one;
        }
        creategraph(root.one_one,s1+1,s2+1,a,b);
        }
        
        if(s1<a.length && s2-1<b.length){
        root.one_zero = new Node(root.dist+distanceM(a[s1],0,0,0));
        root.one_zero.corp1.addAll(root.corp1);
        root.one_zero.corp2.addAll(root.corp2);
        root.one_zero.corp1.add(s1+1);
        root.one_zero.corp2.add(0);
        if(s1+1==a.length && s2==b.length && this.min_dist > root.one_zero.dist)
        {
            this.min_dist = root.one_zero.dist;
            this.min_node = root.one_zero;
        }
        creategraph(root.one_zero,s1+1,s2,a,b);
        }
        
        if(s1-1<a.length && s2<b.length){
        root.zero_one = new Node(root.dist+distanceM(0,b[s2],0,0));
        root.zero_one.corp1.addAll(root.corp1);
        root.zero_one.corp2.addAll(root.corp2);
        root.zero_one.corp1.add(0);
        root.zero_one.corp2.add(s2+1);
        if(s1==a.length && s2+1==b.length && this.min_dist > root.zero_one.dist)
        {
            this.min_dist = root.zero_one.dist;
            this.min_node = root.zero_one;
        }
        creategraph(root.zero_one,s1,s2+1,a,b);
        }
        
        if(s1<a.length && s2+1<b.length){
        root.one_two = new Node(root.dist+distanceM(a[s1],b[s2],0,b[s2+1]));
        root.one_two.corp1.addAll(root.corp1);
        root.one_two.corp2.addAll(root.corp2);
        root.one_two.corp1.add(s1+1);
        root.one_two.corp1.add(s1+1);
        root.one_two.corp2.add(s2+1);
        root.one_two.corp2.add(s2+2);
        if(s1+1==a.length && s2+2==b.length && this.min_dist > root.one_two.dist)
        {
            this.min_dist = root.one_two.dist;
            this.min_node = root.one_two;
        }
        creategraph(root.one_two,s1+1,s2+2,a,b);
        }
        
        if(s1+1<a.length && s2<b.length){
        root.two_one = new Node(root.dist+distanceM(a[s1],b[s2],a[s1+1],0));
        root.two_one.corp1.addAll(root.corp1);
        root.two_one.corp2.addAll(root.corp2);
        root.two_one.corp1.add(s1+1);
        root.two_one.corp1.add(s1+2);
        root.two_one.corp2.add(s2+1);
        root.two_one.corp2.add(s2+1);
        if(s1+2==a.length && s2+1==b.length && this.min_dist > root.two_one.dist)
        {
            this.min_dist = root.two_one.dist;
            this.min_node = root.two_one;
        }
        creategraph(root.two_one,s1+2,s2+1,a,b);
        }
        if(s1+1<a.length && s2+1<b.length){
        root.two_two = new Node(root.dist+distanceM(a[s1],b[s2],a[s1+1],b[s2+1]));
        root.two_two.corp1.addAll(root.corp1);
        root.two_two.corp2.addAll(root.corp2);
        root.two_two.corp1.add(-(s1+1));
        root.two_two.corp1.add(-(s1+2));
        root.two_two.corp2.add(-(s2+1));
        root.two_two.corp2.add(-(s2+2));
        if(s1+2==a.length && s2+2==b.length && this.min_dist > root.two_two.dist)
        {
            this.min_dist = root.two_two.dist;
            this.min_node = root.two_two;
        }
        creategraph(root.two_two,s1+2,s2+2,a,b);
        }
    }
    class Node{
        public ArrayList corp1,corp2;
        public double dist;
        Node one_one,one_zero,zero_one,one_two,two_one,two_two;
        Node(double dist){
            this.dist = dist;
            this.corp1 = new ArrayList();
            this.corp2 = new ArrayList();
        }    
    }
    
    private void alignPara(){
        int outMat[][] = align(this.arr1,this.arr2);
        int k=1;
        for(int i=arr1.length-1;i>=0;i--)
        {
            //System.out.print("Paragraph: "+outMat[i][0]+"\t"+outMat[i][1]+"\n");
            //alignSentences(outMat[i][0],outMat[i][1]);
            alignSentences(k,k);
            k++;
            this.aligned.append("\r\n");
            
        }
    }
    private void alignSentences(int s,int t){
        int a1[],a2[];
        a1 = getSentencesLength(nodes1,s);
        a2 = getSentencesLength(nodes2,t);
        int outMat[][] = align(a1,a2);
        
        for(int i=outMat.length-1;i>=0;i--)
        {
            //System.out.print(outMat[i][0]+"\t"+outMat[i][1]+"\n");
            this.aligned.append(outMat[i][0]).append("\t").append(outMat[i][1]).append("\r\n");
       }
    }
    public StringBuilder getAlignmentText(){
        return this.aligned;
    }
    
    private int[] getSentencesLength(NodeList nodes,int i){
        Element element = (Element)nodes.item(i-1);
        NodeList children = element.getChildNodes();
        int arr[] = new int[children.getLength()/2];
        for(int k=1;k<children.getLength();k+=2)
        {
            Element sentence = (Element) children.item(k);
            Attr c_len = sentence.getAttributeNode(alignPat);
            arr[k/2] = Integer.parseInt(c_len.getNodeValue());
        }
        return arr;
    }
    private void getParaLength(){
        int len1,len2;
        len1 = nodes1.getLength();
        arr1 = new int[len1];
        for(int i=0;i<len1;i++)
        {
            Element element = (Element) nodes1.item(i);
            Attr c_len = element.getAttributeNode(alignPat);
            arr1[i] = Integer.parseInt(c_len.getNodeValue());
        }
        len2 = nodes2.getLength();
        arr2 = new int[len2];
        for(int i=0;i<len2;i++)
        {
            Element element = (Element) nodes2.item(i);
            Attr c_len = element.getAttributeNode(alignPat);
            arr2[i] = Integer.parseInt(c_len.getNodeValue());
        }
    }
    private double pDelta(double x)
    {
        double t,pdel;
        t = 1/(1 + .2316419 * x);
        pdel = 1 - .3989423*Math.exp(-x*x/2)*t*(0.319381530+t*(-.356563782+t*(1.781477939+t*(-1.821255978+t*1.330274429))));
        return pdel;
    }
    private double pDeltaMatch(int len1,int len2)
    {
        double d,mean,pdelm,c,var;
        var = pAlign.getVariance();
        c = pAlign.getScaleFactor();
        if(len1 == 0 && len2 == 0)
            return 0;
        mean = (len1+len2/c)/2;
        d = (len1 - c*len2)/Math.sqrt(mean*var); //changed
        if(d<0)
            d=-d;
        pdelm = 2*(1-pDelta(d));
        return pdelm;
    }   
    private double distanceM(int x1,int y1,int x2,int y2){
        double dist = 0;
        if(x2 == 0 && y2 == 0)
        {
            if(x1 == 0)//0-1
                dist = -Math.log(pDeltaMatch(0,y1)*pAlign.getPMatch('e'));
            else
            if(y1 == 0)//1-0
                dist = -Math.log(pDeltaMatch(x1,0)*pAlign.getPMatch('f'));
            else //1-1
                dist = -Math.log(pDeltaMatch(x1,y1)*pAlign.getPMatch('a'));
        }
        else
        if(x2 == 0)//1-2
            dist = -Math.log(pDeltaMatch(x1,y1+y2)*pAlign.getPMatch('b'));
        else
        if(y2 == 0)//2-1
            dist = -Math.log(pDeltaMatch(x1+x2,y1)*pAlign.getPMatch('c'));
        else //2-2
            dist = -Math.log(pDeltaMatch(x1+x2,y1+y2)*pAlign.getPMatch('d'));
        return dist;
        
    }
}
