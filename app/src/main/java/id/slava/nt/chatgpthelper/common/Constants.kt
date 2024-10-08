package id.slava.nt.chatgpthelper.common


enum class AIModels{
    CHAT_GPT,
    GEMINI
}

val aiModels = listOf(
    AIModels.CHAT_GPT,
    AIModels.GEMINI
)

enum class GPTModels(val modelName: String, val modelDescription: String = ""){
    GPT_3_5_TURBO("gpt-3.5-turbo", "Fast and inexpensive model for simple tasks"),
    GPT_4O_MINI("gpt-4o-mini", "Affordable and intelligent small model for fast, lightweight tasks"),
    GPT_4("gpt-4", "The previous set of high-intelligence models"),
    GPT_4O("gpt-4o", "High-intelligence flagship model for complex, multi-step tasks"),
    GPT_4_TURBO("gpt-4-turbo", "The previous set of high-intelligence models"),
}

val gptModels = listOf(
    GPTModels.GPT_3_5_TURBO, // A fast, inexpensive model for simple tasks
    GPTModels.GPT_4O_MINI, // affordable and intelligent small model for fast, lightweight tasks
    GPTModels.GPT_4, // The previous set of high-intelligence models
    GPTModels.GPT_4O,// High-intelligence flagship model for complex, multi-step tasks
    GPTModels.GPT_4_TURBO //The previous set of high-intelligence models
)

enum class GeminiModels(val modelName: String, val modelDescription: String = ""){

    // Input: Audio, images, videos, and text
    // Output: Text
    // Optimized for: Fast and versatile performance across a diverse variety of tasks
    GEMINI_1_5_FLASH("gemini-1.5-flash", "Fast and versatile performance across a diverse variety of tasks"),
    // Input: Audio, images, videos, and text
    // Output: Text
    // Optimized for: Complex reasoning tasks requiring more intelligence
    GEMINI_1_5_PRO("gemini-1.5-pro", "Complex reasoning tasks requiring more intelligence"),
    // Input: Text
    // Output: Text
    // Optimized for: Natural language tasks, multi-turn text and code chat, and code generation
    GEMINI_1_0_PRO("gemini-1.0-pro", "Natural language tasks, multi-turn text and code chat, and code generation"),

}

val geminiModels = listOf(
    GeminiModels.GEMINI_1_5_FLASH,
    GeminiModels.GEMINI_1_5_PRO,
    GeminiModels.GEMINI_1_0_PRO
)

enum class LANGUAGES(val languageCode: String, val languageName: String){

    ENGLISH("en-EN", "English"),
    SPANISH("es-ES", "Spanish"),
    RUSSIAN("ru-RU", "Russian"),
    UKRAINIAN("uk-UA", "Ukrainian")

}

val languages = listOf(
    LANGUAGES.ENGLISH,
    LANGUAGES.SPANISH,
    LANGUAGES.RUSSIAN,
    LANGUAGES.UKRAINIAN)

