/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import java.io.IOException;
import javax.servlet.FilterChain;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@Import({ MethodSecurityConfiguration.class })
@EnableResourceServer
public class ResourceServerSecurityConfiguration implements ResourceServerConfigurer {

  private TokenExtractor tokenExtractor = new BearerTokenExtractor();

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId("example");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.addFilterAfter(new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request,
          HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
        // We don't want to allow access to a resource with no token so clear
        // the security context in case it is actually an OAuth2Authentication
        if (tokenExtractor.extract(request) == null) {
          SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
      }
    }, AbstractPreAuthenticatedProcessingFilter.class);
    http.csrf().disable();

    http
        .anonymous().and()
        .authorizeRequests()
        .antMatchers("/**").permitAll();
  }

  @Bean
  public AccessTokenConverter accessTokenConverter() {
    return new DefaultAccessTokenConverter();
  }

  /**
   * RemoteTokenServices bean initializer.
   * 
   * @param checkTokenUrl url to check tokens against
   * @param clientId      client's id
   * @param clientSecret  client's secret
   * @return token services
   */
  @Bean
  @Autowired
  public RemoteTokenServices remoteTokenServices(@Value("${auth.server.url}") String checkTokenUrl,
      @Value("${auth.server.clientId}") String clientId,
      @Value("${auth.server.clientSecret}") String clientSecret) {
    final RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
    remoteTokenServices.setCheckTokenEndpointUrl(checkTokenUrl);
    remoteTokenServices.setClientId(clientId);
    remoteTokenServices.setClientSecret(clientSecret);
    remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
    return remoteTokenServices;
  }
}
