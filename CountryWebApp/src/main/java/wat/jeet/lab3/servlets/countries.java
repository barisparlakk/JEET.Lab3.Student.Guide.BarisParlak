package wat.jeet.lab3.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;

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
            // Use your SSL‑bypass Jersey client
            Client client = createJerseyClient();

            WebTarget webserviceClient = client.target(apiEndpointUrl);
            WebTarget webserviceRequest = webserviceClient.path(apiResource);

            String jsonResponse =
                    webserviceRequest
                            .request(MediaType.APPLICATION_JSON)
                            .get(String.class);

            if (response.getStatus() == 200) {
                PrintWriter out = response.getWriter();
                out.println(jsonResponse);
            } else {
                response.sendError(
                        response.getStatus(),
                        "Failed to fetch data from the API"
                );
            }

        } catch (Exception e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error fetching country data: " + e.getMessage()
            );
        }
    }

    // ---------------------------------------------------------
    // SSL‑bypass Jersey client (your provided method)
    // ---------------------------------------------------------
    private Client createJerseyClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            // No validation
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            // No validation
                        }

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
            throw new RuntimeException(
                    "Error creating Jersey client with SSL bypass", e
            );
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
