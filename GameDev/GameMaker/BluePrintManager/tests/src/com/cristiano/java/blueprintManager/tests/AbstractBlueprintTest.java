package com.cristiano.java.blueprintManager.tests;

import static org.junit.Assert.assertTrue;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.bpM.entidade.blueprint.Mod;

public class AbstractBlueprintTest {
	String stTileset = "tileSet";
	String stTilesetValue = "firetiles.png";
	String nameItem = "Some Item";
	
	protected void verificaFunction(AbstractElement ge, String function, String esperado) {
		verificaFunction(ge,function,esperado,false);
	}
	
	protected void verificaFunction(AbstractElement ge, String function, String esperado,boolean startsWith) {
		String resultado = ge.resolveFunctionOf(function);
		if (startsWith){
			assertTrue("Resultado diferente para '" + function + "': " + esperado + "<>" + resultado, resultado.startsWith(esperado));
		} else {
			assertTrue("Resultado diferente para '" + function + "': " + esperado + "<>" + resultado, resultado.equals(esperado));
		}
	}
	
	public void createTestMods(ElementManager emLocal) {
		Mod modSufixo = emLocal.createMod(null);
		modSufixo.setProperty("name= concat($this.name,' of Whooping Ass')");
		modSufixo.setProperty("damage= $source.damage * 2.3");
		modSufixo.setProperty("identifier=WhoopingMod");
		modSufixo.setProperty("modWhooping= 'Whooping'");
		modSufixo.addTag("weapon suffix");// domain do mod, nao dso item gerado

		Mod modPrefixo = emLocal.createMod(null);
		modPrefixo.setProperty("name= concat('Gnarled ',$this.name)");
		modPrefixo.setProperty("value+= 100");
		modPrefixo.setProperty("modgnarled= 'gnarled'");
		modSufixo.setProperty("identifier=GnarledMod");
		modPrefixo.addTag("weapon preffix");// domain do mod, nao do item
											// gerado
	}
	
	protected void criaTestBlueprints(ElementManager emLocal) {
		String nameWeapon = "";
		String namePointedStick = "Pointed Stick";
		String nameSpear = "Worn Spear";

		// Item
		Blueprint item = emLocal.createBlueprint(null);
		item.setProperty("name = '" + nameItem + "'");
		item.setProperty("value = 1");
		item.setProperty("valor55 = 55");
		item.setProperty("valor = 100");
		assertTrue("name=" + item.getProperty("name"), item.getProperty("name").equals(nameItem));
		item.setDomain("type={item}");
		item.addTag("item");

		// Weapon
		Blueprint weapon = emLocal.createBlueprint(item);
		weapon.setProperty("name='" + nameWeapon + "'");
		weapon.setProperty("value +=2");
		weapon.setProperty("valor *=1.1");
		weapon.setProperty("damage=" + 1);
		weapon.setDomain("type+={weapon}");
		weapon.addTag("weapon");
		assertTrue(weapon.getProperty("name"), weapon.getProperty("name").equals(nameWeapon));

		assertTrue(weapon.getProperty("damage").equals("1"));

		String esperadoResolvido = "110";
		String esperado = "100*1.1";
		String saida = weapon.getPropertyH("valor", false);
		assertTrue("Valor:'" + saida + "' esperado:'" + esperadoResolvido + "'", saida.equals(esperado));
		saida = weapon.getPropertyH("valor", true);
		assertTrue("Valor=" + saida + " esperado=" + esperadoResolvido, saida.equals(esperadoResolvido));

		esperado = "1+2";
		assertTrue("Valor:'" + weapon.getPropertyH("value", false) + "'", weapon.getPropertyH("value", false).equals(esperado));
		esperadoResolvido = "3";
		saida = weapon.getPropertyH("value", true);
		assertTrue("Valor=" + weapon.getPropertyH("value", false) + " solved: " + saida, saida.equals(esperadoResolvido));

		// Spear
		Blueprint spear = emLocal.createBlueprint(weapon);
		spear.setProperty("name='" + nameSpear + "'");
		spear.setProperty("damage=rand(10,15)");
		spear.setProperty("identifier=SpearWeapon");
		spear.setDomain("type+={spear}");
		spear.addTag("spear");
		assertTrue(spear.getProperty("name").equals(nameSpear));

		esperado = "3";
		saida = spear.getPropertyH("value", false);
		assertTrue("saida=" + saida + " esperado:" + esperado, spear.getPropertyH("value", true).equals(esperado));

		// PointedStick
		Blueprint pointedStick = emLocal.createBlueprint(weapon);
		pointedStick.setProperty("name='" + namePointedStick + "'");
		pointedStick.setProperty("value=2");
		pointedStick.setProperty("damage=6");
		pointedStick.setDomain("type+={pointedstick}");
		pointedStick.addTag("pointedstick");
		assertTrue(pointedStick.getProperty("name").equals(namePointedStick));
		assertTrue(pointedStick.getProperty("value").equals("2"));
		assertTrue(pointedStick.getProperty("damage").equals("6"));

		// Concatenando string
		String nameModOfDoom = " of Doom";

		// Spear of doom
		Blueprint spearOfDoom = emLocal.createBlueprint(spear);
		spearOfDoom.setProperty("name+='" + nameModOfDoom + "'");
		spearOfDoom.setProperty("damage=rand(30,55)");

		assertTrue(spearOfDoom.getPropertyH("value", true).equals("3"));

		// nao resolvido
		esperado = "'" + nameSpear + "'+'" + nameModOfDoom + "'";
		saida = spearOfDoom.getPropertyH("name", false);
		assertTrue("saida=" + saida + " esperado=" + esperado, esperado.equals(saida));

		// resolvido
		esperado = "'" + nameSpear + nameModOfDoom + "'";
		saida = spearOfDoom.getPropertyH("name", true);
		assertTrue("saida=" + saida + " esperado=" + esperado, esperado.equals(saida));

	}
}
