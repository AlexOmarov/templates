package ru.somarov.templates.java.mail.config;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import ru.somarov.fabricadereva.backend.entities.User;
import ru.somarov.fabricadereva.backend.repositories.UserRepo;
import ru.somarov.fabricadereva.backend.security.OidUserServiceImpl;
import ru.somarov.fabricadereva.backend.security.UserDetailServiceImpl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class AppConfiguration extends WebSecurityConfigurerAdapter
{


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${basepath}")
    private String myPath;


    class VKUserItem{
        @SerializedName("can_access_closed") private boolean canAccessClosed;
        @SerializedName("last_name") private String lastName;
        @SerializedName("id") private int id;
        @SerializedName("first_name") private String firstName;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @SerializedName("email") private String email;
        @SerializedName("is_closed") private boolean isClosed;
        public void setCanAccessClosed(boolean canAccessClosed){
            this.canAccessClosed = canAccessClosed;
        }
        public boolean isCanAccessClosed(){
            return canAccessClosed;
        }
        public void setLastName(String lastName){
            this.lastName = lastName;
        }
        protected String getLastName(){
            return lastName;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return id;
        }
        public void setFirstName(String firstName){
            this.firstName = firstName;
        }
        protected String getFirstName(){
            return firstName;
        }
        public void setIsClosed(boolean isClosed){
            this.isClosed = isClosed;
        }
        protected boolean isIsClosed(){
            return isClosed;
        }
    }

    class VKUsers {
        @SerializedName("response") private List<VKUserItem> users;
        public void setUsers(List<VKUserItem> users){
            this.users = users;
        }
        protected List<VKUserItem> getUsers(){
            return users;
        }
    }

    @Bean
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new DefaultOAuth2UserService() {
            private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";

            private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

            private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

            private final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
                    new ParameterizedTypeReference<Map<String, Object>>() {};

            private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();

            private RestOperations restOperations;


            private Gson gson = new Gson();
            private final MediaType FORM
                    = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
            private OkHttpClient client = new OkHttpClient();

            private VKUsers post (String url, String token) {
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(FORM, String.format("access_token=%s&v=5.92", token)))
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    return gson.fromJson( response.body().string(), VKUsers.class);
                } catch (IOException e) {
                   throw new RuntimeException(e);
                }
            }

            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                System.out.println("Constructor for DefUsOauth2");
                if (userRequest.getClientRegistration().getClientName().equals("vk")) {
                    String url = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
                    String token = userRequest.getAccessToken().getTokenValue();
                    VKUserItem userItem = post(url, token).getUsers().get(0);

                    String userNameAttributeName = userItem.getFirstName() + " " +userItem.getLastName();
                    Map<String, Object> userAttributes = new HashMap<>();
                    userAttributes.put("name", userNameAttributeName);
                    userAttributes.put("isClosed", userItem.isIsClosed());
                    userAttributes.put("authsystem", userRequest.getClientRegistration().getClientName());
                    userAttributes.put("id", userItem.getId());
                    userAttributes.put("email", userItem.getEmail());
                    Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(userAttributes));
                    return new DefaultOAuth2User(authorities, userAttributes, "name");
                }
                else{
                    Assert.notNull(userRequest, "userRequest cannot be null");

                    if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
                        OAuth2Error oauth2Error = new OAuth2Error(
                                MISSING_USER_INFO_URI_ERROR_CODE,
                                "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " +
                                        userRequest.getClientRegistration().getRegistrationId(),
                                null
                        );
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                    }
                    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                            .getUserInfoEndpoint().getUserNameAttributeName();
                    if (!StringUtils.hasText(userNameAttributeName)) {
                        OAuth2Error oauth2Error = new OAuth2Error(
                                MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
                                "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " +
                                        userRequest.getClientRegistration().getRegistrationId(),
                                null
                        );
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                    }

                    RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);

                    ResponseEntity<Map<String, Object>> response;
                    try {
                        response = this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
                    } catch (OAuth2AuthorizationException ex) {
                        OAuth2Error oauth2Error = ex.getError();
                        StringBuilder errorDetails = new StringBuilder();
                        errorDetails.append("Error details: [");
                        errorDetails.append("UserInfo Uri: ").append(
                                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
                        errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
                        if (oauth2Error.getDescription() != null) {
                            errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
                        }
                        errorDetails.append("]");
                        oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                                "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), null);
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
                    } catch (RestClientException ex) {
                        OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                                "An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
                    }

                    Map<String, Object> userAttributes = response.getBody();
                    System.out.println("Добавляем приколы!!!");
                    userAttributes.put("authsystem",userRequest.getClientRegistration().getClientName());
                    Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(userAttributes));
                    return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
                }
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*.headers().frameOptions().sameOrigin()
                .and()*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/oauth2/**",
                        "/blob/**",
                        "/css/**",
                        "/images/**",
                        "/img/**",
                        "/rest/**",
                        "/profile/**",
                        "/favicon.ico",
                        "/project/**",
                        "/js/**","/stomp/**"
                ).permitAll()
                .antMatchers("/orders","/order","/add/**").authenticated()
                .antMatchers("/admin/**","/manage/**").hasAuthority("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                .and()
                .oauth2Login()
//                    .clientRegistrationRepository(this.clientRegistrationRepository())
//                    .authorizedClientService(this.authorizedClientService())
                    .loginPage("/login")
                .defaultSuccessUrl("/", true)
//                    .authorizationEndpoint().baseUri(this.authorizationRequestBaseUri()).authorizationRequestRepository(this.authorizationRequestRepository())
//                    .and()
//                    .redirectionEndpoint().baseUri(this.authorizationResponseBaseUri())
//                    .and()
                    .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
                    .and()
                    .userInfoEndpoint()
                .userService(oauth2UserService())
                .oidcUserService(oidcUserService())

                ;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return new OidUserServiceImpl();
    }


    class VKTokenResponseConverter
            implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {

        private final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = Stream.of(
                OAuth2ParameterNames.ACCESS_TOKEN,
                OAuth2ParameterNames.TOKEN_TYPE,
                OAuth2ParameterNames.EXPIRES_IN,
                OAuth2ParameterNames.REFRESH_TOKEN,
                OAuth2ParameterNames.SCOPE).collect(Collectors.toSet());

        @Override
        public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {

            if (tokenResponseParameters.get(OAuth2ParameterNames.TOKEN_TYPE) == null) {
                String accessToken = tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);
                long expiresIn = Long.valueOf(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN));
                System.out.println(accessToken);

                OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;
                return OAuth2AccessTokenResponse.withToken(accessToken)
                        .tokenType(accessTokenType)
                        .expiresIn(expiresIn)
                        .build();
            } else {

                String accessToken = tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);

                OAuth2AccessToken.TokenType accessTokenType = null;
                if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(
                        tokenResponseParameters.get(OAuth2ParameterNames.TOKEN_TYPE))) {
                    accessTokenType = OAuth2AccessToken.TokenType.BEARER;
                }

                long expiresIn = 0;
                if (tokenResponseParameters.containsKey(OAuth2ParameterNames.EXPIRES_IN)) {
                    try {
                        expiresIn = Long.valueOf(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN));
                    } catch (NumberFormatException ex) {
                    }
                }

                Set<String> scopes = Collections.emptySet();
                if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
                    String scope = tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
                    scopes = Arrays.stream(StringUtils.delimitedListToStringArray(scope, " ")).collect(Collectors.toSet());
                }

                String refreshToken = tokenResponseParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);

                Map<String, Object> additionalParameters = new LinkedHashMap<>();
                tokenResponseParameters.entrySet().stream()
                        .filter(e -> !TOKEN_RESPONSE_PARAMETER_NAMES.contains(e.getKey()))
                        .forEach(e -> additionalParameters.put(e.getKey(), e.getValue()));

                return OAuth2AccessTokenResponse.withToken(accessToken)
                        .tokenType(accessTokenType)
                        .expiresIn(expiresIn)
                        .scopes(scopes)
                        .refreshToken(refreshToken)
                        .additionalParameters(additionalParameters)
                        .build();
            }
        }
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
//        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();

        tokenResponseHttpMessageConverter.setTokenResponseConverter(new VKTokenResponseConverter());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }






    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }


}
