package jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtBuilder {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Please provide thee arguments. Arguments are client Id, client secret and token endpoint");
            return;
        }
        String clientId = args[0];
        String clientSecret = args[1];
        String tokenEndpoint = args[2];
        String jwt = buildJwtString(clientId, clientSecret, tokenEndpoint);
        System.out.println("JWT string: " + jwt);
        String tokenJson = obtainToken(tokenEndpoint, clientId, jwt);
        System.out.println("Token response Json : " + tokenJson);
    }

    private static String buildJwtString(String clientId, String clientSecret, String tokenEndpoint) {
        SecretKey sharedSecret = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        return Jwts.builder()
                .setAudience(tokenEndpoint)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .setIssuer(clientId)
                .setSubject(clientId)
                .setId(UUID.randomUUID().toString())
                .signWith(sharedSecret)
                .compact();
    }

    private static String obtainToken(String tokenEndpoint, String clientId, String jwt) throws Exception {
        HttpPost post = new HttpPost(tokenEndpoint);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("client_id", clientId));
        urlParameters.add(new BasicNameValuePair("client_assertion", jwt));
        urlParameters.add(new BasicNameValuePair("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer")); // This should be hard-coded. This value is fixed
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            System.err.println(ex);
            throw ex;
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        }
    }
}
