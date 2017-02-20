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

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.gson.JsonObject;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.openlmis.example.service.WeatherService;


public class WeatherServiceTest {
  private static String TEST_HOST = "localhost";
  private static Integer TEST_PORT = 8080;
  private static String TEST_API_KEY = "506bc2e0c27da38e628dd249599ea779";

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(TEST_PORT);

  private WeatherService weatherService = new WeatherService(TEST_HOST, TEST_PORT, TEST_API_KEY);

  @Mock
  private WeatherService mockedWeatherService;

  @Before
  public void setUpMocks() {
    initMocks(this);
  }

  @Test
  public void testWeather() {
    JsonObject result = weatherService.getWeather("London");
    Assert.assertEquals(result.get("name").getAsString(), "London");
    Assert.assertEquals(result.get("main").getAsJsonObject().get("temp").getAsDouble(), 285.89, 0);

    result = weatherService.getWeather("Paris");
    Assert.assertEquals(result.get("name").getAsString(), "Paris");
    Assert.assertEquals(result.get("main").getAsJsonObject().get("temp").getAsDouble(), 286.16, 0);
  }

  @Test
  public void testMockedWeather() {
    String propertyName = "name";
    String propertyValue = "Gdynia";

    JsonObject jsonObjectReturnedByGetWeather = new JsonObject();
    jsonObjectReturnedByGetWeather.addProperty(propertyName, propertyValue);
    when(mockedWeatherService.getWeather(propertyName)).thenReturn(jsonObjectReturnedByGetWeather);

    Assert.assertEquals(jsonObjectReturnedByGetWeather,
        mockedWeatherService.getWeather(propertyName));
  }

}
