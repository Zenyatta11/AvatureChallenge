package com.zenyatta.challenge.avature.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.zenyatta.challenge.avature.dto.CreateJobRequest;
import com.zenyatta.challenge.avature.model.Location;
import com.zenyatta.challenge.avature.model.Skill;
import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class CreateJobRequestDeserializer extends StdDeserializer<CreateJobRequest> {
    @Serial
    private static final long serialVersionUID = 1234567890;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    protected CreateJobRequestDeserializer() {
        super(CreateJobRequest.class);
    }

    @Override
    public CreateJobRequest deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);

        final CreateJobRequest.CreateJobRequestBuilder builder = CreateJobRequest.builder()
                .title(parseString("title", node))
                .location(parseLocation("location", node))
                .salary(parseInt("salary", node))
                .skills(parseSkills(node));

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

    private Set<Skill> parseSkills(final JsonNode node)
            throws JsonProcessingException, JsonMappingException {
        Set<Skill> skills = null;
        if (node.get("skills") != null && !node.get("skills").isEmpty()) {
            final ArrayNode spec = (ArrayNode) node.get("skills");
            final Skill[] parsedSkills = MAPPER.readValue(spec.toString(), Skill[].class);
            skills = new HashSet<>(Arrays.asList(parsedSkills));
        }

        return skills;
    }
}
