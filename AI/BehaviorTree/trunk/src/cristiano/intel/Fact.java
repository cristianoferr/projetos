package cristiano.intel;

import java.util.Date;

import cristiano.behaviortree.execution.Executor;

public class Fact {
private String name;
double value;
boolean requireUpdate=false;
private Date lastResult=new Date();


public Fact(String name,double value){
	this.setName(name);
	this.value=value;
}

public boolean oktoUpdate(int timeMS){
	Date now=new Date();
	long i2=now.getTime();
	long i1=lastResult.getTime();
	int dif=(int)(i2-i1);
	if (dif>timeMS){
		lastResult=new Date();
		return true;
	}
	return false;
}	

public double getValue(){
	return value;
}



public double getResult(){
	return value;
}


public void setName(String name) {
	this.name = name;
}

public String getName() {
	return name;
}

public boolean isRequireUpdate() {
	return requireUpdate;
}

public void turn(){
	
}

public void setRequireUpdate(boolean requireUpdate) {
	this.requireUpdate = requireUpdate;
}

/**
 * @param value the value to set
 */
public void setValue(double value) {
	this.value = value;
}
}
