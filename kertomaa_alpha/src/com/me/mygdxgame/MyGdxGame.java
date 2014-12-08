package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
//import com.badlogic.gdx.utils.Timer;
//import com.badlogic.gdx.utils.Timer.Task;
import  com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import java.util.*;
//import java.lang.Math.*;

public class MyGdxGame implements ApplicationListener {
	//Debugging
    private boolean debug;
    private Box2DDebugRenderer debugRenderer;
	
	//Core objects
    private gamelogic game;
    private World world;
    private OrthographicCamera camera;
    
    //Textures, sprites, drawing etc.
    private SpriteBatch batch;
    private BitmapFont font;
    private TextureAtlas testdik;
    private Sprite sprite;
    private Sprite bgsprite;
    private Label titleLabel;
    
    //Lists and tables
    private LinkedList<Sprite> numbers;
    private int[] digits = new int[3];
    
    //Phasing, flags, counters, etc.
    private Phase phase;
    private gamePhase gPhase;
    private long roundtime;
    private boolean pressed=false;
    private float pxtom;
    private float WIDTH;
    private float HEIGHT;
    private Vector2 initial_click;
    
    @Override
    public void create() {   
    	//DEBUGGING??
    	debug=false;
    	debugRenderer= new Box2DDebugRenderer();
    	
    	//World settings
    	WIDTH=20; //Gdx.graphics.getWidth();
    	HEIGHT= 0.75f*WIDTH;
    	world = new World(new Vector2(0, -10), true);
    	world.step(1/60f, 6, 2);
    	pxtom=WIDTH/Gdx.graphics.getWidth();
    	
    	//Camera settings
    	camera = new OrthographicCamera(WIDTH, HEIGHT);
    	camera.setToOrtho(false, WIDTH, HEIGHT);
    	camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
    	camera.update();
    	
    	//Create game-object
        game = new gamelogic(1,world);
        game.set_pxtom(pxtom);
        
        //Manage drawing
    	batch = new SpriteBatch();
    	batch.setProjectionMatrix(camera.combined);
    	TextureAtlas bgAtlas= new TextureAtlas(Gdx.files.internal("data/bgsheet.atlas"));
        AtlasRegion bgregion = bgAtlas.findRegion("tausta1080");
		bgsprite = new Sprite(bgregion);
        bgsprite.setSize(WIDTH,HEIGHT);
        
        //Manage numbers and questions
        numbers = new LinkedList<Sprite>();    
        testdik = new TextureAtlas(Gdx.files.internal("data/numbers.atlas"));
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(6f*pxtom);
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        titleLabel = new Label("Points", style);
        titleLabel.setColor(Color.WHITE);
        
        //Set flags for game start and start listening collisions
        phase = Phase.GAME;
        gPhase=gamePhase.PREGAME;
        this.createCollisionListener();
        this.createContactFilter();
    }

