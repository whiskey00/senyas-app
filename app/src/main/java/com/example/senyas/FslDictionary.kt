package com.example.senyas

object FslDictionary {

    private val dictionary = mapOf(
        // Common Greetings and Phrases
        "magandang umaga" to "fsl_magandang_umaga",
        "magandang hapon" to "fsl_magandang_hapon",
        "magandang gabi" to "fsl_magandang_gabi",
        "kumusta ka" to "fsl_kumusta_ka",
        "kumusta" to "fsl_kumusta",
        "salamat" to "fsl_salamat",
        "walang anuman" to "fsl_walang_anuman",
        "paalam" to "fsl_paalam",
        "mabuti" to "fsl_mabuti",
        "masama" to "fsl_masama",
        
        // Food and Drinks
        "walang asukal" to "fsl_walang_asukal",
        "may asukal" to "fsl_may_asukal",
        "tubig" to "fsl_tubig",
        "kape" to "fsl_kape",
        "gatas" to "fsl_gatas",
        "tinapay" to "fsl_tinapay",
        "kanin" to "fsl_kanin",
        "ulam" to "fsl_ulam",
        
        // Numbers
        "isa" to "fsl_isa",
        "dalawa" to "fsl_dalawa",
        "tatlo" to "fsl_tatlo",
        "apat" to "fsl_apat",
        "lima" to "fsl_lima",
        "anim" to "fsl_anim",
        "pito" to "fsl_pito",
        "walo" to "fsl_walo",
        "siyam" to "fsl_siyam",
        "sampu" to "fsl_sampu",
        
        // Colors
        "asul" to "fsl_asul",
        "pula" to "fsl_pula",
        "dilaw" to "fsl_dilaw",
        "berde" to "fsl_berde",
        "itim" to "fsl_itim",
        "puti" to "fsl_puti",
        "kayumanggi" to "fsl_kayumanggi",
        
        // Family and People
        "nanay" to "fsl_nanay",
        "tatay" to "fsl_tatay",
        "lola" to "fsl_lola",
        "lolo" to "fsl_lolo",
        "kapatid" to "fsl_kapatid",
        "kaibigan" to "fsl_kaibigan",
        "guro" to "fsl_guro",
        "doktor" to "fsl_doktor",
        
        // Common Actions
        "kumain" to "fsl_kumain",
        "uminom" to "fsl_uminom",
        "matulog" to "fsl_matulog",
        "maglaro" to "fsl_maglaro",
        "magbasa" to "fsl_magbasa",
        "magsulat" to "fsl_magsulat",
        "maglakad" to "fsl_maglakad",
        "tumakbo" to "fsl_tumakbo",
        
        // Time and Days
        "ngayon" to "fsl_ngayon",
        "bukas" to "fsl_bukas",
        "kahapon" to "fsl_kahapon",
        "lunes" to "fsl_lunes",
        "martes" to "fsl_martes",
        "miyerkules" to "fsl_miyerkules",
        "huwebes" to "fsl_huwebes",
        "biyernes" to "fsl_biyernes",
        "sabado" to "fsl_sabado",
        "linggo" to "fsl_linggo",
        
        // Common Words
        "oo" to "fsl_oo",
        "hindi" to "fsl_hindi",
        "sige" to "fsl_sige",
        "tama" to "fsl_tama",
        "mali" to "fsl_mali",
        "mabagal" to "fsl_mabagal",
        "mabilis" to "fsl_mabilis",
        "malaki" to "fsl_malaki",
        "maliit" to "fsl_maliit",
        "mainit" to "fsl_mainit",
        "malamig" to "fsl_malamig"
    )

    fun getAllGestureKeys(): List<String> {
        return dictionary.values.toList()
    }

    fun getDictionary(): Map<String, String> {
        return dictionary
    }

    fun translateSentenceToVideoList(input: String): Pair<List<String>, List<String>> {
        val normalizedInput = input.lowercase()
            .replace(Regex("[^a-z\\s]"), " ") // Remove punctuation
            .replace(Regex("\\s+"), " ") // Normalize whitespace
            .trim()
        
        val words = normalizedInput.split(" ")
        val matchedVideos = mutableListOf<String>()
        val unsupportedWords = mutableListOf<String>()
        val usedIndices = mutableSetOf<Int>()
        
        // First pass: try to match phrases (longest first)
        val sortedPhrases = dictionary.keys.sortedByDescending { it.length }
        
        for (phrase in sortedPhrases) {
            val phraseWords = phrase.split(" ")
            if (phraseWords.size > 1) { // Only multi-word phrases
                val phraseStart = normalizedInput.indexOf(phrase)
                if (phraseStart != -1) {
                    // Check if this phrase is not already used
                    val phraseWordIndices = mutableListOf<Int>()
                    var currentIndex = 0
                    var found = true
                    
                    for (phraseWord in phraseWords) {
                        val wordIndex = words.subList(currentIndex, words.size).indexOf(phraseWord)
                        if (wordIndex == -1 || usedIndices.contains(currentIndex + wordIndex)) {
                            found = false
                            break
                        }
                        phraseWordIndices.add(currentIndex + wordIndex)
                        currentIndex = currentIndex + wordIndex + 1
                    }
                    
                    if (found) {
                        dictionary[phrase]?.let { videoName ->
                            matchedVideos.add(videoName)
                            usedIndices.addAll(phraseWordIndices)
                        }
                    }
                }
            }
        }
        
        // Second pass: match individual words not already used
        for (i in words.indices) {
            if (!usedIndices.contains(i)) {
                val word = words[i]
                val videoName = dictionary[word]
                if (videoName != null) {
                    matchedVideos.add(videoName)
                    usedIndices.add(i)
                } else {
                    unsupportedWords.add(word)
                }
            }
        }
        
        return Pair(matchedVideos, unsupportedWords)
    }

    fun getRandomGesturesForDate(dateString: String, count: Int = 3): List<String> {
        val allGestures = getAllGestureKeys()
        if (allGestures.size <= count) {
            return allGestures.shuffled()
        }
        
        // Use date string as seed for deterministic selection
        val seed = dateString.hashCode().toLong()
        val random = kotlin.random.Random(seed)
        val selectedIndices = mutableSetOf<Int>()
        
        while (selectedIndices.size < count) {
            val randomIndex = random.nextInt(allGestures.size)
            selectedIndices.add(randomIndex)
        }
        
        return selectedIndices.map { allGestures[it] }
    }
}
