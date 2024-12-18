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

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public final class CommandHandler extends CommandBase {

	private static final String NAME = "aimassist";

	private static final String USAGE = "aimassist <enable|disable|toggle>";

	private static final String ENABLE_ACTION = "enable";

	private static final String DISABLE_ACTION = "disable";

	private static final String TOGGLE_ACTION = "toggle";

	private static final AimAssist AIMASSIST = AimAssist.instance();

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return USAGE;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1) {
			throw new CommandException("\u00a74\u00a7lAim assist:\u00a7c Wrong usage");
		}

		final String action = args[0];

		if (action.equalsIgnoreCase(ENABLE_ACTION)) {
			AIMASSIST.setEnabled(true);
		} else if (action.equalsIgnoreCase(DISABLE_ACTION)) {
			AIMASSIST.setEnabled(false);
		} else if (action.equalsIgnoreCase(TOGGLE_ACTION)) {
			AIMASSIST.toggle();
		} else {
			throw new CommandException("\u00a74\u00a7lAim assist:\u00a7c Unknown action \"" + action + "\"");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return Arrays.asList(ENABLE_ACTION, DISABLE_ACTION, TOGGLE_ACTION);
	}

}