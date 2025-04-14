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


import net.minecraft.client.Minecraft;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//?} else if forge {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
*///?}

//? if forge
/*@Mod("examplemod")*/
public class ExampleMod /*? if fabric {*/ implements ClientModInitializer /*?}*/ {

    //? if fabric {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(event -> tick());
    }
    //?}

    //? if forge {
    /*public ExampleMod() {
        MinecraftForge.EVENT_BUS.addListener(this::event);
    }

    private void event(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        tick();
    }
    *///?}

    private void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof MarkdownTestGui)) {
            mc.setScreen(new MarkdownTestGui());
        }
    }
}