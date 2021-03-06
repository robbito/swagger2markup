/*
 * Copyright 2017 Robert Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.swagger2markup.internal.resolver;

import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.MarkupLanguage;
import org.junit.Test;

import java.nio.file.Paths;

import static io.github.swagger2markup.helper.ContextUtils.createContext;
import static org.assertj.core.api.Assertions.assertThat;

public class DefinitionDocumentResolverFromDefinitionTest {

    @Test
    public void testDefault() {
        Swagger2MarkupConverter.SwaggerContext context = createContext();

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName")).isNull();
    }

    @Test
    public void testWithSeparatedDefinitions() {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withSeparatedDefinitions()
                .build();
        Swagger2MarkupConverter.SwaggerContext context = createContext(config);

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName")).isNull();
    }

    @Test
    public void testWithSeparatedDefinitionsAndInterDocumentCrossReferences() {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withSeparatedDefinitions()
                .withInterDocumentCrossReferences()
                .build();
        Swagger2MarkupConverter.SwaggerContext context = createContext(config);
        context.setOutputPath(Paths.get("/tmp"));

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName"))
                .isEqualTo("DefinitionName.adoc");
    }

    @Test
    public void testWithInterDocumentCrossReferences() {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withInterDocumentCrossReferences()
                .build();
        Swagger2MarkupConverter.SwaggerContext context = createContext(config);
        context.setOutputPath(Paths.get("/tmp"));

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName"))
                .isEqualTo("definitions.adoc");
    }

    @Test
    public void testWithInterDocumentCrossReferencesAndNoOutputPath() {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withInterDocumentCrossReferences()
                .build();
        Swagger2MarkupConverter.SwaggerContext context = createContext(config);

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName"))
                .isNull();
    }

    @Test
    public void testWithInterDocumentCrossReferencesAndPrefix() {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withInterDocumentCrossReferences("prefix_")
                .build();
        Swagger2MarkupConverter.SwaggerContext context = createContext(config);
        context.setOutputPath(Paths.get("/tmp"));

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName"))
                .isEqualTo("prefix_definitions.adoc");
    }

    @Test
    public void testWithInterDocumentCrossReferencesAndMarkdown() {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withInterDocumentCrossReferences()
                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
                .build();
        Swagger2MarkupConverter.SwaggerContext context = createContext(config);
        context.setOutputPath(Paths.get("/tmp"));

        assertThat(new DefinitionDocumentResolverFromDefinition(context).apply("DefinitionName"))
                .isEqualTo("definitions.md");
    }
}
