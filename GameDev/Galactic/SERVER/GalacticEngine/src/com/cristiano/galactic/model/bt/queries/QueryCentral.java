package com.cristiano.galactic.model.bt.queries;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Logic.representation.Memory;


/**
 *Essa classe é responsável por verificar as questões lógicas referente as entidades.
 *Por exemplo: Alvo está longe? Alvo está acima ou abaixo? E outras coisas do genero.  
 * Ela vai substituir o ItemQueryManager, que era instanciado para cada objeto, o que é desnecessário.
 * @author cmm4
 */
public class QueryCentral {

	public static final String QUERY_TARGETINFRONT="FactSelfPositionX";
	public static final String QUERY_TARGETINREAR="query_TargetInRear";
	public static final String QUERY_TARGETTOTHELEFT="query_TargetToTheLeft";
	public static final String QUERY_TARGETTOTHERIGHT="query_TargetToTheRight";
	public static final String QUERY_TARGETISBELOW="query_TargetIsBelow";
	public static final String QUERY_TARGETISABOVE="query_TargetIsAbove";
	public static final String QUERY_SELFROTATINGLEFT="query_SelfRotatingLeft";
	public static final String QUERY_SELFROTATINGRIGHT="query_SelfRotatingRight";
	public static final String QUERY_SELFROTATINGUP="query_SelfRotatingUp";
	public static final String QUERY_SELFROTATINGDOWN="query_SelfRotatingDown";
	public static final String QUERY_TARGETALMOSTALIGNEDVERT="query_TargetAlmostAlignedVert";
	public static final String QUERY_SELFTOOMUCHROTATION="query_SelfTooMuchRotation";
	public static final String QUERY_SELFLOWROTATIONVERT="query_SelfLowRotationVert";
	public static final String QUERY_SELFLOWROTATIONHORIZ="query_SelfLowRotationHoriz";
	public static final String QUERY_TARGETALMOSTALIGNEDHORIZ="query_TargetAlmostAlignedHoriz";
	public static final String QUERY_TARGETALIGNED="query_TargetAligned";

	
	public static double checkFact(ArtificialEntity item,QueryType qt,String fact,double value,double max){
		double fv=item.getMemory().getFact(fact);
		double res=Math.abs((QueryType.checkQuery(qt, fv, value)));
		
		//res=res/max;
		//if (res>1) res=1;
		return res;		
	}
	
	public static double checkQuery(ArtificialEntity item,String name){
	
		
		if (name.equals(QUERY_TARGETINFRONT)){
			return checkFact(item,QueryType.QT_EQUAL_OR_LOWER,Memory.FACT_TARGETFRONTANGLE,0,30);}
		if (name.equals(QUERY_TARGETINREAR)){return checkFact(item,QueryType.QT_EQUAL_OR_GREATER,Memory.FACT_TARGETFRONTANGLE,0,30);}
		if (name.equals(QUERY_TARGETTOTHELEFT)){return checkFact(item,QueryType.QT_EQUAL_OR_GREATER,Memory.FACT_TARGETHORIZONTALANGLE,2,30);}
		if (name.equals(QUERY_TARGETTOTHERIGHT)){return checkFact(item,QueryType.QT_EQUAL_OR_LOWER,Memory.FACT_TARGETHORIZONTALANGLE,-2,30);}
		if (name.equals(QUERY_TARGETISBELOW)){return checkFact(item,QueryType.QT_EQUAL_OR_GREATER,Memory.FACT_TARGETVERTICALANGLE,2,30);}
		if (name.equals(QUERY_TARGETISABOVE)){return checkFact(item,QueryType.QT_EQUAL_OR_LOWER,Memory.FACT_TARGETVERTICALANGLE,-2,30);}
		if (name.equals(QUERY_SELFROTATINGUP)){return checkFact(item,QueryType.QT_GREATER,Memory.FACT_SELFROCVERTICAL,0,30);}
		if (name.equals(QUERY_SELFROTATINGDOWN)){return checkFact(item,QueryType.QT_LOWER,Memory.FACT_SELFROCVERTICAL,0,30);}
		if (name.equals(QUERY_SELFROTATINGLEFT)){return checkFact(item,QueryType.QT_LOWER,Memory.FACT_SELFROCHORIZONTAL,0,30);}
		if (name.equals(QUERY_SELFROTATINGRIGHT)){return checkFact(item,QueryType.QT_GREATER,Memory.FACT_SELFROCHORIZONTAL,0,30);}
		if (name.equals(QUERY_TARGETALMOSTALIGNEDVERT)) {return checkFact(item,QueryType.QT_ABS_LOWER,Memory.FACT_TARGETVERTICALANGLE,5,30);}
		if (name.equals(QUERY_TARGETALMOSTALIGNEDHORIZ)){return checkFact(item,QueryType.QT_ABS_LOWER,Memory.FACT_TARGETHORIZONTALANGLE,5,30);}
		if (name.equals(QUERY_TARGETALIGNED)){
			//double fv=item.getMemory().getFact(Memory.fact_TargetFrontAngle);
			double r=checkFact(item,QueryType.QT_ABS_LOWER,Memory.FACT_TARGETFRONTANGLE,20,20);
			//System.out.println("fact_TargetFrontAngle:"+fv+" r:"+r);
			
			return r;
		}
		if (name.equals(QUERY_SELFTOOMUCHROTATION)){return checkFact(item,QueryType.QT_GREATER,Memory.FACT_SELFROTATIONMAGNITUDE,.3,1);}
		if (name.equals(QUERY_SELFLOWROTATIONHORIZ)){
			return checkFact(item,QueryType.QT_ABS_LOWER,Memory.FACT_SELFROCHORIZONTAL,.1,5);
		}
		if (name.equals(QUERY_SELFLOWROTATIONVERT)){
			double d=checkFact(item,QueryType.QT_ABS_LOWER,Memory.FACT_SELFROCVERTICAL,.1,.1);
			return d;
			}
		
		return -1;
	}

	
	
	
}
