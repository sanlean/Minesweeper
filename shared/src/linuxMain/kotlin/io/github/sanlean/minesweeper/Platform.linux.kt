package io.github.sanlean.minesweeper

class LinuxPlatform: Platform {
    override val target: TargetType = TargetType.LINUX
}

actual fun getPlatform(): Platform = LinuxPlatform()