/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.hooks;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.event.TickEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class BasicEventHooks
{

    public static void firePlayerChangedDimensionEvent(PlayerEntity player, RegistryKey<World> fromDim, RegistryKey<World> toDim)
    {
        firePlayerChangedDimensionEvent(player, fromDim, toDim, MinecraftServer.getServer().getLevel(fromDim).getWorld());
    }

    public static void firePlayerChangedDimensionEvent(PlayerEntity player, RegistryKey<World> fromDim, RegistryKey<World> toDim, CraftWorld fromWorld)
    {
        PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) player.getBukkitEntity(), fromWorld);
        Bukkit.getServer().getPluginManager().callEvent(event);
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerChangedDimensionEvent(player, fromDim, toDim));
    }

    public static void firePlayerLoggedIn(PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(player));
    }

    public static void firePlayerLoggedOut(PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedOutEvent(player));
    }

    public static void firePlayerRespawnEvent(PlayerEntity player, boolean endConquered)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(player, endConquered));
    }

    public static void firePlayerItemPickupEvent(PlayerEntity player, ItemEntity item, ItemStack clone)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemPickupEvent(player, item, clone));
    }

    public static void firePlayerCraftingEvent(PlayerEntity player, ItemStack crafted, IInventory craftMatrix)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player, crafted, craftMatrix));
    }

    public static void firePlayerSmeltedEvent(PlayerEntity player, ItemStack smelted)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemSmeltedEvent(player, smelted));
    }

    public static void onRenderTickStart(float timer)
    {
        Animation.setClientPartialTickTime(timer);
        MinecraftForge.EVENT_BUS.post(new TickEvent.RenderTickEvent(TickEvent.Phase.START, timer));
    }

    public static void onRenderTickEnd(float timer)
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.RenderTickEvent(TickEvent.Phase.END, timer));
    }

    public static void onPlayerPreTick(PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));
    }

    public static void onPlayerPostTick(PlayerEntity player)
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
    }

    public static void onPreWorldTick(World world)
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.WorldTickEvent(LogicalSide.SERVER, TickEvent.Phase.START, world));
    }

    public static void onPostWorldTick(World world)
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.WorldTickEvent(LogicalSide.SERVER, TickEvent.Phase.END, world));
    }

    public static void onPreClientTick()
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ClientTickEvent(TickEvent.Phase.START));
    }

    public static void onPostClientTick()
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ClientTickEvent(TickEvent.Phase.END));
    }

    public static void onPreServerTick()
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ServerTickEvent(TickEvent.Phase.START));
    }

    public static void onPostServerTick()
    {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ServerTickEvent(TickEvent.Phase.END));
    }
}
