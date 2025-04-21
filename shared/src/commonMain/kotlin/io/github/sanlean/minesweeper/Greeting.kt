package io.github.sanlean.minesweeper

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.target}!"
    }
}