package wat.jeet.lab3.servlets;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class countries extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String countryName = request.getParameter("name");
        if (countryName == null) {
            countryName = "Poland";
        }

        String apiEndpointUrl = "https://restcountries.com/v3.1";
        String apiResource = "name/" + countryName;

        try {
            Client client = createJerseyClient();

            WebTarget webserviceClient = client.target(apiEndpointUrl);
            WebTarget webserviceRequest = webserviceClient.path(apiResource);

            String jsonResponse =
                    webserviceRequest
                            .request(MediaType.APPLICATION_JSON)
                            .get(String.class);

            // Parse JSON › Java List<Map>
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> countries =
                    objectMapper.readValue(jsonResponse, new TypeReference<>() {});

            // Send parsed data to JSP
            request.setAttribute("countryData", countries);
            request.getRequestDispatcher("index.jsp").forward(request, response);

        } catch (Exception e) {
            // API error › show message in JSP
            request.setAttribute("errorMessage", "Country not found.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    // SSL-bypass Jersey client
    private Client createJerseyClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new java.security.SecureRandom());

            return ClientBuilder.newBuilder()
                    .sslContext(sslContext)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error creating Jersey client with SSL bypass", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
