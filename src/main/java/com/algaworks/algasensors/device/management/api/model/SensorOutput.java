package com.algaworks.algasensors.device.management.api.model;

import com.algaworks.algasensors.device.management.domain.model.Sensor;
import io.hypersistence.tsid.TSID;

public record SensorOutput(
    TSID id,
    String name,
    String ip,
    String location,
    String protocol,
    String model,
    boolean enabled) {
  public static SensorOutput from(Sensor sensor) {
    return new SensorOutput(
        sensor.getId().getValue(),
        sensor.getName(),
        sensor.getIp(),
        sensor.getLocation(),
        sensor.getProtocol(),
        sensor.getModel(),
        sensor.getEnabled());
  }
}
