package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture bgtext;
    private Sprite bgsprite;
    private gamelogic game;
    private Phase phase;
    
    @Override
    public void create() {        
    	batch = new SpriteBatch();
        bgtext = new Texture(Gdx.files.internal("data/tausta1080.jpg"));
        bgsprite = new Sprite(bgtext);
        game = new gamelogic(1);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(5);
        phase = Phase.GAME;
        
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render() {      
    	if (phase == Phase.GAME) {
    	//pull data from game? mb these should be class variables, stupid to execute on every frame
        int[] ans;
        ans = game.get_answers();
        int right = game.get_right();
    	//Getting input
    	/*if Gdx.input.isButtonPressed(Input.Buttons.LEFT)
    	
    	{
    		if 
    	}*/
    	//String dik = String.format("X: %d Y: %d", Gdx.input.getX(), Gdx.input.getY());
    	//System.out.println(dik);
    	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
    		int x=Gdx.input.getX();
    		int y=Gdx.input.getY();
    		if (y>280 && y<355) {
    			if(x>495 && x<600) {
    				String dik = String.format("%d %d", ans[0], right);
    				System.out.println(dik);
    				if(ans[0]==right) {
        				System.out.println("dik");
    					game.next_question();
    				}
    			}
    			else if(x>677 && x<780) {
    				System.out.println("dikkl");
    				if(ans[1]==right)
    					game.next_question();
    			}
    			else if(x>848 && x<950) {
    				String dik = String.format("%d %d", ans[0], right);
    				System.out.println("dikl");
    				if(ans[2]==right)
    					game.next_question();
    			}
    		}
    		
    	}
    	//Drawing phase
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        //String test = String.format("test %s", "testijuu");
        String kyssari = game.get_q();
        batch.begin();
        bgsprite.draw(batch);
        //In relation to device dimensions??
        bgsprite.setPosition(1920-2048+128, 1080-2048);
        //these also?=?????????!?!?!!??
        font.draw(batch, kyssari, 100, 800);
        //int[] answers = new int[3];
        String ans_string = String.format("%d     %d     %d", ans[0], ans[1], ans[2]);
        //gief dynamic placing.. -.- omg :D
        font.draw(batch, ans_string, 500, 800);
        batch.end();
    	}
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}