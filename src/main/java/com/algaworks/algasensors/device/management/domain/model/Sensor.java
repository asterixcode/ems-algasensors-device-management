package com.algaworks.algasensors.device.management.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Sensor {
  @Id
  @AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "BIGINT"))
  private SensorId id;

  private String name;
  private String ip;
  private String location;
  private String protocol;
  private String model;
  private Boolean enabled;
}
