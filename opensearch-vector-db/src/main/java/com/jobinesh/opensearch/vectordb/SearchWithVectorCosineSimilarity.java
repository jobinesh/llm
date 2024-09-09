package com.jobinesh.opensearch.vectordb;

import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.search.Hit;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
public class SearchWithVectorCosineSimilarity {

    public static void searchWithVector(OpenSearchClient client) throws IOException {
        // Example query vector
        float[] queryVector = new float[128];
        for (int i = 0; i < queryVector.length; i++) {
            queryVector[i] = 0.5f;
        }

        // Cosine similarity script
        String script = "cosineSimilarity(params.query_vector, 'vector_field') + 1.0";

        Map params = new HashMap<>();
        params.put("query_vector", queryVector);

        /** Build the search request*/
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("vector_index")
                .query(q -> q
                        .scriptScore(s -> s
                                .query(qq -> qq.matchAll(m -> m))
                                .script(ss -> ss
                                        .inline(i -> i
                                                .source(script)
                                                .params(params)

                                        )
                                )
                        )
                )
                .build();

        // Execute the search
        SearchResponse<Map> response = client.search(searchRequest, Map.class);
        List<Hit<Map>> hits = response.hits().hits();

        // Print out the search hits
        for (Hit<Map> hit : hits) {
           log.info(hit.source().toString());
        }
    }
}
