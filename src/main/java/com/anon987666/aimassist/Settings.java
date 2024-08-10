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

import net.minecraftforge.common.config.*;
import net.minecraftforge.common.config.Config.*;

public class Settings {

	@Config(modid = AimAssist.MODID, name = "Aim targets")
	public static class Targets {

		@Name("Players")
		public static boolean players = true;

		@Name("Mobs")
		public static boolean mobs = true;

		@Name("Animals")
		public static boolean animals = false;

	}

	@Config(modid = AimAssist.MODID, name = "Aim white list")
	public static class Ignore {

		@Name("Ignored names")
		public static String[] ignored = new String[0];

		@Name("Enable white list")
		public static boolean enableWhiteList;

	}

	@Config(modid = AimAssist.MODID, name = "Aim options")
	public static class Misc {

		@Name("Yaw speed")
		@RangeInt(min = 0, max = 50)
		@SlidingOption
		public static int yawSpeed = 20;

		@Name("Pitch speed")
		@RangeInt(min = 0, max = 50)
		@SlidingOption
		public static int pitchSpeed = 15;

		@Name("Aim time")
		@RangeInt(min = 0, max = 1000)
		@SlidingOption
		public static int aimTime = 200;

		@Name("Rotation limit")
		@RangeInt(min = 0, max = 180)
		@SlidingOption
		public static int rotationLimit = 80;

		@Name("Target find distance")
		@RangeDouble(min = 1, max = 10)
		@SlidingOption
		public static double targetFindDistance = 5;

	}
}
