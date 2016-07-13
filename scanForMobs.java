	
	
	private HashMap<String, StoredMob> storedMobs = new HashMap<String, EntityLivingBase>();
	private HashSet<String> worldMobs = new HashSet<String>();
	
	public static void scanForMobs(){
		worldMobs.clear();
		for (Entity entity : getWorldEntitiesList()){
			if ((entity != null) && (entity instanceof EntityLiving)){
				String mobKey = getKey(entity);
				worldMobs.add(mobKey);
				//not stored and not dead: store
				if(!storedList.containsKey(mobKey) && (!isMobDead(entity))) {
					storedMobs.put(mobKey, new StoredMob((EntityLivingBase)entity,mobKey));
				//stored and not dead or dead: process then remove if dead
				}else if(storedList.containsKey(mobKey)){
					processMob(mobKey);
				}
			}
		}
		//if storedMobs has more keys then worldMobs then only keep what's in the world
		storedMobs.keySet().retainAll(worldMobs);
	}
	
	@SuppressWarnings("unchecked")
	private static Set<Entity> getWorldEntitiesList(){
		return (Set<Entity>) ReflectionHelper.getPrivateValue(WorldClient.class,Minecraft.getMinecraft().theWorld,new String[] {"entityList","field_73032_d","J" });
	}
	
	private static boolean isMobDead(Entity entity){
		return(MathHelper.ceiling_float_int(entity.getHealth()) > 0)
	}
		
	private static String getKey(Entity entity){
		return entity.getCommandSenderName() + "/" + entity.getEntityId();
	}
	
	public synchronized static getStoredMob(String mobKey){
		return storedMobs.get(mobKey);
	}
