package utils;

/**
 * Created by ${pipi} on 2017/3/7.
 */

public class AccessToken {
    private static AccessToken accessToken;
    private String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    private AccessToken() {
        this.accessToken = accessToken;

    }
    public static AccessToken getInstance() {
        if (accessToken == null) {
            accessToken = new AccessToken();
        }
        return accessToken;
    }
}
