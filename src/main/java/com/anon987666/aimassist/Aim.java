/*
 * The MIT License
 *
 * Copyright (c) 2024 Anthony Afonin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.anon987666.aimassist;

import java.util.*;
import java.util.concurrent.*;

import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;

public final class Aim {

	private static Aim instance;

	private static final Minecraft MC = Minecraft.getMinecraft();

	private static final TargetLookup LOOKUP = TargetLookup.instance();

	private static final long DT = TimeUnit.MILLISECONDS.toNanos(5);

	private boolean enabled;

	private long currentTime = System.nanoTime();

	private long accumulator;

	private float aimTime;

	private Optional<Entity> target = Optional.empty();

	public static Aim instance() {
		if (instance == null) {
			instance = new Aim();
		}

		return instance;
	}

	private Aim() {

	}

	private static boolean isPlaying() {
		return MC.world != null && MC.player != null && MC.currentScreen == null;
	}

	public void setEnabled(boolean enabled) {
		if (enabled) {
			MinecraftForge.EVENT_BUS.register(this);
		} else {
			MinecraftForge.EVENT_BUS.unregister(this);
		}

		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Adapted from {@link Entity#turn(float, float)}
	 */
	private void turn(Entity target, float delta) {
		final Entity player = MC.player;

		final float yaw = MathUtil.computeYawDistance(player, target);
		final float pitch = MathUtil.computePitchDistance(player, target);

		final float prevPitch = player.rotationPitch;
		final float prevYaw = player.rotationYaw;

		player.rotationYaw = player.rotationYaw + yaw * Settings.Misc.yawSpeed * delta;
		player.rotationPitch = player.rotationPitch - pitch * Settings.Misc.pitchSpeed * delta;
		player.rotationPitch = MathHelper.clamp(player.rotationPitch, -90f, 90f);

		player.prevRotationPitch += player.rotationPitch - prevPitch;
		player.prevRotationYaw += player.rotationYaw - prevYaw;
	}

	private void update(float delta) {
		if (target.isPresent() && aimTime > 0) {
			turn(target.get(), delta);
			aimTime -= delta;
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (isPlaying() && MC.gameSettings.keyBindAttack.isKeyDown()) {
			target = LOOKUP.find();
			aimTime = Settings.Misc.aimTime / 1000f;
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		final long newTime = System.nanoTime();
		final long frameTime = newTime - currentTime;
		currentTime = newTime;

		accumulator += frameTime;

		while (accumulator >= DT) {
			update(DT / 1e9f);
			accumulator -= DT;
		}
	}

}
