<?php

/**
 * Uma entidade virtual são os dados que ficam dentro da "entidade" do sistema... é virtual porque não existe a tabela.
 */
class TestsVirtualEntity extends TestBase {

    function testEntidadeVirtual() {
        $table = getTableManager()->getTabelaComNome("virtual");
        $this->assertNotNull($table, "Tabela virtual nula");
        $controller = getControllerForTable("virtual");
        $this->assertNotNull($controller, "Controller virtual nulo");
        $controller->initFromEntity(ENTIDADE_TESTE);

        //$controllerRegistro = getControllerForTable("registro");
        //$controllerRegistro->insereRegistro(ENTIDADE_TESTE);
//coluna
        $colunas = $table->getColunas();
        $this->assertNotNull($colunas, "Colunas da Tabela virtual nulo");
        $this->assertTrue(sizeof($colunas) > 0, "Array de colunas com tamanho 0");
        foreach ($colunas as $coluna) {
            $this->assertTrue(get_class($coluna) == "ColunaVirtualModel", "Classe diferente de ColunaVirtualModel: " . get_class($coluna));
            $this->assertNotNull($coluna->getId(), "Id da coluna vazio");
            $this->assertNotNull($coluna->getDbName(), "DBName da coluna vazio");
            $this->assertNotNull($coluna->getTable(), "Table da coluna vazio");
            $this->assertNotNull($coluna->getTipoInput(), "getTipoInput da coluna vazio");
        }

        //model
        $controller->loadRegistros();
        $rowCount = $controller->getRowCount();
        $this->assertTrue($rowCount > 0, "Rowcount tamanho 0");
        $this->assertTrue($rowCount == 2, "Rowcount diferente de 2:$rowCount");

        $passou = false;
        while ($model = $controller->next()) {
            $passou = true;
            $this->assertTrue(get_class($model) == "VirtualModel", "Classe diferente de VirtualModel: " . get_class($model));
            $this->assertNotNull($model->getId(), "Id do modelo vazio");
            $this->assertNotNull($model->getTable(), "table do modelo vazio");

            foreach ($colunas as $coluna) {
                $valorColunaName = $model->getValorCampo($coluna->getDbName());
                $valorColunaColuna = $model->getValor($coluna);
                $this->assertNotNull($valorColunaColuna, "valorColunaColuna vazio");
                $this->assertNotNull($valorColunaName, "valorColunaName vazio");
                $this->assertEqual($valorColunaName, $valorColunaColuna, "valorColunaName($valorColunaName) diferente de valorColunaColuna($valorColunaColuna)");
            }
        }
        $this->assertTrue($passou, "next() do controller não retornou nada");

        //testando load single
        $model51 = $controller->loadSingle(51);
        $this->assertNotNull($model51, "model51 retornou nulo");
        $this->assertEqual($model51->getId(), 51, "id retornou diferente do esperado: " . $model51->getId());

        $model52 = $controller->loadSingle(52);
        $this->assertNotNull($model52, "model52 retornou nulo");
        $this->assertEqual($model52->getId(), 52, "id retornou diferente do esperado: " . $model52->getId());
    }

    function testaAdicaoModel() {
        $controller = getControllerForTable("virtual");
        $this->assertNotNull($controller, "Controller virtual nulo");
        $controller->initFromEntity(ENTIDADE_TESTE);
        //por ultimo
        $controller->limpaRegistros();
        $rowCount = $controller->getRowCount();
        $this->assertTrue($rowCount == 0, "Rowcount tamanho diferente de 0 após reset");

        $model = $controller->loadEmptyModel();
        $this->assertNotNull($model, "ModeloLimpo vazio");

        $controller->adicionaRegistro($model);
        $controller->adicionaRegistro(null);
        $rowCount = $controller->getRowCount();
        $this->assertTrue($rowCount == 1, "Rowcount diferente de 1: '$rowCount' apos inserir");

        $controller->limpaRegistros();
        $rowCount = $controller->getRowCount();
        $this->assertTrue($rowCount == 0, "Rowcount tamanho diferente de 0:'$rowCount' após reset2");
    }

    function testPreenchePainel() {
        $controller = getControllerForTable("virtual");
        $this->assertNotNull($controller, "Controller virtual nulo");
        $controller->initFromEntity(ENTIDADE_TESTE);
        $controller->loadRegistros();

        $painel = getPainelManager()->getPainel(PAINEL_VIEW_VIRTUAL_ENTITY);
        $painel->limpaColunas();
        $this->assertNotNull($painel, "Painel  nulo");
        $size = sizeof($painel->getColunas());
        $this->assertTrue($size == 0, "Qtd de colunas inicial>0: $size");

        $controller->loadAllColumnsIntoPanel($painel);
        $size = sizeof($painel->getColunas());
        $this->assertTrue($size >= 0, "Colunas não foram incluidaQtd de colunas inicial>0: $size");

        $painel->limpaColunas();
        $size = sizeof($painel->getColunas());
        $this->assertTrue($size == 0, "limpaColunas não funcionou: $size");
    }

}

?>
