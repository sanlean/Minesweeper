package io.github.sanlean.minesweeper

import io.github.sanlean.minesweeper.Platform
import io.github.sanlean.minesweeper.TargetType

class AndroidPlatform : Platform {
    override val target: TargetType = TargetType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()