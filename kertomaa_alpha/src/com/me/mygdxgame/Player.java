package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;

//Pelaaja luokka. Vielä käytännössä sama ku Objecti.
public class Player extends Object {
	public Player(int starthp, float startx, float starty, float startspeed, Sprite sprite) {
		super(starthp, startx, starty, startspeed, sprite);
	}
}
