/*
 * Copyright 2006-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.validation.json.schema;

import com.consol.citrus.json.JsonSchemaRepository;
import com.consol.citrus.json.schema.SimpleJsonSchema;
import com.consol.citrus.validation.json.JsonMessageValidationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for filtering JsonSchemas based on a
 * JsonMessageValidationContext and the application context
 */
public class JsonSchemaFilter {

    /**
     * Filters the all schema repositories based on the configuration in the jsonMessageValidationContext
     * and returns a list of relevant schemas for the validation
     * @param schemaRepositories The repositories to be filtered
     * @param jsonMessageValidationContext The context for the json message validation
     * @param applicationContext The application context to extract beans from
     * @return A list of json schemas relevant for the validation based on the configuration
     */
    public List<SimpleJsonSchema> filter(List<JsonSchemaRepository> schemaRepositories,
                                         JsonMessageValidationContext jsonMessageValidationContext,
                                         ApplicationContext applicationContext) {
        List<SimpleJsonSchema> filteredJsonSchemas = new ArrayList<>();

        if(isSchemaRepositorySpecified(jsonMessageValidationContext)){
            filteredJsonSchemas = filterByRepositoryName(schemaRepositories, jsonMessageValidationContext);
        }else if(isSchemaSpecified(jsonMessageValidationContext)){
            filteredJsonSchemas = getSchemaFromContext(jsonMessageValidationContext, applicationContext);
        }

        return filteredJsonSchemas;
    }

    /**
     * Extracts the the schema specified in the jsonMessageValidationContext from the application context
     * @param jsonMessageValidationContext The message validation context containing the name of the schema to extract
     * @param applicationContext The application context to extract the schema from
     * @return A list containing the relevant schema or an empty list if no schema could have been found
     */
    private List<SimpleJsonSchema> getSchemaFromContext(JsonMessageValidationContext jsonMessageValidationContext,
                                                        ApplicationContext applicationContext) {
        SimpleJsonSchema simpleJsonSchema =
                applicationContext.getBean(jsonMessageValidationContext.getSchema(), SimpleJsonSchema.class);

        if(simpleJsonSchema != null){
            return Collections.singletonList(simpleJsonSchema);
        }else{
            return Collections.emptyList();
        }
    }

    /**
     * Filters the schema repositories by the name configured in the jsonMessageValidationContext
     * @param schemaRepositories The List of schema repositories to filter
     * @param jsonMessageValidationContext The validation context of the json message containing the repository name
     * @return The list of json schemas found in the matching repository or an empty list if no repository matched
     */
    private List<SimpleJsonSchema> filterByRepositoryName(List<JsonSchemaRepository> schemaRepositories,
                                                          JsonMessageValidationContext jsonMessageValidationContext) {
        for (JsonSchemaRepository jsonSchemaRepository : schemaRepositories){
            if(Objects.equals(jsonSchemaRepository.getName(), jsonMessageValidationContext.getSchemaRepository())){
                return jsonSchemaRepository.getSchemas();
            }
        }

        return Collections.emptyList();
    }

    private boolean isSchemaSpecified(JsonMessageValidationContext context) {
        return StringUtils.hasText(context.getSchema());
    }

    private boolean isSchemaRepositorySpecified(JsonMessageValidationContext context) {
        return StringUtils.hasText(context.getSchemaRepository());
    }
}
