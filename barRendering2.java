


	public class RenderingMob {
	
		private EventMob storedMob;
		private double barSize = 25;
		private double rightX;
		private double leftX;
		private double lastRightX;
		private double hitLeftX;
		private double topY = 0;
		private double bottomY = 4;
		private double zeroZ = 0;
		
		public RenderingMob(EventMob storedMob){
			this.storedMob = storedMob;
			this.rightX = ((this.barSize * getRatioOfMaxHp()) * 2.0F) - this.barSize;
			this.leftX = -this.barSize;
			this.lastRightX = (((this.barSize * getHitRatioOfMaxHp()) * 2.0F) - this.barSize) - this.rightX;
			this.hitLeftX = this.rightX;
		}
		
		private float getRatioOfMaxHp(){
			return storedMob.getHp() / storedMob.getMaxHp();
		}
	
		private float getHitRatioOfMaxHp(){
			return storedMob.getLastHit() / storedMob.getMaxHp();
		}
	
		private double getBarSize(){
			return barSize;
		}
		
		public double getRightX(){
			return this.rightX;
		}
		
		public double getLastRightX(){
			return this.lastRightX;
		}
		
		public double getLeftX(){
			return -barSize;
		}
		
		public double getHitLeftX(){
			return this.hitLeftX;
		}
		
		public double getTopY(){
			return this.topY;
		}
		
		public double getBottomY(){
			return this.bottomY;
		}
		
		public double getZeroZ(){
			return this.zeroZ;
		}
	}
	
	//ADD getMaxHP to EventMob
	
				
	
	public static void renderEvent(float partialTicks){
		for (Entity entity : getWorldEntitiesList()){
			if (
					(entity != null) 
					&& ((entity instanceof EntityLiving))
					&& (isInRangeToRender3d())
					&& ((entity.ignoreFrustumCheck) || (getFrustrum().isBoundingBoxInFrustum(entity.boundingBox)))
					&& (entity.isEntityAlive())) {
					renderMobBar(ModCore.getStoredMob(getKey(entity)),partialTicks);
			}
		}
	}
		
	private static String getKey(Entity entity){
		return entity.getCommandSenderName(); + "/" + entity.getEntityId();
	}
	
	@SuppressWarnings("unchecked")
	private static Set<Entity> getWorldEntitiesList(){
		return (Set<Entity>) ReflectionHelper.getPrivateValue(WorldClient.class,Minecraft.getMinecraft().theWorld,new String[] {"entityList","field_73032_d","J" });
	}
	
	private static void createRenderingMob(){
		return new RenderingMob(storedMob);
	}
	
	renderMobBar(EventMob storedMob, float partialTicks){
	
	}
	
	private static void drawMobBar(double leftX, double rightX, double topY, double bottomY, double z){
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setColorRGBA(red, green, blue, alpha);
		Tessellator.instance.addVertex(leftX, topY, z);
		Tessellator.instance.addVertex(leftX, bottomY, z);
		Tessellator.instance.addVertex(rightX, topY, z);
		Tessellator.instance.addVertex(rightX, bottomY, z);
		Tessellator.instance.draw();
	}
	
	private static void drawCurrentHealthBar(){
		drawMobBar(getLeftX(),getRightX(),getTopY(),getBottomY(),getZeroZ());
	}
	
	private static void drawLastHitBar(){
		drawMobBar(getHitLeftX(),getLastRightX(),getTopY(),getBottomY(),getZeroZ());
	}
	
	private static void drawLastHealthBar(){
		drawMobBar(getLeftX(),getLastRightX(),getTopY(),getBottomY(),getZeroZ());
	}
	
	private static void shrinkBar(){
		
		get lastHP
		get lastHit
		int currentHP = lastHP;
		for(int i = 0, i < lastHit, i++){
			currentHP -= i;
			drawCurrentHP(currentHP);
		}
	}
	
	private static float getEntityX(){
		return (float) ((entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) - RenderManager.renderPosX);
	}
	
	private static float getEntityY(){
		return (float) ((entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) - RenderManager.renderPosY + entity.height + heightAbove);
	}
	
	private static float getEntityZ(){
		return (float) ((entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) - RenderManager.renderPosZ);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	private static Color hpBarColor(){
		return Color.getHSBColor(Math.max(0.0F, health / maxHealth / 3.0F - 0.07F), 1.0F, 1.0F);
	}
		
	private static void drawMobBar(double leftX, double rightX, double topY, double bottomY, double z, Color rgb){
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), 127);
		tessellator.addVertex(leftX, topY, z);
		tessellator.addVertex(leftX, bottomY, z);
		tessellator.addVertex(rightX, topY, z);
		tessellator.addVertex(rightX, bottomY, z);
		tessellator.draw();
	}
	
	private static void drawMobBar2(RenderingMob mob, Color color){
		if(mob.getLastHit = 0){
			drawCurrentHealthBar(mob,color);
		}
		if(mob.getLastHit > 0){
			drawLastHealthBar(mob,color);
			drawLastHitBar(mob,color);
		}
	}
	
	
	
	private static void drawCurrentHealthBar(RenderingMob mob, Color color){
		drawMobBar(mob.getLeftX(),mob.getRightX(),mob.getTopY(),mob.getBottomY(),mob.getZeroZ(), color);
	}
	
	private static void drawLastHitBar(RenderingMob mob, Color color){
		drawMobBar(getHitLeftX(),getLastRightX(),getTopY(),getBottomY(),getZeroZ(), color);
	}
	
	private static void drawLastHealthBar(RenderingMob mob, Color color){
		drawMobBar(getLeftX(),getLastRightX(),getTopY(),getBottomY(),getZeroZ(), color);
	}
	
	
	
	
	
	
	private static void startMatrix(){
		GL11.glPushMatrix();
	}
	
	private static void endMatrix(){
		GL11.glPopMatrix();
	}
	
	private static void toggleLighting(int toggle){
		toggle == 1 ? GL11.enable(GL_LIGHTING):GL11.disable(GL_LIGHTING);
	}
	
	private static void toggleDepth(int toggle){
		toggle == 1 ? GL11.enable(GL_DEPTH):GL11.disable(GL_DEPTH);
		toggle == 1 ? GL11.glDepthMask(true):GL11.glDepthMask(false);
	}
	
	private static void toggle2dTexture(int toggle){
		toggle == 1 ? GL11.enable(GL_TEXTURE_2D):GL11.disable(GL_TEXTURE_2D);
	}
	
	private static void toggleAlphaTest(int toggle){
		toggle == 1 ? GL11.enable(GL_ALPHA_TEST):GL11.disable(GL_ALPHA_TEST);
	}
	
	private static void operateMatrix(int toggle){
		if(toggle == 0){
			startMatrix();
		}
		toggleLighting(toggle);
		toggleDepth(toggle);
		toggle2dTexture(toggle);
		toggleAlphaTest(toggle);
		if(toggle == 1){
			endMatrix();
		}
	}
	
	private static void toggleBlend(int toggle){
		toggle == 1 ? GL11.enable(GL_BLEND):GL11.disable(GL_BLEND);
		toggle == 1 ? GL11.glBlendFunc(770, 771):null;
	}
	
	private static void attachMatrix(RenderingMob mob){
		GL11.glTranslatef(mob.getEntityX(), mob.getEntityY(), mob.getEntityZ());
	}
	
	private static void orientMatrixToPlayer(){
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
	}
	
	private static void scaleMatrix(float x, float y, float z){
		GL11.glScalef(x, y, z);
	}
	
	private static void setMatrixRGBA(float r, float g, float b, float a){
		GL11.glColor4f(r, g, b, a);
	}
	
	private static void normalize(){
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	}
	
	private static float hpBarScale = 0.026666673F;
	
	private static int enable = 1;
	private static int disable = 0;
	
	private static int start = 0;
	private static int end = 1;
	
	private static void displayHealthBar(RenderingMob mob){
		operateMatrix(start);
		toggleBlend(enable);
		attachMatrix(mob);
		orientMatrixToPlayer();
		scaleMatrix(-hpBarScale,-hpBarScale,hpBarScale);
		drawMobBar2(mob, hpBarColor())
		//drawCurrentHealthBar(mob,hpBarColor());
		operateMatrix(end);
	}
	
	
	order of operations
	rendering event is used because it only runs while the mob is being rendered.
	this might be a better way to track mobs.
	
	receive partialTicks event from main thread
	this is just a triggering event.
	
	
	
	filter events to mobs
	
	Can I do this from the living update event?
	
	
	
	
	
				//matrix for rendering currentHP, percentHP, maxHP
				//create a new matrix
				GL11.glPushMatrix();
				//scale
				GL11.glScalef(scale3, scale3, scale3);

				if (maxHpStr.endsWith(".0")){
					maxHpStr = maxHpStr.substring(0, maxHpStr.length() - 2);
				}

				if (hpStr.endsWith(".0")) {
					hpStr = hpStr.substring(0, hpStr.length() - 2);
				}

				if (showCurrentHP){
					mc.fontRenderer.drawString(
							hpStr, 
							2, 
							h, 
							16777215);
				}

				if (showMaxHP){
					mc.fontRenderer.drawString(
							maxHpStr,
							(int) (size / (scale2 * scale3) * 2.0F) - 2 - mc.fontRenderer.getStringWidth(maxHpStr), 
							h,
							16777215);
				}

				if (showPercentage){
					mc.fontRenderer.drawString(
							percStr,
							(int) (size / (scale2 * scale3)) - mc.fontRenderer.getStringWidth(percStr) / 2, 
							h, 
							-1);
				}
				//end drawing and revert
				GL11.glPopMatrix();
	
	
	https://www.opengl.org/documentation/
	http://pyopengl.sourceforge.net/documentation/pydoc/OpenGL.raw.GL.constants.html
	
	GL11.glDisable(GL11.GL_ALPHA_TEST);
	GL11.glEnable(GL11.GL_BLEND);
GL_ALPHA_TEST (0xBC0)
0xBC0 = 3008
GL_BLEND (0xBE2) 
0xBE2 = 3042

   | x == 0x0 = Zero
   | x == 0x1 = One
   | x == 0x300 = SrcColor
   | x == 0x301 = OneMinusSrcColor
   | x == 0x306 = DstColor
   | x == 0x307 = OneMinusDstColor
   | x == 0x302 = SrcAlpha //GL11.GL_SRC_ALPHA
   | x == 0x303 = OneMinusSrcAlpha //GL11.GL_ONE_MINUS_SRC_ALPHA
   | x == 0x304 = DstAlpha
   | x == 0x305 = OneMinusDstAlpha
