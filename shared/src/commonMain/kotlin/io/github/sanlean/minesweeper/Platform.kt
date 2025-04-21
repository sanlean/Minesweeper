package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.settings.SaveAdapter

interface Platform {
    val target: TargetType
}

expect fun getPlatform(): Platform