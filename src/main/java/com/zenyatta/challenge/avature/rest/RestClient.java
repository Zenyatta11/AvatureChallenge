package com.zenyatta.challenge.avature.rest;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public abstract class RestClient {
    protected abstract WebClient getWebClient();

    protected <TYPE_OUT_T> TYPE_OUT_T get(final String url, final Class<TYPE_OUT_T> outClass) {

        if (log.isInfoEnabled()) {
            MDC.put("uri", url);
            log.info("Invoking GET request");
        }

        return this.getWebClient().get()
                .uri(url)
                .retrieve()
                .bodyToMono((ParameterizedTypeReference<TYPE_OUT_T>) ParameterizedTypeReference.forType(outClass))
                .block();
    }
}
