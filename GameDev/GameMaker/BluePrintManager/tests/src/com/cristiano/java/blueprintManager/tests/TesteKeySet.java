package com.cristiano.java.blueprintManager.tests;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.product.IGameElement;


public class TesteKeySet {
	ElementManager em;
	String MALE="male";
	String RICH="rich";
	String BEAUTIFUL="beautiful";
	int tot=200;
	
	
	@Before
	public void tearUp() {
		//System.out.println("TesteKeySet Setup");
		em=new ElementManager();
		
		 for (int i=0;i<tot;i++){
		    	GenericElement ge=new GenericElement(em);
		    	ge.setName("-MALE-"+i);
		    	em.addElement(ge);
		    	ge.addTag(MALE);
		    }
		    
		    for (int i=0;i<tot;i++){
		    	GenericElement ge=new GenericElement(em);
		    	ge.setName("-RICH-"+i);
		    	em.addElement(ge);
		    	ge.addTag(RICH);
		    }
		    
		    for (int i=0;i<tot;i++){
		    	GenericElement ge=new GenericElement(em);
		    	ge.setName("-MALE RICH-"+i);
		    	em.addElement(ge);
		    	ge.addTag(RICH);	
		    	ge.addTag(MALE);

		    }
		    
		    for (int i=0;i<tot;i++){
		    	int p=(int)(Math.random()*em.size());
		    	IGameElement ge=em.getElementAt(p);
		    	ge.addTag(BEAUTIFUL);
		    }
	}
	
	
	@Test public void testKeySet() {

	    List<IGameElement> result=em.intersect(MALE,RICH);
	    
	    assertTrue(result.size()>0);
	    
	  //  System.out.println("Testando intersect:"+result.size());
	    assertTrue((result.size()==tot));
	    for (int i=0;i<result.size();i++){
	    	IGameElement ge=result.get(i);
	    	//String name=ge.toString();
	    	//System.out.println("i:"+i+" "+name);
	    	boolean flagMale=ge.hasTag(MALE);
	    	boolean flagRich=ge.hasTag(RICH);
	    	assertTrue(flagMale && flagRich);
	    }
	    
	   // System.out.println("Testando exclude:"+result.size());
	    result=em.exclude(MALE,RICH);
	    assertTrue((result.size()==tot));
	    for (int i=0;i<result.size();i++){
	    	IGameElement ge=result.get(i);
	    	//System.out.println("i:"+i+" "+name);
	    	boolean flagMale=ge.hasTag(MALE);
	    	boolean flagRich=ge.hasTag(RICH);
	    	assertTrue(flagMale && !flagRich);
	    }

	    
	    
	    
	    result=em.union(MALE,RICH);
	   // System.out.println("Testando union:"+result.size());
	    assertTrue("Quantidade final diferente:"+result.size(),(result.size()==tot*3));
	    for (int i=0;i<result.size();i++){
	    	//System.out.println("i:"+i+" "+name);
	    	//assertTrue(name.contains("-MALE-"));
	    }
	    
	    

	}
	
	
	@Test public void testKeySetConjuntosIntersectEexclude() {
		//System.out.println("testKeySetConjuntosIntersectEexclude()");
		List<IGameElement> result;
		 
		
		String tag=MALE+" "+RICH;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()==tot));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			assertTrue(flagMale && flagRich);
		}
		
		tag=MALE+" "+RICH+" "+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(flagMale && flagRich && flagBeautiful);
		}
		
		
		tag=MALE+" "+RICH+" !"+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(flagMale && flagRich && !flagBeautiful);
		}
		
		
		tag=MALE+" !"+RICH+" !"+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(flagMale && !flagRich && !flagBeautiful);
		}
		
		tag="!"+MALE+" "+RICH+" !"+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(!flagMale && flagRich && !flagBeautiful);
		}
		
		tag="!"+MALE+" "+RICH+" "+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(!flagMale && flagRich && flagBeautiful);
		}
	}
	
	
	@Test public void testKeySetConjuntos() {
		//System.out.println("testKeySetConjuntos()");
		List<IGameElement> result;
		 
		
		String tag=MALE+" +"+RICH;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			assertTrue(flagMale || flagRich);
			
		}
		
		tag=MALE+" +"+RICH+" +"+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(flagMale || flagRich|| flagBeautiful);
			
		}
		
		tag=MALE+" !"+RICH+" !"+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagRich=ge.hasTag(RICH);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue((flagMale && (!flagRich && !flagBeautiful)));
		}
		
		tag="!"+MALE+" "+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(!flagMale &&  flagBeautiful);
		}
		
		tag=MALE+" !"+BEAUTIFUL;
		result=em.getElementsWithTag(tag);
		//System.out.println("Testando intersect via elementsWithTag("+tag+"):"+result.size());
		assertTrue((result.size()>0));
		for (int i=0;i<result.size();i++){
			IGameElement ge=result.get(i);
			//System.out.println("i:"+i+" "+ge.toString());
			boolean flagMale=ge.hasTag(MALE);
			boolean flagBeautiful=ge.hasTag(BEAUTIFUL);
			assertTrue(flagMale &&  !flagBeautiful);
			
		}
		
	}
}
