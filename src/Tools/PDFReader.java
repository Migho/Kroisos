package Tools;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFReader{

    private PDFTextStripper pdfStripper;
    private int pageNumber=0;
    private PDDocument pdDoc;

    public PDFReader(String filePath) {
        PDFParser parser;
        COSDocument cosDoc ;

        File file;

        file = new File(filePath);
        try {
            parser = new PDFParser(new RandomAccessFile(file,"r")); // update for PDFBox V 2.0
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
            pageNumber=1;
        } catch (IOException e) {
            System.out.println("File not found: " + filePath);
            System.out.println(e);
            pdDoc=null;
        }
    }

    public boolean hasNextPage() {
        if(pdDoc == null) return false;
        if(pageNumber > pdDoc.getNumberOfPages()) return false;
        return true;
    }

    public String getNextPage() {
        if(!hasNextPage()) return null;
        pdfStripper.setStartPage(pageNumber);
        pdfStripper.setEndPage(pageNumber);
        pageNumber++;
        try {
            return pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println(e);
            return null;
        }
    }

    public void closePDF() {
        try {
            pdDoc.close();
        } catch (IOException e) {
            System.out.println("Error while closing PDF: " + e);
        }
    }
}
