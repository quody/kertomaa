package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import  com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;
import java.lang.Math.*;

public class Object {
	protected int hp;
	protected float x;
	protected float y;
	protected float speed;
	protected Sprite sprite;
	protected Body body;
	protected float pxtom;
	protected float o_x;
	protected float o_y;
	private String type;
	
	public Object(int starthp, float startx, float starty, float startspeed, Sprite newsprite) {
		hp=starthp;
		x=startx;
		y=starty;
		speed=startspeed;
		sprite=newsprite;
		o_x=sprite.getWidth();
		o_y=sprite.getHeight();
		type = "none";
	}
	
	public void update(float pxtom) {
		Vector2 pos=body.getPosition();
		float rotation = body.getAngle();
		x=pos.x;
		y=pos.y;
		sprite.setSize(o_x*pxtom, o_y*pxtom);
		sprite.setOrigin( sprite.getWidth()/2f, sprite.getHeight()/2f);
		if (!(this instanceof Player))
			sprite.setPosition(x-sprite.getWidth()/2, y-sprite.getHeight()/2);
		else
			sprite.setPosition(x, y);
		sprite.setRotation(rotation*MathUtils.radiansToDegrees);
	}
	
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void setPxtom(float value) {
		pxtom=value;
	}
	public void setBody(Body body1) {
		body=body1;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void setType(String type1) {
		type=type1;
	}
	
	public String getType() {
		return type;
	}
	public float get_x() {
		return x;
	}
	
	public float get_y() {
		return y;
	}
	
	public void move() {
		x+=speed;
	}
	
	public Sprite get_sprite() {
		return sprite;
	}
}

