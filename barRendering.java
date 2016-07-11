


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
	
	setupMatrix(getEntityX(),getEntityY(),getEntityZ());
	
	private static void setupMatrix(float entityX,float entityY,float entityZ){
		//starts a matrix
		GL11.glPushMatrix();
		//values are x,y,z  moves matrix on a vector
		GL11.glTranslatef(entityX, entityY, entityZ);
	
	}
	
	
	
