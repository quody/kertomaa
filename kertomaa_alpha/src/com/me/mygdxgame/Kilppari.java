package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Kilppari extends Object {
	private Animation animation;
	private float time=0;
	
	public void update(float pxtom) {
		time += Gdx.graphics.getDeltaTime();
		Vector2 pos=body.getPosition();
		float rotation = body.getAngle();
		x=pos.x;
		y=pos.y;
		sprite.setRegion(animation.getKeyFrame(time, true));
		sprite.setSize(o_x*pxtom, o_y*pxtom);
		sprite.setOrigin( sprite.getWidth()/2f, sprite.getHeight()/2f);
		sprite.setPosition(x-sprite.getWidth()/2, y-sprite.getHeight()/2);

		sprite.setRotation(rotation*MathUtils.radiansToDegrees);
	}
	
	
	public Kilppari(int starthp, float startx, float starty, float startspeed, Sprite sprite) {
		super(starthp, startx, starty, startspeed, sprite);
		TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("data/konnasheet.atlas"));
		animation = new Animation(1/15f, textureAtlas.getRegions());
	}
}
