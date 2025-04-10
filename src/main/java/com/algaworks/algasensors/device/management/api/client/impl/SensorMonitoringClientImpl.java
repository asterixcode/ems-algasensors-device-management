package com.algaworks.algasensors.device.management.api.client.impl;

import com.algaworks.algasensors.device.management.api.client.RestClientFactory;
import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;
import org.springframework.web.client.RestClient;

/*
This class is not used anymore, but it is kept here for reference.
It has been replaced by @HttpExchange,
@GetExchange, @PutExchange, and @DeleteExchange annotations in the SensorMonitoringClient interface,
which automatically generate the necessary REST client methods.
The RestClientConfig creates the RestClient bean, which is used by the SensorMonitoringClient interface.
 */
// @Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

  private final RestClient restClient;

  public SensorMonitoringClientImpl(RestClientFactory factory) {
    this.restClient = factory.temperatureMonitoringRestClient();
  }

  @Override
  public void enableMonitoring(TSID sensorId) {
    restClient
        .put()
        .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
        .retrieve()
        .toBodilessEntity();
  }

  @Override
  public void disableMonitoring(TSID sensorId) {
    restClient
        .delete()
        .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
        .retrieve()
        .toBodilessEntity();
  }

  @Override
  public SensorMonitoringOutput getSensorDetail(TSID sensorId) {
    return restClient
        .get()
        .uri("/api/sensors/{sensorId}/monitoring", sensorId)
        .retrieve()
        .body(SensorMonitoringOutput.class);
  }
}
