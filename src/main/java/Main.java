import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        // create thymeleaf variables
        HashMap<String, Object> variables = new HashMap();
        variables.put("name", "Lucas Aries");
        variables.put("date", LocalDateTime.now().toLocalDate().toString());

        String pathHtml = "template.html";
        String baseuri = "/";
        String namePdfGenerated = "result.pdf";

        try (
                ByteArrayOutputStream outputStream = (ByteArrayOutputStream) PdfGenerator.generatePdf(pathHtml, baseuri, variables);
                FileOutputStream fileOutputStream = new FileOutputStream(namePdfGenerated);
        ){
            fileOutputStream.write(outputStream.toByteArray());
        } catch (IOException e) {
            System.out.println("Error : " + e);
        }
    }

}