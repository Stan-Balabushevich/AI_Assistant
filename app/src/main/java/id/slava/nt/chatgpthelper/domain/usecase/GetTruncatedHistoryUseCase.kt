package id.slava.nt.chatgpthelper.domain.usecase

import id.slava.nt.chatgpthelper.domain.model.UserRequest

class GetTruncatedHistoryUseCase {
    fun execute(
        history: List<UserRequest>,
        maxTokens: Int = 6000
    ): List<UserRequest> {
        var tokenCount = 0
        val truncatedHistory = mutableListOf<UserRequest>()

        // Traverse the list from the most recent message to the oldest
        for (message in history.asReversed()) {
            val tokens = estimateTokens(message.content)
            if (tokenCount + tokens > maxTokens) break
            truncatedHistory.add(0, message) // Add at the front to preserve order
            tokenCount += tokens
        }

        return truncatedHistory
    }

    private fun estimateTokens(text: String): Int {
        return text.length / 4 // Estimate: 1 token ≈ 4 characters
    }
}
