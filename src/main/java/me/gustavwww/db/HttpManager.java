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
import java.util.HashMap;
import java.util.Map;

public class HttpManager {

    private static final String BASE_URI = "http://128.199.63.222";

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    public synchronized static IUser getUser(String id) throws HttpManagerException, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URI + "/v1/users/" + id))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, Object> jsonObj = new Gson().fromJson(response.body(), new TypeToken<HashMap<String, Object>>(){}.getType());

        if (response.statusCode() == 200) {
            return UserFactory.CreateUser(id, jsonObj.get("nickname").toString(), (int) (double) jsonObj.get("amount"));
        }

        String errorMsg = "Bad Request";
        if (jsonObj.get("error") != null) {
            errorMsg = jsonObj.get("error").toString();
        }

        throw new HttpManagerException(errorMsg, response.statusCode());
    }

    public synchronized static void postUserPaper(IUser user, int increment) throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("nickname", user.getNickname());
        data.put("increment", increment);

        String jsonString = new Gson().toJson(data);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .uri(URI.create(BASE_URI + "/v1/users/"))
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

    }

}
