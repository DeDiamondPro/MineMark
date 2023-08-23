package dev.dediamondpro.minemark.elementa.config

import dev.dediamondpro.minemark.config.RenderConfig
import gg.essential.universal.UMatrixStack

data class RenderConfigImpl(val matrixStack: UMatrixStack) : RenderConfig()
