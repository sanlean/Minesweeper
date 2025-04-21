package io.github.sanlean.minesweeper

class JSPlatform: Platform {
    override val target: TargetType = TargetType.JS

}

actual fun getPlatform(): Platform = JSPlatform()