    //This is very typical. Not sure what should be disposed though.
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    //Render function
    @Override
    public void render() {  
    	//Step physics simulation
    	world.step(1/60f, 6, 2);
    	
    	//Draw black when debugging
    	if (debug) {
			Gdx.gl.glClearColor(0, 0, 0, 10);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
    	
    	//Start sprite operations
        batch.begin();
        
        //Draw background when not debugging
        if (!debug)
			bgsprite.draw(batch);
        //Render debug (always - comment to turn off)
        debugRenderer.render(world, camera.combined);
        
    	if (phase == Phase.GAME) {
    		//Get current question
            String kyssari = game.get_q();
            //Move label and set the question
            titleLabel.setPosition(WIDTH*(1/20f), HEIGHT-1);
            titleLabel.setText(kyssari);
            titleLabel.draw(batch, 50);
    		
    		//This is a nice loop for drawing numbers
            	//Numbers should be object with click regions <- Actors
            	//This could be a larger loop for every non-physical draw
        	for (Sprite element : numbers) {
        		if (element != null && !debug) {
        			element.draw(batch);
        		}
        			
        	}
        	
        	//This is a nice loop for drawing everything else (or "elements")
        	for (Object element : game.get_elements()) {
        		if (element != null && !debug)
        		{
        			element.update(pxtom);
        			element.draw(batch);
        		}
        	}
        	
            
            //Different phases
        	
            //PREGAME does pregame setups
    		if (gPhase == gamePhase.PREGAME) {
    			//Create enemy!
    				//Notice!! Clearly some overhead when loading always a new sprite?
    			//Get texture
    	    	TextureAtlas ObjectAtlas= new TextureAtlas(Gdx.files.internal("data/mainsheet.atlas"));
    	        AtlasRegion region = ObjectAtlas.findRegion("kilppari");
    	        //Create sprite and object. Note!! Nowdays starting position is next to nothing... unless game object takes a hint of it.
    			sprite = new Sprite(region);
    			Object newguy = new Kilppari(1,100,100,-1,sprite);
    			//add to game.
    			game.add_enemy(newguy, 1);
    			
    			//Add protagonist and wall. Same problem with position
    			region = ObjectAtlas.findRegion("protagonist");
    			sprite = new Sprite(region);
    			float height=Gdx.graphics.getHeight()*0.50f;
    			newguy = new Player(10, 300, height, 0, sprite);
    			game.add_player(newguy);
    			
    			//Wall
    			region = ObjectAtlas.findRegion("muuri");
    			sprite = new Sprite(region);
    			newguy = new Object(5, 250, 100, 0, sprite);
    			newguy.setType("wall");
    			game.add_wall(newguy);
    			
    			//Set used ammo and change gamephase
    			game.set_weapon(Ammo.ORANGE);
    			//Round start time
    			roundtime=System.currentTimeMillis();
    			//
    			gPhase = gamePhase.NEWQ;
    		}
    		
    		//NEWQ requests and setups a new question
    		else if (gPhase == gamePhase.NEWQ)
    		{
    			//Request a new question
    			game.next_question();
    			
    			//Get answers
    			int[] ans;
    	        ans = game.get_answers();
    	        
    	        //Get how many digits each number has.. for drawing.
    	        for (int i=0;i<3;i++) {
    	        	int length = String.valueOf(ans[i]).length();
    	        	digits[i]=length;
    	        }
    	        
    	        //Draw numbers! Note! everything is indeed dynamically scaled even if it might not seem like that.
    	        
    	       
    	        //j calculates digits.
    	        int j=0;
    	        for (int i=0; i < 3; i++) {
    	        	//This determines a offset from the left side. Note: drawing is started from the 1/4 from the left at offset 0.
        	        int left_padding=-50;
    	        	//Here we make numbers digits (for example 10 becomes 1 and 0)
    	        	int number=ans[i];
    	        	LinkedList<Integer> stack = new LinkedList<Integer>();
    	        	while (number > 0) {
    	        	    stack.push( number % 10 );
    	        	    number = number / 10;
    	        	}
    	        	//Draw everything!
    	        	while (!stack.isEmpty()) {
    	        		//Take number to draw
    	        	    int r=stack.pop();
    	        	    //Find region and load sprite
    	        	    AtlasRegion region = testdik.findRegion(Integer.toString(r));
    	        		Sprite number1 = new Sprite(region);
    	        		//add to drawlist, scale and set to right position
    	        		numbers.add(number1);
    	        		numbers.get(j).setScale(pxtom);
    	        		numbers.get(j).setOrigin(sprite.getWidth()/2f, sprite.getHeight()/2f);
    	        		numbers.get(j).setPosition(WIDTH/4f*(i+1)+left_padding*pxtom, (6/9f)*HEIGHT);
    	        		//System.out.printf("Drawing to %f %f\n", left_padding*pxtom, 600*pxtom);
    	        		j+=1;
    	        		//This is spacing between numbers
    	        		left_padding+=80;
    	        	}
    	        }
    	        //Go to answering phase!
    			gPhase = gamePhase.ANSWERING;
    		}
    		
    		/////////////You are entering the poorly commented area
    		
    		else if (gPhase == gamePhase.ANSWERING) {
    			//answer me pls
    			int[] ans;
    	        ans = game.get_answers();
    	        int right = game.get_right();
    	        //numbers.add(new Sprite (region));
    	        //System.out.print ("nigger\n");
    	        
    	        //Should rethink flow of "pressing" cos it can trigger multiple actions dependent on position
    	        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
    	        	if (!(pressed))
    	        	   	initial_click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    	        	pressed=true;
    	  
    	        	//System.out.printf("%d %d\n", Gdx.input.getX(),  Gdx.input.getY());
    	        }
    			if(pressed && !(Gdx.input.isButtonPressed(Input.Buttons.LEFT))) {
    	    		int x=Gdx.input.getX();
    	    		int y=Gdx.input.getY();
    	    		if (y<210 && y>100) {
	    				pressed=false;
    	    		}
    	    		if (y<200 && y>80) {
    	    			int end1=285+digits[0]*80+100;
    	    			if (x>345 && x<end1) {
    	    				if(ans[0]==right) {
    	    					gPhase=gamePhase.SCORE;
    	    				}
    	    			}
    	    			int end2=592+digits[1]*80+100;
    	    			if (x>end1 && x<end2) {
    	    				if(ans[1]==right) {
    	    					gPhase=gamePhase.SCORE;
    	    				}
    	    				
    	    			}
    	    			int end3=904+digits[2]*80+100;
    	    			if (x>end2 && x<end3) {
    	    				if(ans[2]==right) {
    	    					gPhase=gamePhase.SCORE;
    	    				}
	
    	    			}
    	    		}
    	    		
    	    	}
    		}
    		else if (gPhase == gamePhase.SCORE) {
    			//give him those points!
    			//long start=System.currentTimeMillis();
    			while (!numbers.isEmpty()) {
    				numbers.pop();
    			}
    			//while (start-System.currentTimeMillis()<10) { }
    			gPhase=gamePhase.NEWQ;
    		}
    		if (game.get_ammo(game.get_weapon())>0) {
    			if (pressed && !(Gdx.input.isButtonPressed(Input.Buttons.LEFT))) {
    	    		double x=Gdx.input.getX();
    	    		double y=Gdx.input.getY();
    	    		//Gdx.input.
    	    		if (y>200) {
	    				pressed=false;
	    				float strength=1/10f;
	    				//double total = Math.sqrt();
	    				double speed_x=strength*(x-initial_click.x);
	    				double speed_y=strength*(-y+initial_click.y);
	    				//This is retarded to load the sheet every time
	    				TextureAtlas ProjectileAtlas= new TextureAtlas(Gdx.files.internal("data/mainsheet.atlas"));
	        	        AtlasRegion ProjectileRegion = ProjectileAtlas.findRegion("ammo");
	        			sprite = new Sprite(ProjectileRegion);
	    				Bullet newshit = new Bullet(0, 20, 100, (float) speed_x, (float) speed_y, sprite);
	    				game.add_projectile(newshit);
    	    		}
    			}
    			//nigga want to destroy some stuff
    		}
        batch.end();
    	}
    }
    

