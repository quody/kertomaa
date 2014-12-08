package com.me.mygdxgame;

import java.util.*;
import java.lang.Math;
import  com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class gamelogic {
	//Simple variables
	private int points;
	private int difficulty;	
	private Ammo weapon;
	
	//CREATING BOX2D WORLD AND OBJECTS
    private World world = new World(new Vector2(0,-10), true);
    
	//Data structures
	private int[] products;
	private int[] answers=new int[3];
	private Dictionary<Ammo,Integer> ammo = new Hashtable<Ammo, Integer>();
    private List<Object> elements = new LinkedList<Object>();
    private List<Bullet> projectiles = new LinkedList<Bullet>();
    private float pxtom;
	//Constructor
	public gamelogic(int diff, World world1) {
		world=world1;
		points = 0;
		difficulty = diff;
		//int[] answers = new int[3];
		//Create weapons
		this.set_weapon(Ammo.ORANGE);
		ammo.put(Ammo.ORANGE,100);
		this.next_question();
		this.add_ground();
		//this.add_player();
		//this.add_wall();

	}
	
	public void add_ground() {
		//CREATE BOX2D WORLD AND PUT STUFF IN AND DO SETTINGS
				BodyDef groundBodyDef= new BodyDef();
				groundBodyDef.position.set(new Vector2(0,0));
				Body groundBody = world.createBody(groundBodyDef);
				// Create a polygon shape
				PolygonShape groundBox = new PolygonShape();  
				// Set the polygon shape as a box which is twice the size of our view port and 20 high
				// (setAsBox takes half-width and half-height as arguments)
				groundBox.setAsBox(2000f, 1.0f);
				// Create a fixture from our polygon shape and add it to our ground body  
				groundBody.createFixture(groundBox, 0.0f); 
				// Clean up after ourselves
				groundBox.dispose();
	}
	
	public void add_player(Object enemy) {
		//CREATE BOX2D WORLD AND PUT STUFF IN AND DO SETTINGS
		
		BodyDef groundBodyDef= new BodyDef();
		groundBodyDef.position.set(new Vector2(0,1));
		Body groundBody = world.createBody(groundBodyDef);
		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(0.01f, 0.01f);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
		
		elements.add(enemy);
		enemy.setBody(groundBody);
	}
	
	public void add_wall(Object wall) {
		//CREATE BOX2D WORLD AND PUT STUFF IN AND DO SETTINGS
		BodyDef groundBodyDef= new BodyDef();
		//groundBodyDef.type = BodyDef.BodyType.DynamicBody;
		groundBodyDef.position.set(new Vector2(4f,2.2f));
		Body groundBody = world.createBody(groundBodyDef);
		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		//groundBox.setAsBox(2000f, 1.0f);
		Vector2[] vertices = {new Vector2(-58*pxtom,-62*pxtom), new Vector2(-71*pxtom,-30*pxtom), 
				new Vector2(-46*pxtom,27*pxtom), new Vector2(1*pxtom,93*pxtom), 
				new Vector2(15*pxtom,93*pxtom), new Vector2(41*pxtom,44*pxtom),
				new Vector2(69*pxtom,3*pxtom),
				new Vector2(77*pxtom, -67*pxtom)};
		groundBox.set(vertices);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
		elements.add(wall);
		//wall.setBody(groundBody);
		
		//Create sensor to same body! imba!!! not worker =(
		groundBodyDef= new BodyDef();
		//groundBodyDef.type = BodyDef.BodyType.DynamicBody;
		groundBodyDef.position.set(new Vector2(4f,2.2f));
		groundBodyDef.type = BodyDef.BodyType.DynamicBody;
		groundBody = world.createBody(groundBodyDef);
		// Create a polygon shape
		groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		//groundBox.setAsBox(2000f, 1.0f);
		Vector2[] vertices2 = {new Vector2(-58*pxtom,-62*pxtom), 
				new Vector2(69*pxtom,3*pxtom),
				new Vector2(77*pxtom, -67*pxtom)};
		groundBox.set(vertices2);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f);
		(groundBody.getFixtureList()).get(0).setSensor(true);
		groundBody.setGravityScale(0);
		groundBody.setSleepingAllowed(false);
		// Clean up after ourselves
		groundBox.dispose();
		(groundBody.getFixtureList()).get(0).setUserData(wall);
		wall.setBody(groundBody);
		//Set sensor!? or no
		
	}
	
	public List<Object> get_elements() {
		return elements;
	}
	
	public void add_element(Object enemy, int shape) {
		BodyDef enemyBodyDef= new BodyDef();
		//it's static!
		//enemyBodyDef.type = BodyDef.BodyType.KinematicBody;
		enemyBodyDef.position.set(new Vector2(20, 2));
		Body enemyBody = world.createBody(enemyBodyDef);
		if (shape==1) {
			PolygonShape groundBox = new PolygonShape(); 
			groundBox.setAsBox(345*pxtom, 168*pxtom);
			enemyBody.createFixture(groundBox, 0.0f); 
			groundBox.dispose();
		}
		
	
	}
	
	public void add_enemy(Object enemy, int shape) {
		BodyDef enemyBodyDef= new BodyDef();
		enemyBodyDef.type = BodyDef.BodyType.KinematicBody;
		enemyBodyDef.position.set(new Vector2(20, 108*pxtom+0.5f));
		Body enemyBody = world.createBody(enemyBodyDef);
		if (shape==1) {
			PolygonShape groundBox = new PolygonShape(); 
			//groundBox.setAsBox(345*pxtom/2, 168*pxtom/2, new Vector2(0, 0), 0);
			Vector2[] vertices = {new Vector2(-39.6f*pxtom, -5.28f*pxtom), new Vector2(-89.2f*pxtom, 17.16f*pxtom), new Vector2(-78.64f*pxtom, 50.16f*pxtom), new Vector2(-46.2f*pxtom, 54.12f*pxtom), new Vector2(-26.4f*pxtom, 17.16f*pxtom)};
			groundBox.set(vertices);
			enemyBody.createFixture(groundBox, 1.0f); 
			Vector2[] vertices2= {new Vector2(-56.96f*pxtom, -62.04f*pxtom), new Vector2(-55.44f*pxtom, -21.12f*pxtom), new Vector2(-18.48f*pxtom, 23.76f*pxtom), new Vector2(10.56f*pxtom, 23.76f*pxtom), new Vector2(38.28f*pxtom, 3.96f*pxtom), new Vector2(59.4f*pxtom, -23.76f*pxtom), new Vector2(44.88f*pxtom, -62.04f*pxtom)};
			groundBox.set(vertices2);
			enemyBody.createFixture(groundBox, 1.0f); 
			groundBox.dispose();
		}
		enemyBody.setLinearVelocity(-1.0f,0.0f);
		elements.add(enemy);
		(enemyBody.getFixtureList()).get(0).setUserData(enemy);
		(enemyBody.getFixtureList()).get(1).setUserData(enemy);
		enemy.setBody(enemyBody);
	}
	
	public List<Bullet> get_projectiles() {
		return projectiles;
	}
	
	public void add_projectile(Bullet projectile) {
		elements.add(projectile);
		
		//add ball tho box2d
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(1.3f, 3.8f);

		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		//body.applyLinearImpulse(projectile.get_speed(), new Vector2(0,0), true);
		body.setLinearVelocity(projectile.get_speed());
		body.setAngularVelocity((float) ((Math.random()-0.5)*20));

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(0.3f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 10f; 
		fixtureDef.friction = 10f;
		fixtureDef.restitution = 0.1f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(projectile);
		

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
		projectile.setBody(body);
		//ball created
	}
	
	public void remove_projectile(Bullet projectile) {
		projectiles.remove(projectile);
	}
	
	public void remove_element(Object element) {
		elements.remove(element);
	}
	
	public void remove_object(Bullet projectile) {
		projectiles.remove(projectile);
	}
	
	public void remove_object(Object element) {
		elements.remove(element);
	}
	
	//move ali
	
	//THESE MOVES WOULD be ultimately combined to be a "render" and from there their position
	//would simply be copied from the box2d
	public void move_enemies() {
		for (Object element : elements) {
			element.move();
		}
	}
	
	public void move_projectiles() {
		step();
		for (Object projectile : projectiles) {
			double old_x=projectile.get_x();
			double old_y=projectile.get_y();
			projectile.move();
			double new_x=projectile.get_x();
			double new_y=projectile.get_y();
			//Now detect collisions!
			int max_step=5;
			double dif_x=Math.abs(old_x-new_x);
			double dif_y=Math.abs(old_y-new_y);
			double steps=Math.max(dif_x,dif_y);
			/*for (int i=0; i<steps; i++) {
				double x=old_x+(dif_x*i)/steps;
				double y=old_y+(dif_y*i)/steps;
				for ()
			}*/
			
		}
	}
	
	//public int getpoints();
	
	//public void next_question(int difficulty);
	public void next_question() {
		Random rand = new Random();
		int n1 = rand.nextInt(10)+1;
		int n2 = rand.nextInt(10)+1;
		products = new int[2];
		products[0] = n1;
		products[1] = n2;
		int result = n1*n2;
		//int[] answers = new int[3];
		generate_answers(n1,n2);
	}
	
	public void set_pxtom(float dik) {
		pxtom=dik;
	}
	
	//ShuffleArray mb this isnt the place for this...
	 static void shuffleArray(int[] ar)
	 {
	   Random rnd = new Random();
	   for (int i = ar.length - 1; i > 0; i--)
	   {
	     int index = rnd.nextInt(i + 1);
	     // Simple swap
	     int a = ar[index];
	     ar[index] = ar[i];
	     ar[i] = a;
	   }
	 }
	
	public void generate_answers(int n1, int n2) {
		int n_methods = 1;
		Random rand= new Random();
		int choice=rand.nextInt(n_methods);
		//int[] answers = new int[3];
		if (choice == 0) {
			answers[0] = n1*n2-1;
			answers[1] = n1*n2+1;
			answers[2] = n1*n2;
			shuffleArray(answers);
		}
	}
	 //public string getquestion
	public String get_q() {
		//"%d * %d ?"
		String kysymys = String.format("123456789", products[0], products[1]); 
		return kysymys;
	}
	
	public int[] get_answers() {
		return answers;
	}
	//public int[] get_answers
	public int get_right() {
		return products[0]*products[1];
	}
	
	public int get_ammo(Ammo type) {
		//again could use different types
		return ammo.get(type);
	}
	
	
	public void add_ammo(Ammo type, int amount) {
		//different types?? i dunno
		int old=ammo.get(type);
		ammo.put(type, old+amount);
	}
	
	public void set_weapon(Ammo type) {
		weapon=type;
	}
	
	public void step() {
		world.step(1/45f, 6, 2);
	}
	
	public Ammo get_weapon() {
		return weapon;
	}
	//public int get_right_answer
	
}
