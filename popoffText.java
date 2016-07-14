https://www.agiledeveloper.com/articles/cloning072002.htm

package ninja.genuine.battle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ninja.genuine.battle.system.System;
import ninja.genuine.battle.text.Text;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BattleText.MODID, name = BattleText.NAME, version = BattleText.VERSION)
public class BattleText {

	@Instance(BattleText.MODID)
	public static BattleText instance;
	public static final String MODID = "BattleText";
	public static final String NAME = "BattleText";
	public static final String VERSION = "1.0.17";
	public static File dir;
	public static Configuration config;

	@EventHandler
	public void pre(final FMLPreInitializationEvent event) {
		BattleText.dir = new File(event.getModConfigurationDirectory(), "BattleText");
		config = new Configuration(new File(dir, "Main.cfg"));
		Text.gravity = config.getFloat("gravity", "", 0.5F, 0, 2, "This sets the gravity for the text.");
		Text.sideVariance = config.getBoolean("sideVariance", "", true, "Allows the text to move randomly instead of just falling straight down.");
		Text.fontScale = config.getFloat("fontScale", "", 1.0F, 0.25F, 10.0F, "Adjust the default text size.");
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(System.instance);
		MinecraftForge.EVENT_BUS.register(System.instance);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void post(final FMLPostInitializationEvent event) {
		loadColors();
		saveColors();
		loadColors();
	}

	public void loadColors() {
		if (!BattleText.dir.exists())
			BattleText.dir.mkdirs();
		try {
			final File file = new File(BattleText.dir, "Colors.cfg");
			final BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty() || line.startsWith("#"))
					continue;
				final String[] values = line.split("=");
				if (values.length > 1)
					try {
						Text.Colors.setTextColor(values[0], Integer.decode(values[1]));
					} catch (final Exception e) {
						Text.Colors.setTextColor(values[0], Text.Colors.DEFAULT_COLOR);
					}
				else if (values.length > 0)
					Text.Colors.setTextColor(values[0], Text.Colors.DEFAULT_COLOR);
			}
			br.close();
		} catch (final Exception e) {}
	}

	public void saveColors() {
		try {
			if (!BattleText.dir.exists())
				BattleText.dir.mkdirs();
			final File file = new File(BattleText.dir, "Colors.cfg");
			if (!file.exists())
				file.createNewFile();
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			final List<String> newList = new ArrayList<String>();
			for (final Entry<String, Integer> entry : Text.Colors.textColors.entrySet())
				newList.add(entry.getKey() + "=0x" + Integer.toHexString(entry.getValue()).toUpperCase());
			Collections.sort(newList);
			for (final String line : newList)
				((BufferedWriter) bw.append(line)).newLine();
			bw.close();
		} catch (final Exception e) {}
	}
}

package ninja.genuine.battle.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import ninja.genuine.battle.render.Renderer;
import ninja.genuine.battle.text.Text;

import com.google.common.collect.ImmutableList;
import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class System {

	private static boolean ccIsLoaded() {
		return Loader.isModLoaded(System.CC_MOD_NAME);
	}

	private static final String CC_MOD_NAME = "ClosedCaption";
	private static final String CC_DIRECT_MESSAGE_KEY = "DirectMessage";
	public static final System instance = new System();
	private final Renderer renderer = new Renderer();
	private final List<Text> textList = new ArrayList<Text>();
	private ImmutableList<Text> renderList;
	private long tick;

	private System() {}

	private StringBuilder beginDamageMessage(final DamageSource source) {
		final StringBuilder message = new StringBuilder();
		if (!(source instanceof EntityDamageSource))
			return message;
		final EntityDamageSource nds = (EntityDamageSource) source;
		Entity src = null;
		if (nds instanceof EntityDamageSourceIndirect)
			src = ((EntityDamageSourceIndirect) nds).getEntity();
		else
			src = nds.getSourceOfDamage();
		if (src instanceof EntityPlayer)
			message.append(damageSourceName(((EntityPlayer) src).getDisplayName()));
		else
			message.append(damageSourceName(src.getCommandSenderName()));
		return message;
	}

	private String constructDamageMessage(final DamageSource source) {
		final StringBuilder message = beginDamageMessage(source);
		message.append(constructDamageTypes(source));
		message.append(": ");
		message.append(ChatFormatting.DARK_RED);
		return message.toString();
	}

	private String constructDamageTypes(final DamageSource source) {
		final String[] tmps = source.getDamageType().split("\\.");
		final StringBuilder sourceTypes = new StringBuilder();
		for (final String string : tmps)
			sourceTypes.append(string.substring(0, 1).toUpperCase() + string.substring(1));
		return sourceTypes.toString().replaceAll("[A-Z]", " $0").trim();
	}

	private String damageSourceName(final String name) {
		final StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(ChatFormatting.BLUE);
		nameBuilder.append(name);
		nameBuilder.append(ChatFormatting.RESET);
		nameBuilder.append(" -> ");
		return nameBuilder.toString();
	}

	@SubscribeEvent
	public void entityHeal(final LivingHealEvent event) {
		if (event.amount <= 0)
			return;
		if (System.ccIsLoaded() && event.entityLiving.equals(Minecraft.getMinecraft().thePlayer)) {
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", "healing");
			tag.setFloat("amount", event.amount);
			FMLInterModComms.sendMessage(System.CC_MOD_NAME, System.CC_DIRECT_MESSAGE_KEY, tag);
			return;
		}
		synchronized (textList) {
			textList.add(new Text(event.entityLiving, event.amount));
		}
	}

	@SubscribeEvent
	public void entityHurt(final LivingHurtEvent event) {
		if (event.ammount <= 0)
			return;
		if (System.ccIsLoaded() && event.entityLiving.equals(Minecraft.getMinecraft().thePlayer)) {
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", "damage");
			tag.setFloat("amount", event.ammount);
			tag.setString("message", constructDamageMessage(event.source));
			FMLInterModComms.sendMessage(System.CC_MOD_NAME, System.CC_DIRECT_MESSAGE_KEY, tag);
			return;
		}
		synchronized (textList) {
			textList.add(new Text(event.entityLiving, event.source, event.ammount));
		}
	}

	@SubscribeEvent
	public void renderWorldEvent(final RenderWorldLastEvent event) {
		tick();
		renderer.render(renderList, event.partialTicks);
	}

	public void tick() {
		final long tick = RenderManager.instance.worldObj.getTotalWorldTime();
		if (this.tick == tick)
			return;
		this.tick = tick;
		synchronized (textList) {
			final List<Text> removalQueue = new ArrayList<Text>();
			for (final Text text : textList)
				if (!text.onUpdate())
					removalQueue.add(text);
			textList.removeAll(removalQueue);
			Collections.sort(textList);
			renderList = ImmutableList.copyOf(textList);
		}
	}
}

