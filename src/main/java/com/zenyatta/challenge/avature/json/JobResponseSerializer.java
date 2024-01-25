package com.zenyatta.challenge.avature.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.zenyatta.challenge.avature.dto.JobResponse;
import com.zenyatta.challenge.avature.model.Skill;
import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.RecordComponent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class JobResponseSerializer extends StdSerializer<JobResponse> {
    @Serial
    private static final long serialVersionUID = 1234567891;

    protected JobResponseSerializer() {
        super(JobResponse.class);
    }

    @Override
    public void serialize(final JobResponse response, final JsonGenerator generator,
            final SerializerProvider serializers) throws IOException {
        generator.writeStartObject();

        for (final RecordComponent component : JobResponse.class.getRecordComponents()) {
            final String field = component.getAccessor().getName();

            // In a real job posting board we wouldn't want to omit the ID from our endpoint
            // however, to keep in line with the response from the extra source, we will.
            if ("skills".equals(field) || "id".equals(field)) {
                continue;
            }

            writeField(field, generator, response);
        }

        if (!response.skills().isEmpty()) {
            generator.writeArrayFieldStart("skills");

            for (final Skill skill : response.skills()) {
                generator.writeString(skill.toString());
            }

            generator.writeEndArray();
        }

        generator.writeEndObject();
    }

    private void writeField(
            final String field,
            final JsonGenerator generator,
            final JobResponse response) throws IOException {
        try {
            final Object jobResponse = response.getClass().getDeclaredMethod(field).invoke(response);
            if (jobResponse != null) {
                generator.writeStringField(field, jobResponse.toString());
            } else {
                generator.writeNullField(field);
            }
        } catch (final Exception e) {
            throw (IOException) new IOException(e.getMessage());
        }
    }
}