    private void createContactFilter() {
    	world.setContactFilter(new ContactFilter() {

            @Override
            public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
            	//should deal with bodies lol :D
               if (fixtureA.getUserData() instanceof Kilppari && fixtureB.getUserData() instanceof Bullet) {
            	  //return true;
                  return ((Bullet) fixtureB.getUserData()).should_collide();
               }
               if (fixtureB.getUserData() instanceof Kilppari && fixtureA.getUserData() instanceof Bullet) {
            	   //return true;
                  return ((Bullet) fixtureA.getUserData()).should_collide();
               }

               return true;
            }
         });
    }
    
    private void createCollisionListener() {
        world.setContactListener(new ContactListener() {
        	
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                if (fixtureA.getUserData()!=null) {
                	//System.out.print(((Object) fixtureA.getUserData()).getType() + "\n");
                }
                if (fixtureB.getUserData()!=null) {
                	//System.out.print(((Object) fixtureB.getUserData()).getType() + "\n");
                }
                //System.out.print(fixtureA.toString() + "\n");
                if (fixtureA.getUserData() instanceof Kilppari && ((Object) fixtureB.getUserData()).getType()=="wall")  {
                	((Object) fixtureA.getUserData()).getBody().setLinearVelocity(0.0f,0.0f);
                	//Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
                }
                else if (fixtureB.getUserData() instanceof Kilppari && ((Object) fixtureA.getUserData()).getType()=="wall")  {
                	((Object) fixtureA.getUserData()).getBody().setLinearVelocity(0.0f,0.0f);
                	//Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
                }
                
                if (fixtureA.getUserData() instanceof Kilppari && fixtureB.getUserData() instanceof Bullet) {
                	((Bullet) fixtureB.getUserData()).set_collide(false);
                	System.out.print("Kilppariosuma" + fixtureA.toString() + "\n");
                }
                else if (fixtureB.getUserData() instanceof Kilppari && fixtureA.getUserData() instanceof Bullet) {
                	System.out.print("Kilppariosuma" + fixtureA.toString() + "\n");
                	((Bullet) fixtureA.getUserData()).set_collide(false);
                }
            }

            @Override
            public void endContact(Contact contact) {
                //Fixture fixtureA = contact.getFixtureA();
                //Fixture fixtureB = contact.getFixtureB();
                //Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
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