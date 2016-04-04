package com.cristiano.galactic.controller;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.view.ItemsViewManagerAbstract;


public abstract class AbstractController implements PropertyChangeListener {

    private ArrayList<ItemsViewManagerAbstract> registeredViews;
    //private ArrayList<AbstractModel> registeredModels;

    public AbstractController() {
        setRegisteredViews(new ArrayList<ItemsViewManagerAbstract>());
       // registeredModels = new ArrayList<AbstractModel>();
    }

   

   /* public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }*/

    public void addView(ItemsViewManagerAbstract view) {
    	if (getRegisteredViews().contains(view)){
    		Galactic.log("Controller já possui essa view!!");
    	}
        getRegisteredViews().add(view);
    }

    public void removeView(ItemsViewManagerAbstract view) {
        getRegisteredViews().remove(view);
    }


    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.


    public void propertyChange(PropertyChangeEvent evt) {

        for (ItemsViewManagerAbstract view: getRegisteredViews()) {
            view.modelPropertyChange(evt);
        }
    }


    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value
     * of the property.
     */
    protected void setModelProperty(Object model,String propertyName, Object newValue) {

        //for (AbstractModel model: registeredModels) {
            try {
	
                Method method = model.getClass().
                    getMethod("set"+propertyName, new Class[] {
                                                      newValue.getClass()
                                                  }


                             );
                method.invoke(model, newValue);

            } catch (Exception ex) {
            	Galactic.log(ex.getMessage());
            }
      //  }
    }


	public void setRegisteredViews(ArrayList<ItemsViewManagerAbstract> registeredViews) {
		this.registeredViews = registeredViews;
	}


	public ArrayList<ItemsViewManagerAbstract> getRegisteredViews() {
		return registeredViews;
	}

    
    

}