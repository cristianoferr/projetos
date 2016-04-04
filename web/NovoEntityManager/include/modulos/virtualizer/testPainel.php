<?php

class TestsPainel extends TestBase {

    function testWidget() {
        $controllerPainel = getControllerForTable("painel");
        $painelH = $controllerPainel->createPanelObjectForID(PAINEL_TESTE_H);
        $painelV = $controllerPainel->createPanelObjectForID(PAINEL_TESTE_V);

        $this->assertNotNull($painelH, "painelH nulo");
        $this->assertNotNull($painelV, "painelV nulo");
        $this->assertEqual(get_class($painelV), "PainelVertical", "Classe vertical errada:" . get_class($painelV));
        $this->assertEqual(get_class($painelH), "PainelHorizontal", "Classe horizontal errada:" . get_class($painelH));
    }

}

?>
