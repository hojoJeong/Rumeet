package com.d204.rumeet.pipeline.model.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.CompletableFuture;

public interface PipelineService {


    CompletableFuture<?> sendToTopic(String userAgentName, String colorName, String userName);
}

