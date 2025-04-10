package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.management.api.model.SensorDetailOutput;
import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
  private final SensorMonitoringClient sensorMonitoringClient;

  @GetMapping
  public Page<SensorOutput> search(@PageableDefault Pageable pageable) {
    return sensorRepository.findAll(pageable).map(SensorOutput::from);
  }

  @GetMapping("/{sensorId}")
  @ResponseStatus(HttpStatus.OK)
  public SensorOutput getOne(@PathVariable("sensorId") TSID sensorId) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return SensorOutput.from(sensor);
  }

  @GetMapping("/{sensorId}/detail")
  @ResponseStatus(HttpStatus.OK)
  public SensorDetailOutput getOneWithDetail(@PathVariable("sensorId") TSID sensorId) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    SensorMonitoringOutput monitoringOutput = sensorMonitoringClient.getSensorDetail(sensorId);
    SensorOutput sensorOutput = SensorOutput.from(sensor);

    return new SensorDetailOutput(sensorOutput, monitoringOutput);
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

    return SensorOutput.from(sensorRepository.saveAndFlush(sensor));
  }

  @PutMapping("/{sensorId}")
  @ResponseStatus(HttpStatus.OK)
  public SensorOutput updateSensor(
      @PathVariable("sensorId") TSID sensorId, @RequestBody SensorInput input) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    BeanUtils.copyProperties(input, sensor);

    return SensorOutput.from(sensorRepository.saveAndFlush(sensor));
  }

  @PutMapping("/{sensorId}/enable")
  @ResponseStatus(HttpStatus.OK)
  public SensorOutput enableSensor(@PathVariable("sensorId") TSID sensorId) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    sensor.setEnabled(true);

    sensorMonitoringClient.enableMonitoring(sensorId);

    return SensorOutput.from(sensorRepository.saveAndFlush(sensor));
  }

  @DeleteMapping("/{sensorId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteSensor(@PathVariable("sensorId") TSID sensorId) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    sensorRepository.delete(sensor);

    sensorMonitoringClient.disableMonitoring(sensorId);
  }

  @DeleteMapping("/{sensorId}/enable")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disableSensor(@PathVariable("sensorId") TSID sensorId) {
    Sensor sensor =
        sensorRepository
            .findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    sensor.setEnabled(false);
    sensorRepository.saveAndFlush(sensor);

    sensorMonitoringClient.disableMonitoring(sensorId);
  }
}
