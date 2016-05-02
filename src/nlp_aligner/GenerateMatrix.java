/*
 * GenerateMatrix.java: Read the Text File and Generate matrix. Calculate correct Align
 */
package nlp_aligner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KALYAN
 */
public class GenerateMatrix {
    private BufferedReader dataSheet;
    public int one_one,one_two,two_one,two_two,zero_one,one_zero;
    private int correctM,retrievedA,correctA;;
    GenerateMatrix(BufferedReader br){
        this.dataSheet = br;
        this.one_one = 0;
        this.one_two = 0;
        this.two_one = 0;
        this.two_two = 0;
        this.zero_one = 0;
        this.one_zero = 0;
        getEachPara(br);
    }

    GenerateMatrix(BufferedReader hAln, BufferedReader sAln) {
        this.correctM = 0;
        this.retrievedA = 0;
        this.correctA = 0;
        getMatch(hAln,sAln);
    }
    private void getEachPara(BufferedReader dataSheet){
        try {
            String s = "";
            int lines = 0;
            StringTokenizer st;
            int matrix[][] = null;
            while((s=dataSheet.readLine()) != null)
            {
                if(s.isEmpty())
                {
                    calculateAlign(matrix,lines);
                    lines = 0;
                }
                else
                {
                    st = new StringTokenizer(s,"\t");
                    if(lines==0)
                        matrix = new int[1][2];
                    matrix[lines][0] = Integer.parseInt(st.nextToken());
                    matrix[lines][1] = Integer.parseInt(st.nextToken());
                    lines++;
                    int new_mat[][] = new int[lines][2];
                    //Copy Matrix
                    for(int k=0;k<lines;k++)
                        for(int j=0;j<2;j++)
                            new_mat[k][j] = matrix[k][j];
                    //new assigned size
                    matrix = new int[lines+1][2];
                    //Copy Back Matrix
                    for(int k=0;k<lines;k++)
                        for(int j=0;j<2;j++)
                             matrix[k][j] = new_mat[k][j];
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void calculateAlign(int matrix[][],int row){
        for(int i=0;i<row;i++)
        {
            if(matrix[i][0] <= 0 || matrix[i][1] <= 0)
            {
                //0-1,1-0,2-2
                if(matrix[i][0] == 0)
                    this.zero_one++;
                else
                if(matrix[i][1] == 0)
                    this.one_zero++;
                else
                {
                    this.two_two++;
                    i++;
                }
            }
            else
            {
                //1-1,1-2,2-1
                if(matrix[i][0] == matrix[i+1][0])
                {
                   this.one_two++;
                   i++;
                }
                else
                if(matrix[i][1] == matrix[i+1][1])
                {
                    this.two_one++;
                    i++;
                }
                else
                    this.one_one++;
            }
        }
                 
    }
    public int getCorrectMatch(){
        return this.correctM;
    }
    public int getCorrectAlign(){
        return this.correctA;
    }
    public int getRetrievedAlign(){
        return this.retrievedA;
    }
    private void getMatch(BufferedReader hAln,BufferedReader sAln) {
        try {
            String s = "";
            int lines = 0,para = 1;
            StringTokenizer st;
            int matrix1[][] = null,matrix2[][] = null;
            matrix1 = new int[1][3];
            matrix2 = new int[1][3];
            while((s=hAln.readLine()) != null )
            {
                if(s.isEmpty())
                {
                    para++;
                }
                else
                {
                    st = new StringTokenizer(s,"\t");
                    matrix1[lines][0] = para;
                    matrix1[lines][1] = Integer.parseInt(st.nextToken());
                    matrix1[lines][2] = Integer.parseInt(st.nextToken());
                    lines++;
                    
                    int new_mat[][] = new int[lines][3];
                    //Copy Matrix
                    for(int k=0;k<lines;k++)
                        for(int j=0;j<3;j++)
                            new_mat[k][j] = matrix1[k][j];
                    //new assigned size
                    matrix1 = new int[lines+1][3];
                    //Copy Back Matrix
                    for(int k=0;k<lines;k++)
                        for(int j=0;j<3;j++)
                             matrix1[k][j] = new_mat[k][j];
                             
                }
            }
            //Second Matrix
            para = 1;
            lines = 0;
            while((s=sAln.readLine()) != null )
            {
                if(s.isEmpty())
                {
                    para++;
                }
                else
                {
                    st = new StringTokenizer(s,"\t");
                    matrix2[lines][0] = para;
                    matrix2[lines][1] = Integer.parseInt(st.nextToken());
                    matrix2[lines][2] = Integer.parseInt(st.nextToken());
                    lines++;
                    int new_mat[][] = new int[lines][3];
                    //Copy Matrix
                    for(int k=0;k<lines;k++)
                        for(int j=0;j<3;j++)
                            new_mat[k][j] = matrix2[k][j];
                    //new assigned size
                    matrix2 = new int[lines+1][3];
                    //Copy Back Matrix
                    for(int k=0;k<lines;k++)
                        for(int j=0;j<3;j++)
                             matrix2[k][j] = new_mat[k][j];
                             
                }
            }
            
            for(int i=0;i<matrix2.length-1;i++)
            {
                for(int j=0;j<matrix1.length-1;j++)
                {
                    if(matrix2[i][0]==matrix1[j][0] && matrix2[i][1]==matrix1[j][1] && matrix2[i][2]==matrix1[j][2])
                    {
                        correctM++;
                        break;
                    }
                    else
                    {
                    }
                }
            }
            this.retrievedA = matrix2.length - 1;
            this.correctA = matrix1.length - 1;
            
        } catch (IOException ex) {
            Logger.getLogger(GenerateMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
