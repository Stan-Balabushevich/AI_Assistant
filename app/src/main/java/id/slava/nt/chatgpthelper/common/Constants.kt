package id.slava.nt.chatgpthelper.common

import id.slava.nt.chatgpthelper.domain.model.Language


enum class AIModels{
    CHAT_GPT,
    GEMINI
}

val aiModels = listOf(
    AIModels.CHAT_GPT,
    AIModels.GEMINI
)

enum class GPTModels(val modelName: String){
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_4("gpt-4"),
    GPT_4O("gpt-4o"),
    GPT_4_TURBO("gpt-4-turbo"),
}

val gptModels = listOf(
    GPTModels.GPT_3_5_TURBO, // A fast, inexpensive model for simple tasks
    GPTModels.GPT_4O_MINI, // affordable and intelligent small model for fast, lightweight tasks
    GPTModels.GPT_4, // The previous set of high-intelligence models
    GPTModels.GPT_4O,// high-intelligence flagship model for complex, multi-step tasks
    GPTModels.GPT_4_TURBO //The previous set of high-intelligence models
)

enum class GeminiModels(val modelName: String){

    // Input: Audio, images, videos, and text
    // Output: Text
    // Optimized for: Fast and versatile performance across a diverse variety of tasks
    GEMINI_1_5_FLASH("gemini-1.5-flash"),
    // Input: Audio, images, videos, and text
    // Output: Text
    // Optimized for: Complex reasoning tasks requiring more intelligence
    GEMINI_1_5_PRO("gemini-1.5-pro"),
    // Input: Text
    // Output: Text
    // Optimized for: Natural language tasks, multi-turn text and code chat, and code generation
    GEMINI_1_0_PRO("gemini-1.0-pro"),

}

val geminiModels = listOf(
    GeminiModels.GEMINI_1_5_FLASH,
    GeminiModels.GEMINI_1_5_PRO,
    GeminiModels.GEMINI_1_0_PRO
)


val languages = listOf(
    Language("en-EN", "English"),
    Language("es-ES", "Spanish"),
    Language("ru-RU", "Russian"),
    Language("uk-UA", "Ukrainian"),
//    Language("pl-PL", "Polish"),
//    Language("fr-FR", "French"),
//    Language("nrf-NO", "Norwegian"),
//    Language("de-DE", "German"),
//    Language("it-IT", "Italian"),
//    Language("pt-PT", "Portuguese")
)

