# OpenSearch Vector Database with Java

This project demonstrates how to use **OpenSearch** as a vector database in Java, leveraging OpenSearch’s k-NN capabilities for efficient vector search. It covers setting up an OpenSearch client, inserting vector-based data, querying documents using `match_all`, and executing Java classes using Maven.

## Features

- **Connect to OpenSearch** using Java's OpenSearch client.
- **Insert data** into OpenSearch, including vector embeddings for k-NN search.
- **Retrieve documents** using the `match_all` query.

## Requirements

- **Java 8+**
- **Maven 3+**
- **OpenSearch 1.x or 2.x** (with k-NN plugin installed)
- **OpenSearch running on localhost** (can be adjusted in code)

## Project Structure

```bash
src/main/java/com/jobinesh/opensearch/vectordb/  
    ├── [SearchWithVectorCosineSimilarity.java]    #  vector search with Cosine Similarity 
    ├── SimpleOpenSearchClient.java   # Connection to OpenSearch
```
## Running the Code
Start the OpnSerch nodes using docker-compose 
```bash
cd <opensearch-vector-db>/docker-compose
docker-compose up
```
```Run OpenSerach Client Class
mvn exec:java -Dexec.mainClass="com.jobinesh.opensearch.vectordb.SimpleOpenSearchClient"
```
