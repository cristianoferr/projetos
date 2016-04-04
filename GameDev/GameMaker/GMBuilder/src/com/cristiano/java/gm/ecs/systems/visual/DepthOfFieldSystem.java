package com.cristiano.java.gm.ecs.systems.visual;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.visual.DepthOfFieldComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.renderer.Camera;

public class DepthOfFieldSystem extends BuilderAbstractSystem {

	public DepthOfFieldSystem() {
		super(GameComps.COMP_DEPTH_OF_FIELD);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		updateFilter((DepthOfFieldComponent) component);
	}

	private void updateFilter(DepthOfFieldComponent component) {

		Camera cam = game.getCamera();
		Vector3f origin = cam.getWorldCoordinates(new Vector2f(
				game.getWidth() / 2, game.getHeight() / 2), 0.0f);
		Vector3f direction = cam.getWorldCoordinates(
				new Vector2f(game.getWidth() / 2, game.getHeight() / 2), 0.3f);
		direction.subtractLocal(origin).normalizeLocal();
		Ray ray = new Ray(origin, direction);
		CollisionResults results = new CollisionResults();
		int numCollisions = game.getRootNode().collideWith(ray, results);
		if (numCollisions > 1) {
			CollisionResult hit = results.getClosestCollision();
			float distance = hit.getDistance();
			((DepthOfFieldFilter) component.filter).setFocusDistance(distance/10);
		}
	}

}
