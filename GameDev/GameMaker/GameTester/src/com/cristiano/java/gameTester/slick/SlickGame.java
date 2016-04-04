package com.cristiano.java.gameTester.slick;


import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.cristiano.java.blueprintManager.ElementManager;
import com.cristiano.java.blueprintManager.entidade.GenericElement;
import com.cristiano.java.blueprintManager.extras.Extras;
import com.cristiano.math.Vector3;

public class SlickGame extends BasicGame{
	ArrayList<SlickEntity> gameEntities;
	ElementManager em;
	public static boolean debug_names=false;
	public static boolean debug_names_components=false;
	public static boolean debug_draw_line=false;
	public static boolean debug_draw_points=false;
	public static int debug_tamanho_tela=3; //1 a 3
	
	public final int MAX_TRIES=5; //tentativas maximas de geracao de entidades caso nao sejam validas
	
	public SlickGame() throws SlickException, IOException
    {
        super("Slick game");
        inicializa();
        
		//parteShape2.rotateBy(180);
    }

	private void inicializa() throws IOException {
		gameEntities=new ArrayList<SlickEntity>();
        em=new ElementManager();
        em.loadBlueprintsFromFile(Extras.BLUEPRINTS_PATH);
        
        int x=0;
        int y=0;
        if (SlickGame.debug_tamanho_tela==1){
        	x=90;
        	y=x;
        }
        if (SlickGame.debug_tamanho_tela==2){
        	x=250;
        	y=250;
        }
        if (SlickGame.debug_tamanho_tela==3){
        	x=450;
        	y=500;
        }
		
        	
       adicionaEntityFromTag("air ready blueprint",x,y);
      //adicionaEntityFromTag("visual test",x,y);
        System.out.println("");
        em.printDebug();
        System.out.println("");
	}

	private void adicionaEntityFromTag(String tag,int atX,int atY) {
		
		SlickEntity entity=null;
		int tries=0;
		do {
			entity = criaNovaEntidade(tag, atX, atY);
			System.out.println("Valido? "+entity.isValid()+" try:"+tries );
			tries++;
			entity.kill();
		} while ((tries<MAX_TRIES)&&(!entity.isValid()));
		
		if (entity.isValid())
			gameEntities.add(entity);
		
		
		/*entity=new GTEntity(ge,new Vector3(95,195));
		entity.setName("nave peq");
		entity.inicializaShape(50, 50);
		entity.atualizaGameShape();
		gameEntities.add(entity);*/
	}

	private SlickEntity criaNovaEntidade(String tag, int atX, int atY) {
		GenericElement ge = (GenericElement)em.createFinalElementFromTag(tag);
		
		//System.out.println("GE:"+ge);
		SlickEntity entity=new SlickEntity(ge,new Vector3(atX,atY));
		entity.setName("nave");
		 if (SlickGame.debug_tamanho_tela==1)
			 entity.inicializaShape(50, 50);
		 if (SlickGame.debug_tamanho_tela==2)
			 entity.inicializaShape(150, 150);
		 if (SlickGame.debug_tamanho_tela==3)
			 entity.inicializaShape(350, 350);
		entity.atualizaGameShape();
		return entity;
	}

	
    public static void main(String[] arguments) throws IOException
    {
        try
        {
            AppGameContainer app = new AppGameContainer(new SlickGame());
            if (SlickGame.debug_tamanho_tela==1)
              app.setDisplayMode(200, 200, false);
            if (SlickGame.debug_tamanho_tela==2)
            	app.setDisplayMode(500, 500, false);
            if (SlickGame.debug_tamanho_tela==3)
            	app.setDisplayMode(1200, 1000, false);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
 
    @Override
    public void init(GameContainer container) throws SlickException
    {
    	
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {
    	
    	for (SlickEntity entity : gameEntities) {
    		
			entity.update(delta/1000);
		}

    	
    	Input input = container.getInput();
    	if (input.isKeyPressed(Input.KEY_D)){
    		debug_names=!debug_names;
    	}
    	if (input.isKeyPressed(Input.KEY_F)){
    		debug_draw_line=!debug_draw_line;
    	}
    	if (input.isKeyPressed(Input.KEY_P)){
    		debug_draw_points=!debug_draw_points;
    	}
    	
    	if (input.isKeyPressed(Input.KEY_C)){
    		debug_names_components=!debug_names_components;
    	}
    	
    	
    	if (input.isKeyPressed(Input.KEY_ESCAPE)){
    		container.exit();
    	}
    	
    	if (input.isKeyPressed(Input.KEY_R)){
    		try {
				inicializa();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    }
 
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	for (SlickEntity entity : gameEntities) {
    		
			entity.draw(g);
		}

    }
}
