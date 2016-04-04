package com.Space.controller.ships;

import com.jme.math.Vector3f;

public class BasicShipData {
    // You can use a model of yourself here, but remember to calibrate tha CHASSIS_SCALE properly.
    public static final String CHASSIS_MODEL = "resources/Devilfish/Devilfish.jme";
    public static final float CHASSIS_MASS = 1000;
    public static final Vector3f CHASSIS_SCALE = new Vector3f( 7, 6.5f, 7 );


    public static final Vector3f FRONT_SUSPENSION_OFFSET = new Vector3f( 10, -1.75f, 3.25f );
    public static final Vector3f REAR_SUSPENSION_OFFSET = new Vector3f( -9.5f, -1.75f, 3.25f );

    // The fancy smoke effect. Made with Ren's particle editor.
    public static final String SMOKE_MODEL = "smoke.jme";
    // If you change the chassis model, you'll want to calibrate this offset also.
    public static final Vector3f SMOKE_OFFSET = new Vector3f( 0,5f, -50f );

}
