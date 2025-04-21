package io.github.sanlean.minesweeper

class JVMPlatform: Platform {
    override val target: TargetType = TargetType.JVM
}

actual fun getPlatform(): Platform = JVMPlatform()