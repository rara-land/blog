package rara.board.auth;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class Kakao {

    private final String accessRequestUrl = "https://kauth.kakao.com/oauth/token";
    private final String userInfoRequestUrl = "https://kapi.kakao.com/v2/user/me";

    @Value("${custom.auth.client-key}")
    private String clientKey;

    @Value("${custom.auth.redirect-uri}")
    private String redirectUri;

    public KakaoUserInfo getUserInfo(String code) {

        String accessToken = getAccessToken(code);
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);

        return userInfo;
    }

    private String getAccessToken(String code) {
        String accessToken = "";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("grant_type", "authorization_code");
        param.add("client_id", clientKey);
        param.add("redirect_uri", redirectUri);
        param.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);

        ResponseEntity<String> response = restTemplate.exchange(accessRequestUrl, HttpMethod.POST, request, String.class);

        try {
            String jsonBody = response.getBody();
            JSONParser jsonParser = new JSONParser();
            JSONObject body = (JSONObject) jsonParser.parse(jsonBody);

            accessToken = body.get("access_token").toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        log.info("access token = {}", accessToken);
        return accessToken;
    }


    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo();

        if (accessToken.isEmpty()) {
            return kakaoUserInfo;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(userInfoRequestUrl, HttpMethod.POST, request, String.class);

        try {
            String jsonBody = response.getBody();
            JSONParser jsonParser = new JSONParser();

            JSONObject body = (JSONObject) jsonParser.parse(jsonBody);
            JSONObject kakao_account = (JSONObject) jsonParser.parse(body.get("kakao_account").toString());
            JSONObject properties = (JSONObject) jsonParser.parse(body.get("properties").toString());

            Long id = (Long) body.get("id");
            String name = properties.get("nickname").toString();

            kakaoUserInfo.setId(id);
            kakaoUserInfo.setName(name);

            if (kakao_account.containsKey("email")) {
                String email = kakao_account.get("email").toString();
                kakaoUserInfo.setEmail(email);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return kakaoUserInfo;
    }

}
