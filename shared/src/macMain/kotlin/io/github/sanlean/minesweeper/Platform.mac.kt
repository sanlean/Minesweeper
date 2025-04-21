package io.github.sanlean.minesweeper

class MacOSPlatform: Platform {
    override val target: TargetType = TargetType.MACOS
}

actual fun getPlatform(): Platform = MacOSPlatform()