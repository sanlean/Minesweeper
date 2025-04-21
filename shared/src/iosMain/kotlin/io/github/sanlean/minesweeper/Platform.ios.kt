package io.github.sanlean.minesweeper

class IOSPlatform: Platform {
    override val target: TargetType = TargetType.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()