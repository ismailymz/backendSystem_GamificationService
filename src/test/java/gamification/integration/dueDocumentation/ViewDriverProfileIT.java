package gamification.integration.dueDocumentation;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;




@QuarkusTest

public class ViewDriverProfileIT {
//public Response createDriver(@Valid CreateDriverRequestDTO request)
        @Test
        @TestTransaction

    void createProfileAndViewDriverProfileWithRequest() throws Exception{
        //createProfile postRequest
            String jsonString = """
                    {
                    "username": "ErdilWantsToQuit",
                    "password": "securePassword123"
                    }""";

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8081/api/drivers"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .build();


            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(postResponse.body());


            ObjectMapper mapper = new ObjectMapper();

            String body = postResponse.body();

            JsonNode root = mapper.readTree(body);

            String driverId = root.get("id").asText();
            System.out.println("Driver ID = " + driverId);
            //getProfile getRequest
            String username = "ErdilWantsToQuit";
            String password = "securePassword123";  // Basic Auth Encoding



            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8081/api/drivers/"+driverId+"/profile"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + encodedAuth)
                    .GET()
                    .build();

            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(getResponse.body());

        }
    }



