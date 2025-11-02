package com.example.senyas

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for FslDictionary translation functionality
 */
class FslDictionaryTest {

    @Test
    fun `translateSentenceToVideoList should match phrases first`() {
        // Test phrase-first matching
        val input = "magandang umaga"
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertEquals(1, matchedVideos.size)
        assertEquals("learn_magandang_umaga", matchedVideos[0])
        assertTrue(unsupportedWords.isEmpty())
    }

    @Test
    fun `translateSentenceToVideoList should match individual words when no phrases found`() {
        // Test individual word matching
        val input = "salamat"
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertEquals(1, matchedVideos.size)
        assertEquals("learn_salamat", matchedVideos[0])
        assertTrue(unsupportedWords.isEmpty())
    }

    @Test
    fun `translateSentenceToVideoList should handle mixed phrases and words`() {
        // Test mixed matching
        val input = "magandang umaga salamat"
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertEquals(2, matchedVideos.size)
        assertTrue(matchedVideos.contains("learn_magandang_umaga"))
        assertTrue(matchedVideos.contains("learn_salamat"))
        assertTrue(unsupportedWords.isEmpty())
    }

    @Test
    fun `translateSentenceToVideoList should identify unsupported words`() {
        // Test unsupported word detection
        val input = "magandang umaga hello world"
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertEquals(1, matchedVideos.size)
        assertEquals("learn_magandang_umaga", matchedVideos[0])
        assertEquals(2, unsupportedWords.size)
        assertTrue(unsupportedWords.contains("hello"))
        assertTrue(unsupportedWords.contains("world"))
    }

    @Test
    fun `translateSentenceToVideoList should handle empty input`() {
        // Test empty input
        val input = ""
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertTrue(matchedVideos.isEmpty())
        assertTrue(unsupportedWords.isEmpty())
    }

    @Test
    fun `translateSentenceToVideoList should normalize input correctly`() {
        // Test input normalization
        val input = "Magandang Umaga, Kumusta Ka!"
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertEquals(2, matchedVideos.size)
        assertTrue(matchedVideos.contains("learn_magandang_umaga"))
        assertTrue(matchedVideos.contains("learn_kumusta_ka"))
        assertTrue(unsupportedWords.isEmpty())
    }

    @Test
    fun `translateSentenceToVideoList should handle no matches`() {
        // Test no matches scenario
        val input = "xyz abc def"
        val (matchedVideos, unsupportedWords) = FslDictionary.translateSentenceToVideoList(input)
        
        assertTrue(matchedVideos.isEmpty())
        assertEquals(3, unsupportedWords.size)
        assertTrue(unsupportedWords.contains("xyz"))
        assertTrue(unsupportedWords.contains("abc"))
        assertTrue(unsupportedWords.contains("def"))
    }

    @Test
    fun `getRandomGesturesForDate should return deterministic results`() {
        // Test deterministic selection
        val dateString = "20250124"
        val gestures1 = FslDictionary.getRandomGesturesForDate(dateString, 3)
        val gestures2 = FslDictionary.getRandomGesturesForDate(dateString, 3)
        
        assertEquals(gestures1, gestures2)
        assertEquals(3, gestures1.size)
    }

    @Test
    fun `getRandomGesturesForDate should return different results for different dates`() {
        // Test different dates produce different results
        val gestures1 = FslDictionary.getRandomGesturesForDate("20250124", 3)
        val gestures2 = FslDictionary.getRandomGesturesForDate("20250125", 3)
        
        assertNotEquals(gestures1, gestures2)
    }

    @Test
    fun `getRandomGesturesForDate should handle insufficient gestures gracefully`() {
        // Test when fewer gestures available than requested
        val gestures = FslDictionary.getRandomGesturesForDate("20250124", 100)
        
        // Should return all available gestures without crashing
        assertTrue(gestures.isNotEmpty())
        assertTrue(gestures.size <= FslDictionary.getAllGestureKeys().size)
    }
}

