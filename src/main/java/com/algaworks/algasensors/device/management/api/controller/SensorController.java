package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

  private final SensorRepository sensorRepository;

  @GetMapping
  public Page<SensorOutput> search(@PageableDefault Pageable pageable) {
    return sensorRepository.findAll(pageable).map(SensorOutput::from);
  }

  @GetMapping("/{sensor-id}")
  @ResponseStatus(HttpStatus.OK)
  public SensorOutput getOne(@PathVariable("sensor-id") TSID sensorId) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return SensorOutput.from(sensor);
  }

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
