package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class EmailUtil {


    private static final String RESEND_API_KEY = "re_QbiCAtpo_2Em9ZQne4sd5Prg2sLcsivbF";
    private static final String RESEND_URL      = "https://api.resend.com/emails";
    private static final String FROM_EMAIL      = "SiAlumni <noreply@pradatelyu.online>";

 
    public static boolean sendEmail(String to, String subject, String body) {
        try {
         
            URL url = new URL(RESEND_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

         
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + RESEND_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

        
            String safeBody  = body.replace("\\", "\\\\").replace("\"", "\\\"")
                                   .replace("\n", "\\n").replace("\r", "\\r");
            String safeSubject = subject.replace("\"", "\\\"");

            String json = "{\"from\":\"" + FROM_EMAIL + "\","
                        + "\"to\":[\"" + to + "\"],"
                        + "\"subject\":\"" + safeSubject + "\","
                        + "\"html\":\"" + safeBody + "\"}";

   
            byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBytes);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            BufferedReader reader;

            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Resend API response (" + responseCode + "): " + response.toString());

        
            return responseCode >= 200 && responseCode < 300;

        } catch (IOException e) {
     
            System.out.println("Error kirim email: " + e.getMessage());
            return false;
        }
    }
}
