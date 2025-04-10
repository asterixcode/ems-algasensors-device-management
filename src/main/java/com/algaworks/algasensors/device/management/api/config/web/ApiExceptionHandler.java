package com.algaworks.algasensors.device.management.api.config.web;

import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClientBadGatewayException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({
    SocketTimeoutException.class,
    ConnectException.class,
    ClosedChannelException.class
  })
  public ProblemDetail handle(IOException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);

    problemDetail.setTitle("Gateway Timeout");
    problemDetail.setDetail(e.getMessage());
    problemDetail.setType(URI.create("/errors/gateway-timeout"));

    return problemDetail;
  }

  @ExceptionHandler(SensorMonitoringClientBadGatewayException.class)
  public ProblemDetail handle(SensorMonitoringClientBadGatewayException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);

    problemDetail.setTitle("Bad Gateway");
    problemDetail.setDetail(e.getMessage());
    problemDetail.setType(URI.create("/errors/bad-gateway"));

    return problemDetail;
  }
}