package ninja.genuine.battle.render;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import ninja.genuine.battle.text.Text;

import org.lwjgl.opengl.GL11;

public class Renderer {

	public Renderer() {}

	public void render(final List<Text> renderList, final double partialTicks) {
		if (renderList == null || renderList.isEmpty())
			return;
		final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (final Text txt : renderList) {
			if (txt.getDistanceTo(Minecraft.getMinecraft().thePlayer) > 24)
				continue;
			final double x = RenderManager.renderPosX - (txt.prevPosX + (txt.posX - txt.prevPosX) * partialTicks);
			final double y = RenderManager.renderPosY - (txt.prevPosY + (txt.posY - txt.prevPosY) * partialTicks) - 2;
			final double z = RenderManager.renderPosZ - (txt.prevPosZ + (txt.posZ - txt.prevPosZ) * partialTicks);
			GL11.glTranslated(-x, -y, -z);
			GL11.glRotatef(RenderManager.instance.playerViewY + 180, 0F, -1F, 0F);
			GL11.glRotatef(RenderManager.instance.playerViewX, -1F, 0F, 0F);
			int alpha = (int) (txt.getPercent() * 0xFF) & 0xFF;
			if (alpha < 5)
				alpha = 5;
			final int color1 = txt.textColor | alpha << 24;
			final int color2 = txt.backgroundColor | alpha << 24;
			final int offX = -fr.getStringWidth(txt.display);
			final int offY = -4;
			double scale = 0.02;
			scale *= txt.getScale();
			GL11.glScaled(scale, -scale, scale);
			// Shadows
			fr.drawString(txt.display, offX + 1, offY, color2);
			fr.drawString(txt.display, offX, offY + 1, color2);
			fr.drawString(txt.display, offX, offY - 1, color2);
			fr.drawString(txt.display, offX - 1, offY, color2);
			// Main
			fr.drawString(txt.display, offX, offY, color1);
			GL11.glScaled(1.0 / scale, -1.0 / scale, 1.0 / scale);
			GL11.glRotatef(RenderManager.instance.playerViewX, 1F, 0F, 0F);
			GL11.glRotatef(RenderManager.instance.playerViewY - 180, 0F, 1F, 0F);
			GL11.glTranslated(x, y, z);
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}

package ninja.genuine.battle.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class Text implements Comparable<Text> {

	public static class Colors {

		public static int getBackgroundColor(final String name) {
			if (!Colors.backgroundColors.containsKey(name))
				Colors.backgroundColors.put(name, 0);
			return Colors.backgroundColors.get(name);
		}

		public static int getTextColor(final String name) {
			if (!Colors.textColors.containsKey(name))
				Colors.textColors.put(name, Colors.DEFAULT_COLOR);
			return Colors.textColors.get(name);
		}

		public static void setBackgroundColor(final String name, final int color) {
			Colors.backgroundColors.put(name, color);
		}

		public static void setTextColor(final String name, final int color) {
			Colors.textColors.put(name, color);
		}

		public static int DEFAULT_COLOR = 0xFF5020;
		public static Map<String, Integer> textColors = new HashMap<String, Integer>();
		public static Map<String, Integer> backgroundColors = new HashMap<String, Integer>();

		static {
			Colors.textColors.put("arrow", 0xFE2712);
			Colors.textColors.put("cactus", Colors.DEFAULT_COLOR);
			Colors.textColors.put("drown", Colors.DEFAULT_COLOR);
			Colors.textColors.put("explosion", 0xFE2712);
			Colors.textColors.put("explosion.player", 0xFE2712);
			Colors.textColors.put("fall", Colors.DEFAULT_COLOR);
			Colors.textColors.put("generic", Colors.DEFAULT_COLOR);
			Colors.textColors.put("heal", 0x00A550);
			Colors.textColors.put("inFire", 0xFF7F00);
			Colors.textColors.put("inWall", Colors.DEFAULT_COLOR);
			Colors.textColors.put("indirectMagic", 0xA020F0);
			Colors.textColors.put("lava", 0x4F0000);
			Colors.textColors.put("magic", 0xA020F0);
			Colors.textColors.put("mob", Colors.DEFAULT_COLOR);
			Colors.textColors.put("onFire", 0xFF7F00);
			Colors.textColors.put("outOfWorld", 0);
			Colors.textColors.put("player", Colors.DEFAULT_COLOR);
			Colors.textColors.put("thrown", Colors.DEFAULT_COLOR);
			Colors.textColors.put("wither", 0x505050);
			Colors.backgroundColors.put("ofOfWorld", -1);
		}
	}

	public static float gravity = 0.5F;
	public static float fontScale = 1.0F;
	public static boolean sideVariance = true;
	public String display;
	public final float amount;
	public int textColor;
	public int backgroundColor;
	protected int ticks;
	protected int lifetime = 40;
	public double posX;
	public double posY;
	public double posZ;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	private double motionX;
	private double motionY;
	private double motionZ;
	private final Random random = new Random();

	public Text(final EntityLivingBase entity, final DamageSource damageSource, final float damage) {
		setupPos(entity);
		final String name = damageSource.damageType;
		setAmount("-", damage, name, false);
		amount = damage;
	}

	public Text(final EntityLivingBase entity, final float healing) {
		setupPos(entity);
		setAmount("+", healing, "heal", false);
		amount = healing;
	}

	@Override
	public int compareTo(final Text text) {
		if (Minecraft.getMinecraft().thePlayer == null)
			return 0;
		final double distance1 = this.getDistanceTo(Minecraft.getMinecraft().thePlayer);
		final double distance2 = text.getDistanceTo(Minecraft.getMinecraft().thePlayer);
		if (distance1 == distance2)
			return 0;
		else if (distance2 > distance1)
			return 1;
		else
			return -1;
	}

	public double getDistanceTo(final double posX, final double posY, final double posZ) {
		final double distanceX = this.posX - posX;
		final double distanceY = this.posY - posY;
		final double distanceZ = this.posZ - posZ;
		return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
	}

	public double getDistanceTo(final Entity entity) {
		return this.getDistanceTo(entity.posX, entity.posY, entity.posZ);
	}

	public double getDistanceTo(final Text text) {
		return this.getDistanceTo(text.posX, text.posY, text.posZ);
	}

	public double getInterpPercent(final double delta) {
		return getPreviousPercent() + (getPercent() - getPreviousPercent()) * delta;
	}

	public double getPercent() {
		return (double) ticks / (double) lifetime;
	}

	public double getPreviousPercent() {
		return (double) (ticks + 1) / (double) lifetime;
	}

	public float getScale() {
		final float out = amount / 100;
		return (1 + out) * fontScale;
	}

	public void move() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionX *= 0.95;
		motionY -= Text.gravity / 100;
		motionZ *= 0.95;
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
	}

	public boolean onUpdate() {
		if (ticks-- <= 0)
			return false;
		move();
		return true;
	}

	public void setAmount(final String prefix, final float amount, final String name, final boolean flag) {
		textColor = Colors.getTextColor(name) & 0xFFFFFF;
		backgroundColor = Colors.getBackgroundColor(name) & 0xFFFFFF;
		if (flag)
			display = prefix + (int) amount + " (" + name + ")";
		else
			display = prefix + (int) amount;
	}

	private void setupPos(final EntityLivingBase entity) {
		ticks = lifetime;
		posX = entity.posX;
		posY = entity.posY;
		posZ = entity.posZ;
		prevPosX = entity.prevPosX;
		prevPosY = entity.prevPosY;
		prevPosZ = entity.prevPosZ;
		motionX = random.nextGaussian() / 24;
		motionY = random.nextGaussian() / 32;
		motionZ = random.nextGaussian() / 24;
	}
}
