package com.cinema.infrastructure.logging.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogDocument, String> {}
