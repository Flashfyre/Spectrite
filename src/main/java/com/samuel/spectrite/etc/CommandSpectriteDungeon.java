package com.samuel.spectrite.etc;

import java.util.ArrayList;
import java.util.List;

import com.samuel.spectrite.init.ModWorldGen;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandSpectriteDungeon implements ICommand,
	Comparable<ICommand> {

	private List aliases;

	public CommandSpectriteDungeon() {
		this.aliases = new ArrayList();
		this.aliases.add("sd");
	}

	@Override
	public String getUsage(
		ICommandSender icommandsender) {
		return "sd";
	}

	@Override
	public List getAliases() {
		return this.aliases;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "sd";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
		String[] astring) {
		int chunkX = ModWorldGen.spectriteDungeon.getSpawnPos().getX() >> 4;
		int chunkZ = ModWorldGen.spectriteDungeon.getSpawnPos().getZ() >> 4;
		ModWorldGen.spectriteDungeon.generate(sender.getEntityWorld().rand, chunkX, chunkZ, sender.getEntityWorld(), null, null);
		sender.sendMessage(new TextComponentString(String.format("Rebuilt Spectrite Dungeon at chunk(%d, %d)", chunkX, chunkZ)));
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender.canUseCommand(2, this.getName());
	}
}
