package me.gustavwww.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.gustavwww.model.IUser;
import me.gustavwww.model.UserFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {

    // http://128.199.63.222
    private static final String BASE_URI = "http://localhost:3005";

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    public synchronized static IUser getUserLogin(String username, String password) throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        Map<String, Object> response = sendPostRequest("/v1/users/login", body);

        Map<String, Object> skinObj = (Map<String, Object>) response.get("skins");

        return UserFactory.CreateUser(response.get("id").toString(), response.get("username").toString(), (int) (double) response.get("coins"), (int) (double) response.get("weeklyAmount"), (int) (double) response.get("amount"), (ArrayList<String>) skinObj.get("owned"), (String) skinObj.get("equippedSkin"));
    }

    public synchronized static IUser getUserSignup(String username, String password) throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        Map<String, Object> response = sendPostRequest("/v1/users/create", body);

        Map<String, Object> skinObj = (Map<String, Object>) response.get("skins");

        return UserFactory.CreateUser(response.get("id").toString(), response.get("username").toString(), (int) (double) response.get("coins"), (int) (double) response.get("weeklyAmount"), (int) (double) response.get("amount"), (ArrayList<String>) skinObj.get("owned"), (String) skinObj.get("equippedSkin"));
    }

    public synchronized static Map<String, Object> sendGetRequest(String URI_EXT) throws HttpManagerException, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URI + URI_EXT))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> jsonObj = new Gson().fromJson(response.body(), new TypeToken<HashMap<String, Object>>(){}.getType());

        if (!(response.statusCode() == 200)) {
            String errorMsg = "Bad Request";

            if (jsonObj.get("error") != null) {
                errorMsg = jsonObj.get("error").toString();
            }

            throw new HttpManagerException(errorMsg, response.statusCode());
        }

        return jsonObj;
    }

    public synchronized static Map<String, Object> sendPostRequest(String URI_EXT, Map<String, Object> body) throws HttpManagerException, IOException, InterruptedException {

        String jsonString = new Gson().toJson(body);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .uri(URI.create(BASE_URI + URI_EXT))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 401) {
            throw new HttpManagerException("Unauthorized", response.statusCode());
        }

        Map<String, Object> jsonObj = new Gson().fromJson(response.body(), new TypeToken<HashMap<String, Object>>(){}.getType());

        if (!(response.statusCode() == 200)) {
            String errorMsg = "Bad Request";

            if (jsonObj.get("error") != null) {
                errorMsg = jsonObj.get("error").toString();
            }

            throw new HttpManagerException(errorMsg, response.statusCode());
        }

        return jsonObj;
    }

}
