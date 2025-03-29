package jetbrains.kotlin.course.alias.util

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game-state/")
class GameStateResource(private val gameStateService: GameStateService) {
    @CrossOrigin
    @GetMapping("/has-saved-game")
    fun hasSavedGame(): Boolean = gameStateService.hasSavedGame()

    @CrossOrigin
    @PostMapping("/save")
    fun saveGame() {
        gameStateService.saveGameState()
    }

    @CrossOrigin
    @PostMapping("/load")
    fun loadGame(): Boolean = gameStateService.loadGameState()

    @CrossOrigin
    @DeleteMapping("/delete")
    fun deleteGame() {
        gameStateService.deleteSavedGame()
    }
} 