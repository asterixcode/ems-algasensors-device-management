package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

  private final SensorRepository sensorRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SensorOutput createSensor(@RequestBody SensorInput input) {
    Sensor sensor =
        Sensor.builder()
            .id(new SensorId(IdGenerator.generateTSID()))
            .name(input.name())
            .ip(input.ip())
            .location(input.location())
            .protocol(input.protocol())
            .model(input.model())
            .enabled(false)
            .build();

    sensorRepository.saveAndFlush(sensor);

    return SensorOutput.from(sensor);
  }
}
