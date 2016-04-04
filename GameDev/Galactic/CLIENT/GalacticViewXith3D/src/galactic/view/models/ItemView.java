package galactic.view.models;


import galactic.model.wares.ModelData;
import galactic.view.IView;
import galactic.view.View;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jagatoo.util.errorhandling.IncorrectFormatException;
import org.jagatoo.util.errorhandling.ParsingException;
import org.openmali.vecmath2.Colorf;
import org.xith3d.loaders.models.Model;
import org.xith3d.loaders.models.ModelLoader;
import org.xith3d.scenegraph.AmbientLight;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.PointLight;
import org.xith3d.scenegraph.RenderingAttributes;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.TransparencyAttributes;
import org.xith3d.scenegraph.primitives.Box;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.selection.Selectable;

import cristiano.math.Vector3;
import cyclone.entities.geom.Geom;
import cyclone.entities.geom.Geom.PrimitiveType;
import cyclone.entities.geom.GeomBox;
import cyclone.entities.geom.GeomSphere;
import cyclone.xith3d.CycloneBoundingBoxSelectable;

public abstract class ItemView extends AbstractItemView {
TransformGroup group=null;
Transform3D transform = new Transform3D();
//boolean added=false;
Model model=null; 
boolean flagDepth=false;

Shape3D objVisual=null;

int flagScaled=0;

	public ItemView(IView view){
		super(view);
	}
	
	/* (non-Javadoc)
	 * @see galactic.view.models.IItemView#getModel()
	 */
	public Model getModel() {
		return model;
	}

	
	
	
	
	/*public Rectangle addText(){
		return TextBillboard.createFixedWidth(
                10.0f,
                item.getName(),
                new Colorf( 0.55f, 1.0f, 0.8f ),
                new Font( "Verdana", Font.PLAIN, 12 ),
                TextRectangle.TEXT_ALIGNMENT_HORIZONTAL_CENTER
                );
	}*/
	
	
	public TransformGroup createSphere(String texture,double radius){
		if (objVisual==null){
//		System.out.println("RADIUS:"+radius);
			int divs=16;
			if (radius>100000) divs=200;
			if (texture!=null)
				objVisual = new Sphere( (float)radius, divs, divs,texture);
			else
				objVisual = new Sphere( (float)radius, divs, divs,new Colorf((float)Math.random()*1,(float)Math.random()*1,(float)Math.random()*1));
			objVisual.setName( name );
			objVisual.setUserData( Selectable.class, new CycloneBoundingBoxSelectable<Node>( objVisual ) );
		}
		
		TransformGroup group = new TransformGroup( 0, 0f, 0f );
		group.addChild( objVisual );
		return group;
	}
	
	
	public TransformGroup createBox(String texture,Vector3 halfSize) {
		if (objVisual==null){
			objVisual=new Box((float)halfSize.x*2,(float)halfSize.y*2,(float)halfSize.z*2,texture);
			//objVisual.setName( name );
			objVisual.setUserData( Selectable.class, new CycloneBoundingBoxSelectable<Node>( objVisual ) );
		}
		TransformGroup group = new TransformGroup( 0, 0f, 0f );
		group.addChild( objVisual );
		return group;
	}
	
	
	
