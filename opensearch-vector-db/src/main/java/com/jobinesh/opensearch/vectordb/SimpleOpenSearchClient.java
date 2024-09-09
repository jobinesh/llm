package com.jobinesh.opensearch.vectordb;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.transport.rest_client.RestClientTransport;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class SimpleOpenSearchClient {

    public static OpenSearchClient createClient(String username, String password) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true) // Trust all certificates
                .build();

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder
                                .setSSLContext(sslContext)
                                .setSSLStrategy(new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE))
                                .setDefaultCredentialsProvider(credentialsProvider)
                );

        RestClient restClient = builder.build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new OpenSearchClient(transport);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String username = "admin";
        String password = "yourStrongPassword123!";
        OpenSearchClient client = createClient(username, password);
        String indexName = "jm-open-vector-index";

        if (!doesIndexExist(client, indexName)) {
            createVectorIndex(client, indexName);
        } else {
            log.info("Index already exists: " + indexName);
        }

        Map<String, Object> document = new HashMap<>();
        document.put("name", "Jobs Man");
        document.put("age", 90);
        document.put("city", "New York");

        String documentId = "10001";
        float[] vector = {0.1f, 0.2f, 0.3f, 0.4f};

        putDocumentWithVector(client, indexName, documentId, document, vector);

        float[] queryVector = {0.1f, 0.2f, 0.3f, 0.4f};
        //searchByVector(client, indexName, queryVector);
        retrieveAllDocuments(client, indexName);
        client._transport().close();
    }

    private static boolean doesIndexExist(OpenSearchClient client, String indexName) throws IOException {
        try {
            client.indices().get(b -> b.index(indexName));
            return true;
        } catch (Exception e) {
            //log.error("Error while searching index", e);
            return false;
        }
    }

    private static void createVectorIndex(OpenSearchClient client, String indexName) throws IOException {
        client.indices().create(b -> b
                .index(indexName)
                .mappings(m -> m
                        .properties("personVector", p -> p
                                .knnVector(dv -> dv
                                        .dimension(4)
                                )
                        )
                )
        );
        log.info("Vector index created: " + indexName);
    }

    private static void putDocumentWithVector(OpenSearchClient client, String indexName, String documentId, Map<String, Object> document, float[] vector) throws IOException {
        document.put("personVector", vector);

        IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                .index(indexName)
                .id(documentId)
                .document(document)
        );

        IndexResponse response = client.index(request);
        log.info("Document indexed with ID: " + response.id());
    }

    private static void searchByVector(OpenSearchClient client, String indexName, float[] queryVector) throws IOException {
        var searchResponse = client.search(s -> s
                .index(indexName)
                .query(q -> q
                        .knn(knn -> knn
                                .field("personVector")
                                .vector(queryVector)
                                .k(3)
                        )
                ), Map.class);

        log.info("Response: " + searchResponse);
        searchResponse.hits().hits().forEach(hit -> log.info("Found document: " + hit.source()));
    }
    public static void retrieveAllDocuments(OpenSearchClient client, String indexName) throws IOException {
        // Build the search request using match_all query
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)  // Specify the index
                .query(q -> q
                        .matchAll(m -> m)  // match_all query
                )
        );

        // Execute the search request
        SearchResponse searchResponse = client.search(searchRequest, Map.class);

        // Iterate through the hits and print the documents
        log.info("Response: " + searchResponse);
        searchResponse.hits().hits().forEach(hit -> log.info("Found document: " + hit.toString()));
    }
}