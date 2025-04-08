package com.algaworks.algasensors.device.management.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SensorId implements Serializable {

  private TSID value;

  public SensorId(TSID value) {
    Objects.requireNonNull(value);
    this.value = value;
  }

  public SensorId(Long value) {
    Objects.requireNonNull(value);
    this.value = TSID.from(value);
  }

  public SensorId(String value) {
    Objects.requireNonNull(value);
    this.value = TSID.from(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
