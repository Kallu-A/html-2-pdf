import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class PdfGenerator {

    /**
     * Generate a PDF file from a HTML string
     * @param pathHtml path to the .html file
     * @param baseUri path for annex resources like css js (must be a URI like https://xxx/)
     * @param variables path for annex resources like css js (must be a URI like https://xxx/)
     * @return outputstream of the pdf file
     * @throws IOException
     */
    public static OutputStream generatePdf(String pathHtml, String baseUri, HashMap<String, Object> variables) throws IOException {
        String inputHTML = PdfGenerator.parseHtmlThymeLeafToString(pathHtml, variables);
        return htmlToPdf(inputHTML, baseUri);
    }
    /**
     * Parse the .html file with thymeleaf to a string with thymeleaf parse
     * @param path to the .html file
     * @param variables path for annex resources like css js (must be a URI like https://xxx/)
     * @return string of the html file parsed
     */
    private static String parseHtmlThymeLeafToString(String path, HashMap<String, Object> variables) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/");

        Context context = new Context();
        variables.forEach(context::setVariable);


        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        return templateEngine.process(path, context);
    }

    /** Parse a String of HTML to a Document
     * @param baseuri path for annex resources like css js (must be a URI like https://xxx/)
     * */
    private static Document html5ParseDocument(String inputHTML, String baseuri) {
        org.jsoup.nodes.Document doc;
        doc = Jsoup.parse(inputHTML, baseuri);
        return new W3CDom().fromJsoup(doc);
    }

    private static OutputStream htmlToPdf(String inputHTML, String baseuri) throws IOException {

        Document doc = html5ParseDocument(inputHTML, baseuri);
        OutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.toStream(os);

        builder.withW3cDocument(doc, baseuri);

        builder.run();

        return os;
    }

}
