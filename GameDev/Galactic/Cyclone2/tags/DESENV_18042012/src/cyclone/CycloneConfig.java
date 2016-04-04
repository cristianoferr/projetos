package cyclone;

import cyclone.collide.CollisionSolverEnum;

public abstract class CycloneConfig {
	public static final float MAX_UPDATE_TIME=0.15f;	
	
	private static CollisionSolverEnum collisionSolver=CollisionSolverEnum.BVH;
	private static boolean useGravity=false;//aplica Gravidade direcional em todos os corpos
	private static boolean useFloor=false;
	private static double friction=0.5; //fricção... preciso verificar isso
	private static double restitution=0.1; //Quanto de energia o movimento perde ao colidir
	private static double tolerance=0.1;
	private static int internalPointsEixo=15; //Quantidade de internal points que serao criados e testados por eixo (n*n*n)
	private static boolean applyGravity=false; //Corpos astronomicos emitem gravidade (planetas/estrelas/etc)
	private static double polygonRotationFix=0.9985; //Forma feia de resolver o problema das naves girando demais (aplicar "fricção" na rotação)// 1=no fix
	
	
    /**
     * Holds the number of iterations to perform when resolving
     * velocity.
     */
	private static int velocityIterations=4;

    /**
     * Holds the number of iterations to perform when resolving
     * position.
     */
	private static int positionIterations=4;
    
    
	private static final double SLEEP_EPSILON=0.001;
    /**
     * Holds the amount of damping applied to linear
     * motion.  Damping is required to remove energy added
     * through numerical instability in the integrator.
     */
	private static double linearDamping=1;

    /**
     * Holds the amount of damping applied to angular
     * motion.  Damping is required to remove energy added
     * through numerical instability in the integrator.
     */
	private static double angularDamping=0.99;

	private static double rotationFix=0.5;

    /**
     * Sets the number of iterations for each resolution stage.
     */
    public static void setIterations(int v,
                       int p){
        velocityIterations = v;
        positionIterations = p;
    }
    
    /**
     * Sets the number of iterations for both resolution stages.
     */
    public static void setIterations(int iterations){
        setIterations(iterations, iterations);
    }
	
	public static CollisionSolverEnum getCollisionSolver() {
		return collisionSolver;
	}

	public static boolean isUsingGravity() {
		return useGravity;
	}

	public static boolean isUseFloor() {
		return useFloor;
	}

	public static double getFriction() {
		return friction;
	}

	public static double getRestitution() {
		return restitution;
	}

	public static double getTolerance() {
		return tolerance;
	}

	public static int getInternalPointsEixo() {
		return internalPointsEixo;
	}

	public static boolean isApplyGravity() {
		return applyGravity;
	}

	public static double getPolygonRotationFix() {
		return polygonRotationFix;
	}

	public static double getSleepepsilon() {
		return SLEEP_EPSILON;
	}

	public static double getLinearDamping() {
		return linearDamping;
	}

	public static double getAngularDamping() {
		return angularDamping;
	}

	public static void setCollisionSolver(CollisionSolverEnum collisionSolver) {
		CycloneConfig.collisionSolver = collisionSolver;
	}

	public static double getRotationFix() {
		return rotationFix;
	}

	public static void setUseGravity(boolean useGravity) {
		CycloneConfig.useGravity = useGravity;
	}

	public static void setUseFloor(boolean useFloor) {
		CycloneConfig.useFloor = useFloor;
	}

	public static void setVelocityIterations(int velocityIterations) {
		CycloneConfig.velocityIterations = velocityIterations;
	}

	public static int getVelocityIterations() {
		return velocityIterations;
	}

	public static void setPositionIterations(int positionIterations) {
		CycloneConfig.positionIterations = positionIterations;
	}

	public static int getPositionIterations() {
		return positionIterations;
	}
}
