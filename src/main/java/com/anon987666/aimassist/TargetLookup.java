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

import static com.anon987666.aimassist.MathUtil.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

public final class TargetLookup {

    private static TargetLookup instance;

    private static final Minecraft MC = Minecraft.getMinecraft();

    private static final Comparator<Entity> YAW_COMPARATOR = (e1, e2) -> {
	final Entity player = MC.player;

	final float dist1 = abs(computeYawDistance(player, e1));
	final float dist2 = abs(computeYawDistance(player, e2));

	return Float.compare(dist1, dist2);
    };

    private static final Predicate<Entity> TYPE_FILTER = entity -> {
	final boolean players = Settings.Targets.players;
	final boolean mobs = Settings.Targets.mobs;
	final boolean animals = Settings.Targets.animals;

	return (players && entity instanceof EntityOtherPlayerMP) || (mobs && entity instanceof EntityMob)
		|| (animals && entity instanceof EntityAnimal);
    };

    private static final Predicate<Entity> DEAD_FILTER = entity -> !entity.isDead;

    private static final Predicate<Entity> DISTANCE_FILTER = entity -> entity
	    .getDistance(MC.player) <= Settings.Misc.targetFindDistance;

    private static final Predicate<Entity> ANGLE_FILTER = entity -> {
	final float yawDistance = abs(computeYawDistance(MC.player, entity));

	return yawDistance <= Settings.Misc.rotationLimit;
    };

    private static final Predicate<Entity> IGNORED_FILTER = entity -> {
	if (!Settings.Ignore.enableWhiteList) {
	    return true;
	}

	final String entityName = entity.getName();

	for (String name : Settings.Ignore.ignored) {
	    if (name.equalsIgnoreCase(entityName)) {
		return false;
	    }
	}

	return true;
    };

    public static TargetLookup instance() {
	if (instance == null) {
	    instance = new TargetLookup();
	}

	return instance;
    }

    private TargetLookup() {

    }

    public Optional<Entity> find() {
	final Stream<Entity> entities = MC.world.loadedEntityList.stream();

	return entities.filter(DISTANCE_FILTER).filter(DEAD_FILTER).filter(ANGLE_FILTER).filter(TYPE_FILTER)
		.filter(IGNORED_FILTER).sorted(YAW_COMPARATOR).findFirst();
    }

}
