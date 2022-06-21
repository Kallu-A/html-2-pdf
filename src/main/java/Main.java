import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.dom.Document;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        // create thymeleaf variables
        HashMap<String, Object> variables = new HashMap();
        variables.put("name", "Lucas Aries");
        variables.put("date", LocalDateTime.now().toLocalDate().toString());

        String orderHtml = parseHtmlThymeLeafToString("template.html", variables);
        String baseuri = "/";
        String namePdfGenerated = "result.pdf";

        try (
                ByteArrayOutputStream outputStream = (ByteArrayOutputStream) htmlToPdf(orderHtml, baseuri);
                FileOutputStream fileOutputStream = new FileOutputStream(namePdfGenerated);
        ){
            fileOutputStream.write(outputStream.toByteArray());
        } catch (IOException e) {
            System.out.println("Error : " + e);
        }


    }


    /**
     * Parse the .html file with thymeleaf to a string with thymeleaf parse
     * @param path to the .html file
     * @param variables variables used by the template (key name on the template, value used in the template)
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