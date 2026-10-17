package com.cinema.infrastructure.logging.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "application_logs")
public class LogDocument {
    @Id
    private String id;
    private Instant timestamp;
    private String correlationId;
    private String userId;
    private String httpMethod;
    private String endpoint;
    private long durationMs;
    private boolean success;
    private String exception;
    private String operation;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getException() { return exception; }
    public void setException(String exception) { this.exception = exception; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}
