package jetbrains.kotlin.course.alias.util

typealias Identifier = Int

class IdentifierFactory {
    var counter: Int = 0
        private set

    fun uniqueIdentifier(): Identifier = counter++

    fun setCounter(value: Int) {
        require(value >= 0) { "Counter value must be non-negative" }
        counter = value
    }
} 