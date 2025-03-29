package jetbrains.kotlin.course.alias.results

import alias.JsTeam
import jetbrains.kotlin.course.alias.util.GameStateService
import jetbrains.kotlin.course.alias.util.toArrayJsTeams
import jetbrains.kotlin.course.alias.util.toGameResult
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/results")
class GameResultsResource(
    private val gameResultsService: GameResultsService,
    private val gameStateService: GameStateService
) {
    private val logger = LoggerFactory.getLogger(GameResultsResource::class.java)

    @CrossOrigin
    @PostMapping("/save")
    fun saveGameResults(@RequestBody teams: List<JsTeam>) {
        logger.info("Saving game results for ${teams.size} teams")
        try {
            gameResultsService.saveGameResults(teams.toGameResult())
            // After saving results, also save the game state
            gameStateService.saveGameState()
            logger.info("Game results and state saved successfully")
        } catch (e: Exception) {
            logger.error("Failed to save game results", e)
            throw e
        }
    }

    @CrossOrigin
    @GetMapping("/all")
    fun getAllGameResults(): Array<Array<JsTeam>> {
        logger.info("Retrieving all game results")
        return gameResultsService.getAllGameResults()
            .map { it.toArrayJsTeams() }
            .toTypedArray()
    }

    @CrossOrigin
    @GetMapping("/latest")
    fun getLatestGameResult(): Array<JsTeam>? {
        return gameResultsService.getAllGameResults()
            .firstOrNull()
            ?.toArrayJsTeams()
    }
}
