package jetbrains.kotlin.course.alias.util

import jetbrains.kotlin.course.alias.card.Card
import jetbrains.kotlin.course.alias.card.CardService
import jetbrains.kotlin.course.alias.results.GameResult
import jetbrains.kotlin.course.alias.results.GameResultsService
import jetbrains.kotlin.course.alias.team.Team
import jetbrains.kotlin.course.alias.team.TeamService
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException

@Serializable
data class GameState(
    val gameHistory: List<List<Team>>,
    val teamsStorage: Map<Identifier, Team>,
    val teamIdentifierCounter: Int,
    val cardIdentifierCounter: Int,
    val usedCards: List<Card>
)

@Service
class GameStateService(
    private val gameResultsService: GameResultsService,
    private val teamService: TeamService,
    private val cardService: CardService
) {
    private val logger = LoggerFactory.getLogger(GameStateService::class.java)
    private val gameStateFile = File("game_state.json")
    private val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
    }

    fun hasSavedGame(): Boolean = gameStateFile.exists()

    fun saveGameState() {
        try {
            val state = GameState(
                gameHistory = gameResultsService.getAllGameResults(),
                teamsStorage = TeamService.teamsStorage,
                teamIdentifierCounter = teamService.identifierFactory.counter,
                cardIdentifierCounter = cardService.identifierFactory.counter,
                usedCards = cardService.cards
            )
            gameStateFile.writeText(json.encodeToString(state))
            logger.info("Game state saved successfully")
        } catch (e: IOException) {
            logger.error("Failed to save game state", e)
            throw e
        }
    }

    fun loadGameState(): Boolean {
        return try {
            if (!hasSavedGame()) {
                logger.info("No saved game state found")
                return false
            }

            val state = json.decodeFromString<GameState>(gameStateFile.readText())

            // Restore game history
            GameResultsService.gameHistory.clear()
            GameResultsService.gameHistory.addAll(state.gameHistory)

            // Restore teams storage
            TeamService.teamsStorage.clear()
            TeamService.teamsStorage.putAll(state.teamsStorage)

            // Restore identifier counters
            teamService.identifierFactory.setCounter(state.teamIdentifierCounter)
            cardService.identifierFactory.setCounter(state.cardIdentifierCounter)

            // Restore used cards
            cardService.cards = state.usedCards

            logger.info("Game state loaded successfully")
            true
        } catch (e: IOException) {
            logger.error("Failed to load game state", e)
            false
        }
    }

    fun deleteSavedGame(): Boolean {
        return try {
            if (hasSavedGame()) {
                gameStateFile.delete()
                logger.info("Saved game state deleted")
                true
            } else {
                logger.info("No saved game state to delete")
                false
            }
        } catch (e: IOException) {
            logger.error("Failed to delete saved game state", e)
            false
        }
    }
} 