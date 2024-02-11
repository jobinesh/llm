package com.jobinesh.ai.example;

import com.jobinesh.ai.example.agent.AppointmentTool;
import com.jobinesh.ai.example.agent.CustomerSupportAgent;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import dev.langchain4j.service.*;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_4;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "ai-assistant")

public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    Tokenizer tokenizer() {
        return new OpenAiTokenizer(GPT_4);
    }


    // In the real world, ingesting documents would often happen separately, on a CI server or similar
    @Bean
    CommandLineRunner docsToEmbeddings(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            Tokenizer tokenizer,
            ResourceLoader resourceLoader
    ) throws IOException {
        return args -> {
            Resource resource =
                    resourceLoader.getResource("classpath:terms-of-service.txt");
            var termsOfUse = loadDocument(resource.getFile().toPath(), new TextDocumentParser());

            DocumentSplitter documentSplitter = DocumentSplitters.recursive(200, 0,
                    tokenizer);

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(documentSplitter)
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();
            ingestor.ingest(termsOfUse);
        };
    }

    @Bean
    StreamingChatLanguageModel chatLanguageModel(@Value("${openai.api.key}") String apiKey) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4-turbo-preview")
                .build();
    }


    @Bean
    Retriever<TextSegment> retriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel
    ) {
        return EmbeddingStoreRetriever.from(
                embeddingStore,
                embeddingModel,
                1,
                0.6);
    }


    @Bean
    CustomerSupportAgent customerSupportAgent(
            StreamingChatLanguageModel chatLanguageModel,
            Tokenizer tokenizer,
            Retriever<TextSegment> retriever,
            AppointmentTool tool
    ) {

        return AiServices.builder(CustomerSupportAgent.class)
                .streamingChatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatId -> TokenWindowChatMemory.builder()
                        .id(chatId)
                        .maxTokens(1000, tokenizer)
                        .build())
                .retriever(retriever)
                .tools(tool)
                .build();
    }
}
