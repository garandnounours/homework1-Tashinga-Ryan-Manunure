package jetbrains.kotlin.course.alias.card

import jetbrains.kotlin.course.alias.util.IdentifierFactory
import jetbrains.kotlin.course.alias.util.Words
import org.springframework.stereotype.Service

@Service
class CardService {
    internal val identifierFactory = IdentifierFactory()
    internal var cards: List<Card> = generateCards()

    companion object {
        const val WORDS_IN_CARD = 4
        val cardsAmount: Int = Words.wordsList.size / WORDS_IN_CARD
    }

    private fun List<String>.toWords(): List<Word> = map { Word(it) }

    private fun generateCards(): List<Card> {
        val shuffledWords = Words.wordsList.shuffled()
        val wordChunks = shuffledWords.chunked(WORDS_IN_CARD).take(cardsAmount)
        return wordChunks.mapIndexed { _: Int, words: List<String> ->
            Card(identifierFactory.uniqueIdentifier(), words.toWords())
        }
    }

    fun getCardByIndex(index: Int): Card {
        require(index in cards.indices) { "Card with index $index not found" }
        return cards[index]
    }
}
