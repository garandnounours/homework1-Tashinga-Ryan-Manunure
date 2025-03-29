package jetbrains.kotlin.course.alias.results

import jetbrains.kotlin.course.alias.team.Team
import jetbrains.kotlin.course.alias.team.TeamService
import org.springframework.stereotype.Service

typealias GameResult = List<Team>

@Service
class GameResultsService {
    companion object {
        internal val gameHistory: MutableList<GameResult> = mutableListOf()
    }

    fun saveGameResults(result: GameResult) {
        require(result.isNotEmpty()) { "Game result cannot be empty" }
        require(result.all { team -> team.id in TeamService.teamsStorage }) { "All team IDs must be in teamsStorage" }
        gameHistory.add(result)
    }

    fun getAllGameResults(): List<GameResult> = gameHistory.toList()
}
