/*
 * This file is part of MineMark
 * Copyright (C) 2024 DeDiamondPro
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.examplemod;

import net.minecraft.client.MinecraftClient;

//#if FABRIC
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

//#else
//$$ import net.minecraftforge.common.MinecraftForge;
//$$ import net.minecraftforge.event.TickEvent;
//$$ import net.minecraftforge.fml.common.Mod;
//$$
//$$ @Mod("@ID@")
//#endif
public class ExampleMod
        //#if FABRIC == 1
        implements ClientModInitializer
        //#endif
{

    //#if FABRIC == 1
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(event -> tick());
    }
    //#else
    //$$ public ExampleMod() {
    //$$     MinecraftForge.EVENT_BUS.addListener(this::event);
    //$$ }
    //$$
    //$$ private void event(TickEvent.ClientTickEvent event) {
    //$$     if (event.phase != TickEvent.Phase.START) return;
    //$$     tick();
    //$$ }
    //#endif


    private void tick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!(mc.currentScreen instanceof MarkdownTestGui)) {
            mc.setScreen(new MarkdownTestGui());
        }
    }
}