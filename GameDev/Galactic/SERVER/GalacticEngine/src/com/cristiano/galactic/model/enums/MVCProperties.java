package com.cristiano.galactic.model.enums;

/**
 * Strings ainda utilizadas pelo MVC para atualizar os clientes...
 * 
 * @author cmm4
 *
 */
//TODO: estudar eventual remoção dessas strings
public abstract class MVCProperties {
	public static final String EQUIPPED_WARE_PROPERTY = "EQUIPPED_WARE";
	
//  Properties this controller expects to be stored in one or more registered models
	public static final String ORBIT_ENT_PROPERTY="OrbitObj";
	
    public static final String MASS_PROPERTY = "Mass";
    
    public static final String TEXTURE_PROPERTY = "Texture";
    
    public static final String COORD_PROPERTY = "Coord";
    
    public static final String ROTATION_PROPERTY = "Rotation";
    
    public static final String ORIENTATION_PROPERTY = "Orientation";
    
    public static final String VELOCITY_PROPERTY = "Velocity";
    
    public static final String SHIPWARE_PROPERTY = "ShipWare";

    public static final String TYPE_PROPERTY = "Type"; 
    
    public static final String WORLD_PROPERTY = "World";
    
    public static final String COMMAND_PROPERTY = "Command";
    
    /**
     * Constant for firing property change events for the 'items' links.
     */
    public static final String ITEMS_PROPERTY = "items";

	public static String NAME_PROPERTY="Name";
}
