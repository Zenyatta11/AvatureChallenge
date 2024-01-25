package com.zenyatta.challenge.avature.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenyatta.challenge.avature.dto.extrasource.ExtraSourceResponseDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class JobberwockyExtraRestClient extends RestClient {

    @Value("${extra-source.url}")
    private String baseUrl;

    @Autowired
    @Qualifier("jobberwockyExtraSourceClient")
    private WebClient webClient;

    @Override
    protected WebClient getWebClient() {
        return webClient;
    }

    /**
     * This is a very unsafe way of handling the situation, however, given
     * that this is a challenge and not production-ready code, it is enough
     * because the response values are known and not subject to change.
     * </p>
     * It would've helped greatly if the response from the extra were in
     * key => value format and not nested arrays. Maybe it's part of the
     * challenge? I don't know, anyway, not a problem!
     */
    public List<ExtraSourceResponseDTO> getExternalJobs(final String query) {
        final String jsonResponse = get(baseUrl + "/jobs", String.class);
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final List<List<Object>> nestedList = objectMapper.readValue(jsonResponse,
                    new TypeReference<List<List<Object>>>() {
                    });

            return nestedList.stream()
                    .filter(entry -> entry.size() >= 4)
                    .map(entry -> new ExtraSourceResponseDTO(
                            (String) entry.get(0),
                            (Integer) entry.get(1),
                            (String) entry.get(2),
                            Arrays.asList(convertToStringArray(entry.get(3)))))
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String[] convertToStringArray(final Object skills) {
        final ObjectMapper objectMapper = new ObjectMapper();

        if (skills instanceof List) {
            final List<?> skillsList = (List<?>) skills;
            return objectMapper.convertValue(skillsList, String[].class);
        } else {
            return new String[0];
        }
    }
}
