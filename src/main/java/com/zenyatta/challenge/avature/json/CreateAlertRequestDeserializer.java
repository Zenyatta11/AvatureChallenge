package com.zenyatta.challenge.avature.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.zenyatta.challenge.avature.dto.CreateAlertRequest;
import com.zenyatta.challenge.avature.model.Location;
import java.io.IOException;
import java.io.Serial;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class CreateAlertRequestDeserializer extends StdDeserializer<CreateAlertRequest> {
    @Serial
    private static final long serialVersionUID = 987654321;

    protected CreateAlertRequestDeserializer() {
        super(CreateAlertRequest.class);
    }

    @Override
    public CreateAlertRequest deserialize(final JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);

        final CreateAlertRequest.CreateAlertRequestBuilder builder = CreateAlertRequest.builder()
                .email(parseString("email", node))
                .title(parseString("title", node))
                .location(parseLocation("location", node))
                .minSalary(parseInt("salary", node))
                .maxSalary(parseInt("salary", node));

        return builder.build();
    }

    private String parseString(final String field, final JsonNode node) {
        return null != node.get(field) ? node.get(field).asText() : null;
    }

    private Location parseLocation(final String field, final JsonNode node) {
        return null != node.get(field) ? new Location(node.get(field).asText()) : null;
    }

    private Integer parseInt(final String field, final JsonNode node) {
        return null != node.get(field) ? node.get(field).asInt(0) : null;
    }
}
