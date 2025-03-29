package jetbrains.kotlin.course.alias

import jetbrains.kotlin.course.alias.util.GameStateService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@SpringBootApplication
class AliasApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AliasApplication>(*args)
}

@Component
class GameStateManager(private val gameStateService: GameStateService) {
    private val logger = LoggerFactory.getLogger(GameStateManager::class.java)

    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationStart(event: ContextRefreshedEvent) {
        logger.info("Application starting - checking for saved game state...")
        if (gameStateService.hasSavedGame()) {
            logger.info("Found saved game state - attempting to load...")
            if (gameStateService.loadGameState()) {
                logger.info("Successfully loaded previous game state")
            } else {
                logger.warn("Failed to load previous game state")
            }
        } else {
            logger.info("No saved game state found - starting fresh")
        }
    }

    @EventListener(ContextClosedEvent::class)
    fun onApplicationStop(event: ContextClosedEvent) {
        logger.info("Application shutting down - saving game state...")
        try {
            gameStateService.saveGameState()
            logger.info("Game state saved successfully")
        } catch (e: Exception) {
            logger.error("Failed to save game state", e)
        }
    }
}

