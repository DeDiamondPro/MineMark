package dev.dediamondpro.minemark.elementa.util

import dev.dediamondpro.minemark.providers.BrowserProvider
import gg.essential.universal.UDesktop
import java.net.URI

object ElementaBrowserProvider : BrowserProvider {
    override fun browse(url: String) {
        UDesktop.browse(URI(url))
    }
}