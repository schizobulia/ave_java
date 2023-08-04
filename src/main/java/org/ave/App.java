package org.ave;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException {
        System.out.println( "Hello Ave!" );
        Options options = new Options();
        options.addOption("t", true, "pdf | word");
        options.addOption("data", true, "data");
        options.addOption("output", true, "output");
        PosixParser posixParser = new PosixParser();

        CommandLine parser = posixParser.parse(options, args);
        String type = parser.getOptionValue("t");
        if (type.equals("pdf")) {
            JSONArray ps = new JSONArray(parser.getOptionValue("data"));
            String[] pdfs = new String[ps.length()];
            for (int i = 0; i < ps.length(); i++) {
                pdfs[i] =  ps.get(i).toString();
            }
            mergerPdf(pdfs, parser.getOptionValue("output"));
        }
    }

    static void mergerPdf( String[] files , String out ) {
        Document document = null;
        PdfCopy copy = null;
        PdfReader reader = null;
        try {
            document = new Document(new PdfReader(files[0]).getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(out));
            document.open();
            for (int i = 0; i < files.length; i++) {
                reader = new PdfReader(files[i]);
                int numberOfPages = reader.getNumberOfPages();
                for (int j = 1; j <= numberOfPages; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
        } catch (IOException | DocumentException e) {
            System.out.println(e.getMessage());
        } finally {
            if (document != null)
                document.close();
            if (reader != null)
                reader.close();
            if (copy != null)
                copy.close();
        }
    }
}

