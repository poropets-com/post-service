package com.propets.postservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.propets.postservice.api.RegexConstants.*;

public class JwtTokenValidatorFilter extends GenericFilterBean {
    @Value("${connection.timeout}")
    int timeout;//TODO add usage for timeout exceptions
    private static final String HEADER ="Authorization" ;
    private static final String URL_PREF="http://localhost:8075/account/**/v1/";
    private static final String URL_SUF="/validation";
    private final RestTemplate restTemplate=new RestTemplate();
    private static final String USER = "ROLE_USER";
    private static final String MODERATOR = "ROLE_MODERATOR";
    private final Map<String, Map<String, List<String>>>authorities=new HashMap<>();

    public JwtTokenValidatorFilter(){
        init();
    }
    private void init(){
        Map<String,List<String>>httpPost=new HashMap<>();
        httpPost.put(ANY+PREFIX+OWNER+ANY, Arrays.asList(USER));

        Map<String,List<String>>httpPut=new HashMap<>();
        httpPut.put(ANY+PREFIX+COMPLAIN+ANY,Arrays.asList(USER));
        httpPut.put(ANY+PREFIX+HIDE+ANY,Arrays.asList(MODERATOR));
        httpPut.put(ANY+PREFIX+"/"+ANY+"/"+ANY,Arrays.asList(USER));


        Map<String,List<String>>httpDelete=new HashMap<>();
        httpDelete.put(ANY+PREFIX+"/"+ANY+"/"+ANY,Arrays.asList(USER));

        Map<String,List<String>>httpGet=new HashMap<>();
        httpGet.put(ANY+PREFIX+VIEW+ANY,Arrays.asList(USER));
        httpGet.put(ANY+PREFIX+ANY,Arrays.asList(USER));

        authorities.put("POST",httpPost);
        authorities.put("PUT",httpPut);
        authorities.put("DELETE",httpDelete);
        authorities.put("GET",httpGet);
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpHeaders header=getHeader((HttpServletRequest)servletRequest);
        String token=resolveToken((HttpServletRequest)servletRequest);
        HttpEntity<MultiValueMap>httpEntity=new HttpEntity(header);
        ResponseEntity<TokenDao> response = restTemplate.exchange(URL_PREF + token + URL_SUF, 
                HttpMethod.GET,
                httpEntity, 
                TokenDao.class);
        String responseToken = response.getHeaders().getFirst(HEADER);
        ((HttpServletResponse)servletResponse).addHeader(HEADER,responseToken);
        
        authorizeRequest((HttpServletRequest)servletRequest,response.getBody());
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private boolean authorizeRequest(HttpServletRequest servletRequest, TokenDao body) {
        String httpMethod= servletRequest.getMethod();
        StringBuffer requestURL =servletRequest.getRequestURL();
        Map<String, List<String>> endPoints = authorities.get(httpMethod);
        
        String key = endPoints.keySet().stream().filter(k -> {
            Pattern p = Pattern.compile(k);
            Matcher m = p.matcher(requestURL);
            return m.matches();
        }).findFirst().orElse(null);
        if (key==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"url address not found");
        }
        List<String> roles = endPoints.get(key);
        for (String role : roles) {
            if (body.getRoles().contains(role)){
                return true;
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized request");
    }

//    private ClientHttpRequestFactory getClientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
//                = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setConnectTimeout(timeout);
//        return clientHttpRequestFactory;
//    }

    private String resolveToken(HttpServletRequest request){
       return request.getHeader(HEADER);
    }

    private HttpHeaders getHeader(HttpServletRequest servletRequest) {
        HttpHeaders header=new HttpHeaders();
        header.add(HEADER,servletRequest.getHeader(HEADER));
        return header;
    }
}
