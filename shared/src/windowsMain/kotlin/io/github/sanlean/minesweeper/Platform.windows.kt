package io.github.sanlean.minesweeper

class WindowsPlatform: Platform {
    override val target: TargetType = TargetType.WINDOWS
}

actual fun getPlatform(): Platform = WindowsPlatform()
