/*
 * Evaluation.java: calculate precession, recall, f-measure and error for the given corpora match
 */
package nlp_aligner;

/**
 *
 * @author KALYAN
 */
public class Evaluation {
    private String handAligned,sysAligned;
    Evaluation(String handAligned,String sysAligned){
        this.handAligned = handAligned;
        this.sysAligned = sysAligned;
    }
    public void evaluate(){
        ReadText rdDSTextHA,rdDSTextSA;
        GenerateMatrix matrixGen;
        double precission,recall,fMeasure,error;
        rdDSTextHA = new ReadText(this.handAligned);
        rdDSTextSA = new ReadText(this.sysAligned);
        matrixGen = new GenerateMatrix(rdDSTextHA.getSource(),rdDSTextSA.getSource());
        precission = (double)matrixGen.getCorrectMatch()/matrixGen.getRetrievedAlign();
        recall = (double)matrixGen.getCorrectMatch()/matrixGen.getCorrectAlign();
        fMeasure = (2*precission*recall)/(precission+recall);
        error = (1-fMeasure)*100;
        System.out.printf("Error:%.2f",error);
        System.out.print("%\n");
    }
}
