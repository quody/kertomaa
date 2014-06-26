package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "kertomaa_alpha";
		cfg.useGL20 = false;
		cfg.width = 1920;
		cfg.height = 1080;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
