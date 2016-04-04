package com.cristiano.java.gm.utils;

import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.product.IManageElements;

public interface IConsole {

	void console(String auxInfo, EntityManager entMan, IManageElements em);

}
