package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Sensor createSensor(@RequestBody SensorInput input) {
    return Sensor.builder()
        .id(IdGenerator.generateTSID())
        .name(input.name())
        .ip(input.ip())
        .location(input.location())
        .protocol(input.protocol())
        .model(input.model())
        .enabled(false)
        .build();
  }
}
