package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;
//import java.math.*;
import com.badlogic.gdx.math.Vector2;

//Ammuksien luokka.
public class Bullet extends Object {
	private float speed_x;
	private float speed_y;
	//Tän muuttujan avulla törmäily voidaan ottaa pois päältä. Ideana on että ammus törmäis vaan kerran vihuun (ellei oo erikoispomppiva-ammus).
	private boolean should_collide;
	public Bullet(int starthp, float startx, float starty, float startspeedx, float startspeedy, Sprite sprite) {
		super(starthp, startx, starty, startspeedx, sprite);
		speed_x=startspeedx;
		speed_y=startspeedy;
		should_collide=true;
	}
	
	//C:n avulla ilmanvastus. Muuten liikkuu normisti. 
	public void move() {
		float C=0.99f;
		float gravity=5;
		speed_x=speed_x*C;
		speed_y=speed_y*C-gravity;
		x +=speed_x;
		y +=speed_y;
		
		
		
	}
	
	public void set_collide(boolean setting) {
		should_collide=setting;
	}
	
	public boolean should_collide() {
		return should_collide;
	}
	public Vector2 get_speed() {
		return new Vector2(speed_x, speed_y);
	}
	
	//en ees tiiä mitä tän pitäs tehä.
	public boolean hit() {
		return true;
	}

}
