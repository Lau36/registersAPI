package com.example.registers_api.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AnalyticsPipelineServiceTest {
    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private IndexOperations indexOperations;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @InjectMocks
    private AnalyticsPipelineService analyticsPipelineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mongoTemplate.indexOps("analytics_register")).thenReturn(indexOperations);
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection("registers")).thenReturn(mongoCollection);
    }

    // ===================================================
    // ✅ TEST 1: ensureIndexes()
    // ===================================================
    @Test
    void ensureIndexes_ShouldCreateUniqueCompoundIndex() {
        // Act
        analyticsPipelineService.ensureIndexes();

        // Assert
        verify(mongoTemplate, times(1)).indexOps("analytics_register");
        verify(indexOperations, times(1)).ensureIndex(any(Index.class));
    }

    // ===================================================
    // ✅ TEST 2: insertInitialSnapshot()
    // ===================================================
    @Test
    void insertInitialSnapshot_ShouldCallRunAggregation_WithNullLayer() {
        // Mock aggregation pipeline
        when(mongoCollection.aggregate(anyList())).thenReturn(mock(com.mongodb.client.AggregateIterable.class));

        // Act
        analyticsPipelineService.insertInitialSnapshot("654321");

        // Assert
        verify(mongoDatabase, times(1)).getCollection("registers");
        verify(mongoCollection, times(1)).aggregate(anyList());
    }

    // ===================================================
    // ✅ TEST 3: insertLayerSnapshot()
    // ===================================================
    @Test
    void insertLayerSnapshot_ShouldCallRunAggregation_WithLayerId() {
        when(mongoCollection.aggregate(anyList())).thenReturn(mock(com.mongodb.client.AggregateIterable.class));

        analyticsPipelineService.insertLayerSnapshot("123456", "layer-abc");

        verify(mongoDatabase, times(1)).getCollection("registers");
        verify(mongoCollection, times(1)).aggregate(anyList());
    }

    // ===================================================
    // ✅ TEST 4: insertAllLayersSnapshot()
    // ===================================================
    @Test
    void insertAllLayersSnapshot_ShouldCallRunAggregation_WithNullLayer() {
        when(mongoCollection.aggregate(anyList())).thenReturn(mock(com.mongodb.client.AggregateIterable.class));

        analyticsPipelineService.insertAllLayersSnapshot("reg-001");

        verify(mongoDatabase, times(1)).getCollection("registers");
        verify(mongoCollection, times(1)).aggregate(anyList());
    }

    // ===================================================
    // ✅ TEST 5: runAggregation() (implícitamente cubierto)
    // ===================================================
    @Test
    void runAggregation_ShouldBuildPipelineCorrectly_WhenValidObjectId() {
        // Arrange
        String validObjectId = new ObjectId().toString();
        when(mongoCollection.aggregate(anyList())).thenReturn(mock(com.mongodb.client.AggregateIterable.class));

        // Act
        analyticsPipelineService.insertInitialSnapshot(validObjectId);

        // Assert: se ejecuta el pipeline
        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(mongoCollection).aggregate(captor.capture());

        List<Document> pipeline = captor.getValue();

        // Verificamos que contiene los pasos clave del pipeline
        assertTrue(pipeline.stream().anyMatch(doc -> doc.containsKey("$match")));
        assertTrue(pipeline.stream().anyMatch(doc -> doc.containsKey("$unwind")));
        assertTrue(pipeline.stream().anyMatch(doc -> doc.containsKey("$project")));
        assertTrue(pipeline.stream().anyMatch(doc -> doc.containsKey("$merge")));

        verify(mongoCollection, times(1)).aggregate(anyList());
    }
}
