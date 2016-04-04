<?

//http://www.simpletest.org/en/first_test_tutorial.html
$GLOBALS["login_page"] = true;
require_once('simpletest/autorun.php');
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_MAIN_PAGE);
inicializa();



if (!isAdmin()) {
    die("Must be admin.");
}

session_destroy();
session_start();


$_SESSION["projeto"] = PROJETO_TESTE;
$_SESSION["id_usuario"] = 1;

class TestBase extends UnitTestCase {

    function rsCount($rs) {
        $c = 0;
        while ($row = $rs->fetch()) {
            $c++;
        }
        return $c;
    }

    function verificaInclusaoRemocao($arr, $controller, $nmTabela, $arg0 = null) {
        $_SESSION["projeto"] = PROJETO_TESTE;
        $id = $controller->insereRegistro($arr, $arg0);
        $this->assertNotNull($id, "id $nmTabela nulo");
        $this->assertTrue(existsQuery("select * from $nmTabela where id_$nmTabela=?", array($id)), "$nmTabela não foi inserida: $id");

        $table = getTableManager()->getTabelaComNome($nmTabela);
        $descField = $table->getDescName();
        if ($descField) {
            $row = executaQuerySingleRow("select * from $nmTabela where id_$nmTabela=?", array($id));
            $nmExpected = $row[$descField];
            $nmDesc = $controller->getDescricao($id, false);
            $this->assertEqual($nmExpected, $nmDesc, "Nomes: '$nmExpected'<>'$nmDesc' Tabela:'$nmTabela'");
        }

        $model = $controller->loadSingle($id);
        $this->assertNotNull($model, "model nulo");
        $this->assertEqual($id, $model->getId(), "id($id) inserido diferente do id do model:" . $model->getId());

        $controller->excluirRegistro($id);
        if ($nmTabela == "projeto") {
            $controller->excluirFisicamente($id);
        }
        $this->assertFalse(existsQuery("select * from $nmTabela where id_$nmTabela=?", array($id)), "exclusão da $nmTabela deu erro: $id");

        executaSQL("ALTER TABLE $nmTabela AUTO_INCREMENT = " . ($id ));
    }

    function getArrayTabelas() {
        erroFatal("Defina getArrayTabelas()");
    }

    /**
     * Teste generico para testar o projeto da tabela informada
     * @param type $tabela
     * @param type $id_registro
     * @param type $id_esperado
     */
    function verificaIDProjeto($tabela, $id_registro, $id_esperado) {
        $controller = getControllerForTable($tabela);
        $id_resultado = $controller->getProjeto($id_registro);
        $this->assertEqual($id_resultado, $id_esperado, $tabela . "[" . $id_registro . "] : '$id_resultado' diferente de: '$id_esperado'"
        );
    }

    function validaTabelas() {
        $arr = $this->getArrayTabelas();
        foreach ($arr as $tabela) {
            $this->assertNotNull(getTableManager()->getTabelaComNome($tabela), "tabela: $tabela");
            $controller = getControllerForTable($tabela);
            $this->assertNotNull($controller, "tabela: $tabela");

            $this->assertNotNull($controller->getPlural(), "tabela: $tabela");
            $this->assertNotNull($controller->getSingular(), "tabela: $tabela");
            // $this->assertNotNull($controller->getNewItem(), "tabela: $tabela");
        }
    }

    /**
     * Testa a seguranca do modulo indicado
     * @param type $modulo
     * @param type $id_modulo
     * @param type $expectedWrite
     * @param type $expectedRead
     */
    function checkSecurity($modulo, $id_modulo, $expectedWrite, $expectedRead) {
        $flagWrite = checaEscrita($modulo, $id_modulo);
        $flagRead = checaLeitura($modulo, $id_modulo);

        $this->assertEqual($flagWrite, $expectedWrite, "Erro na permissao de escrita: '$flagWrite'<>'$expectedWrite': $modulo em $id_modulo no usuario:" . usuarioAtual());
        $this->assertEqual($flagRead, $expectedRead, "Erro na permissao de leitura: '$flagWrite'<>'$expectedWrite': $modulo em $id_modulo no usuario:" . usuarioAtual());
    }

    function validaPermissoesDefault($modulo, $id) {
        $this->impersonateNormalUser();
        $this->checkSecurity($modulo, $id, false, false);
        $this->impersonatePersonalUser();
        $this->checkSecurity($modulo, $id, false, true);
        $this->impersonateGuest();
        $this->checkSecurity($modulo, $id, false, false);
        $this->impersonateAdmin();
        $this->checkSecurity($modulo, $id, true, true);
    }

    function limpaSessao() {
        foreach ($_SESSION as $key => $value) {
            unset($_SESSION[$key]);
        }
        $_SESSION["projeto"] = PROJETO_TESTE;
    }

    function impersonateNormalUser() {
        $this->limpaSessao();
        $_SESSION["id_usuario"] = 10; //"testeNome"
    }

    function impersonatePersonalUser() {
        $this->limpaSessao();
        $_SESSION["id_usuario"] = USUARIO_PESSOAL;
    }

    function impersonateGuest() {
        $this->limpaSessao();
        $_SESSION["id_usuario"] = GUEST_ID;
    }

    function impersonateAdmin() {
        $this->limpaSessao();
        $_SESSION["id_usuario"] = 1;
    }

}

runUnitTests(null);
?>