	public void createVisualNodeFromGeom(Geom geom){
		if (geom.type==PrimitiveType.SPHERE)group=createSphere(texture,((GeomSphere)geom).radius);
		if (geom.type==PrimitiveType.BOX)group=createBox(texture,((GeomBox)geom).halfSize);
		view.backLinkVisualItem(group,this,true);
		view.backLinkVisualItem(objVisual,this,false);
		
		flagDepth=true;
		
	}
	
	
	/* (non-Javadoc)
	 * @see galactic.view.models.IItemView#createVisualRepresentation(galactic.model.EntityData.ModelData)
	 */
	@Override
	public void loadModel(ModelData md){
		
		if (md!=null) {
			
			//ModelData md=Model3DManager.getModel3dManager().getModelID((int)d.getAux());
			
			group = new TransformGroup();
			
			try {
				model=ModelLoader.getInstance().loadModel( ((View)view).getResLoc().getResource( md.getPath() ) );
				model.setName( getName() );
				model.setUserData( Selectable.class, new CycloneBoundingBoxSelectable<Node>( model ) );
				view.backLinkVisualItem(group,this,true);
				view.backLinkVisualItem(model,this,false);
		    	Transform3D transformRotation = new Transform3D();
		    	TransformGroup gTrans;
		    	
		       // transformRotation.setRotation(new AxisAngle3f(1,0,0,Consts.deg2rad*(md.getRotX())));
		        
		      /*  if (md.getRotY()!=0){
		        	Transform3D tY=new Transform3D();
		        	tY.setRotation(new AxisAngle3f(0,1,0,Consts.deg2rad*(md.getRotY())));
		        	transformRotation.add(tY);
		        }
		        if (md.getRotZ()!=0){
		        	Transform3D tZ=new Transform3D();
		        	tZ.setRotation(new AxisAngle3f(0,0,1,Consts.deg2rad*(md.getRotZ())));
		        	transformRotation.add(tZ);
		        }*/
		    	
		    	

		        gTrans=new TransformGroup();
		        gTrans.addChild(model);
		        
		        
		        
		        transformRotation.setScale(md.getScale(), md.getScale(), md.getScale());

		        group.addChild(gTrans);
		        gTrans.setTransform(transformRotation);
		      
		          
		       // if (md.getPrimType()==PrimitiveType.BOX){
		        	
		        	
		        	//Cria uma "caixa" ao redor do objeto
		    
		        if (View.drawBox)
		        	//if (!getName().equals("Nave do Jogador"))
		        	{
		        Box objVisual=new Box((float)md.getTamX()*2,(float)md.getTamY()*2,(float)md.getTamZ()*2,"grass.jpg");
		        group.addChild(objVisual);
		        Appearance a=objVisual.getAppearance();
	            RenderingAttributes ra =a.getRenderingAttributes();
	            if (ra==null)ra = new RenderingAttributes();
	            a.setRenderingAttributes(ra);
	            TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.5f); 
	            Material material = new Material();
	            material.setColorTarget(Material.DIFFUSE);
	            a.setMaterial(material);
	            a.setTransparencyAttributes(ta); 
			//}
		        	
		        
		        
				////group.addChild( new PointLight( 1f,1f, 1f, 200f, 200f, 150f, 0.001f ) );
				//group.addChild( new PointLight( 1f, 1f, 1f, -2f, -2f, -15f, 0.001f ) );
			group.addChild( new PointLight(true));
				
			//	group.addChild( new AmbientLight(true,Colorf.WHITE));
		        } else {
		        	// group.addChild(new Sphere((float)md.getTamX(),16,16,""));
				       //group.addChild(md.getGeom().getViewGroup("sph", ""));
					//	group.addChild( new PointLight( 1f, 0.8f, 0.2f, 2f, 2f, 15f, 0.001f ) );	
		        }
		        
			} catch (IncorrectFormatException e) {
				e.printStackTrace();
			} catch (ParsingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		//group_noRot.addChild(group);
		//return group_noRot;
	}
	
	

	
	
	 public void doMove() 
	    {        
		 //System.out.println("name:"+name+" "+flagDepth);
	    /*	Transform3D transform3D = new Transform3D();
	  		group.getTransform(transform3D);
	  		Transform3D toMove = new Transform3D();
	  		toMove.setTranslation(theMove);
	  		transform3D.mul(toMove);
	  		transform3D.setRotation(node.getOrientation().getQuaternion4f());
	  		group.setTransform(transform3D);*/
	  		
	    	//Transform3D transform = new Transform3D();
	
		 Vector3 theMove=new Vector3((coord.getX()-view.getRelativePosition().x),
				 coord.getY()-view.getRelativePosition().y,
				 coord.getZ()-view.getRelativePosition().z);
		 
		 	double scale=1;
		 	if (((View)view).useScale){
			 	double distancia=theMove.magnitude();
			 	if (distancia>View.clippingDist){
			 		
			 		if ((flagScaled!=1) && (flagDepth)){
			 	        //RenderingAttributes ra = new RenderingAttributes();
			        /*    Shape3D node=(Shape3D)(getView().getVisualItem(this));
			            Appearance a=node.getAppearance();
			            RenderingAttributes ra =a.getRenderingAttributes();
			            if (ra==null)ra = new RenderingAttributes();
			            System.out.println("name:"+getName()+" "+flagScaled);
			            Texture texture =getTextureOrNull("Planets/Barren02.png",true);
			            texture.setBoundaryModeS( TextureBoundaryMode.CLAMP_TO_BORDER);
			            texture.setBoundaryModeT( TextureBoundaryMode.CLAMP_TO_BORDER );
			            
			            a.setTexture( texture );*/
			            //ra.
			            //ra.setDepthBufferEnabled(false);
			            //ra.setd
			          //  ra.setDepthBufferWriteEnabled( false );
			           // a.setRenderingAttributes(ra);
			         //   view.getCanvas().getRenderer().setTransparentSortingPolicy(Renderer.TransparentSortingPolicy.SORT_FRONT_TO_BACK);

			 			/*if (view.getRootBranch().indexOf(group)>=0)
			 				view.getRootBranch().removeChild(group);
			 			getView().skyBoxTransform.addChild(group);*/
			            flagScaled=1;
			 		}
			 		
				 		scale=View.clippingDist/distancia;
				 		theMove.x=theMove.x*scale;//+theMove.x()/Consts.C));
				 		theMove.y=theMove.y*scale;
				 		theMove.z=theMove.z*scale;
			 			
				 		/*double ndist=View.clippingDist+(distancia-View.clippingDist);
				 		scale=View.clippingDist/ndist;
				 		theMove.x=theMove.x*scale;//+theMove.x()/Consts.C));
				 		theMove.y=theMove.y*scale;
				 		theMove.z=theMove.z*scale;*/
			 
			 	} else {
			 		if ((flagScaled!=2) && (flagDepth)){
			         /*   Shape3D node=(Shape3D)(getView().getVisualItem(this));
			            Appearance a=node.getAppearance();
			            RenderingAttributes ra =a.getRenderingAttributes();
			            if (ra==null)ra = new RenderingAttributes();
			            ra.setDepthBufferWriteEnabled( true );
			            a.setRenderingAttributes(ra);
			            
			            System.out.println("name:"+getName()+" "+flagScaled);*/


			 			/*if (getView().skyBoxTransform.indexOf(group)>=0)
			 				getView().skyBoxTransform.removeChild(group);
			 			if (view.getRootBranch().indexOf(group)==-1)
					 		view.getRootBranch().addChild(group);*/
					 	flagScaled=2;
			 		}
			 	}
		 	}
		 
//			System.out.println("viewModel:"+getName()+" theMove="+theMove+" centerpos="+view.getCenter_position()+" coord="+getCoord());	
	    	transform.set(theMove.getVector3f());
	    	transform.setRotation(orientation.getQuaternion4f());
	    	transform.setScale((float)scale);
	    	group.setTransform(transform);
	    	
	  		
	    	
	   }
	 

	
	/* (non-Javadoc)
	 * @see galactic.view.models.IItemView#draw(boolean)
	 */
	@Override
	public void draw(boolean direct){
		if (group==null){
			//mainGroup=new TransformGroup();
			//group=getViewGroup(getName(),view.getResLoc());
		System.out.println("View Group = null!");	
			
		//	mainGroup.addChild(group);
		//	mainGroup.addChild(addText());
		}
//		System.out.println("ViewModel.draw() pos:"+coord+" "+name);
		
		if ((direct) || (relCoord.x!=coord.getX()) || (relCoord.y!=coord.getY()) ||(relCoord.z!=coord.getZ()))
			doMove();
		
		updateCoord();
		//System.out.println("x:"+coord.getX()+" ix:"+item.getCoord().getX());
	
	}


	

	
	

	/* (non-Javadoc)
	 * @see galactic.view.models.IItemView#getGroup()
	 */
	public TransformGroup getGroup() {
		return group;
	}

	public void setGroup(TransformGroup group) {
		this.group = group;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
	
	




	
	
	

	
	
}
