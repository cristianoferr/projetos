<?

define("PAINEL_ABTESTS", 'P_abtests');
define("PAINEL_ABTEST", 'P_abtest');
define("PAINEL_FORM_INC_ABTEST", 'P_inc_abtest');
define("PAINEL_VARIACOES_ABTEST", 'P_VARS_AB');
define("PAINEL_FORM_INC_ABTEST_VARIACAO", 'p_inc_abt_var');
define("PAINEL_ABTESTS_VARIACOES", 'p_ab_vars');
define("PAINEL_FORM_INC_TIPO_ABTEST", 'p_fftab');

class PainelManagerABTest {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_ABTESTS)
            return $this->getPainelABTests();
        if ($nome == PAINEL_ABTEST)
            return $this->getPainelABTest();
        if ($nome == PAINEL_FORM_INC_ABTEST)
            return $this->getPainelIncTest();
        if ($nome == PAINEL_VARIACOES_ABTEST)
            return $this->getPainelVariacoesABTest();
        if ($nome == PAINEL_FORM_INC_ABTEST_VARIACAO)
            return $this->getPainelIncVariacaoABTest();
        if ($nome == PAINEL_ABTESTS_VARIACOES)//usado na pagina exclusiva de variações
            return $this->getPainelVariacoesABTest();

        if ($nome == PAINEL_FORM_INC_TIPO_ABTEST)
            return $this->getPainelIncTipoAbtest();
        if ($nome == PAINEL_TIPO_ABTESTS)
            return $this->getPainelTiposAbtest();
    }

    function getPainelIncTipoAbtest() {
        $table = getTableManager()->getTabelaComNome("tipo_abtest");
        $painel = new PainelVertical("pitab", $table);

        $painel->addColunaWithDBName("nm_tipo_abtest");
        $painel->addColunaWithDBName("id_pagina");
        $painel->addColunaWithDBName("flag_guest_only");

        $painel->adicionaLinkImportante(getHomeDir() . "tipoabtest", translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelTiposAbtest() {
        $table = getTableManager()->getTabelaComNome("tipo_abtest");
        $painel = new PainelHorizontal("ptab", $table);
        $painel->addColunaWithDBName("id_tipo_abtest");
        $painel->addColunaWithDBName("nm_tipo_abtest");
        $painel->addColunaWithDBName("id_pagina");
        $painel->addColunaWithDBName("flag_guest_only");

        $painel->modoEdicao();

        $painel->setDeleteLink("tipoabtest/delete/");
        return $painel;
    }

    function getPainelIncVariacaoABTest() {
        $table = getTableManager()->getTabelaComNome("abtest_variacao");
        $painel = new PainelVertical("pivabt", $table);
        $painel->addColunaWithDBName("nm_abtest_variacao");
        $painel->addColunaWithDBName("id_abtest");

        $painel->adicionaLinkImportante(getHomeDir() . "abtest/" . abtestAtual(), translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelVariacoesABTest() {
        $table = getTableManager()->getTabelaComNome("abtest_variacao");
        $painel = new PainelHorizontal("pvabt", $table);
        $painel->addColunaWithDBName("id_abtest_variacao");
        $painel->addColunaWithDBName("cod_abtest_variacao");
        $painel->addColunaWithDBName("nm_abtest_variacao");
        $painel->addColunaWithDBName("id_abtest");
        $painel->addColunaWithDBName("count_abtest_variacao");
        $painel->addColunaWithDBName("count_participantes");
        $painel->modoEdicao();

        $painel->setEditLink("abtest/variacao/" . abtestAtual() . "/");
        $painel->setDeleteLink("abtest/variacao/delete/" . abtestAtual() . "/");
        return $painel;
    }

    function getPainelABTests() {
        $table = getTableManager()->getTabelaComNome("abtest");
        $painel = new PainelHorizontal("pabt", $table);
        $painel->addColunaWithDBName("id_abtest");
        $painel->addColunaWithDBName("nm_abtest");
        $painel->addColunaWithDBName("url_abtest");
        $painel->addColunaWithDBName("id_tipo_abtest");
        $painel->addColunaWithDBName("count_votos");
        $painel->addColunaWithDBName("cont_variacoes");
        $painel->addColunaWithDBName("cont_participantes");
        $painel->modoEdicao();

        $painel->setEditLink("abtest/");
        $painel->setDeleteLink("abtest/delete/");
        return $painel;
    }

    function getPainelABTest() {
        $table = getTableManager()->getTabelaComNome("abtest");
        $painel = new PainelVertical("pabt", $table);
        $painel->addColunaWithDBName("id_abtest");
        $painel->addColunaWithDBName("nm_abtest");
        $painel->addColunaWithDBName("url_abtest");
        $painel->addColunaWithDBName("desc_abtest");
        $painel->addColunaWithDBName("id_tipo_abtest");
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelIncTest() {
        $table = getTableManager()->getTabelaComNome("abtest");
        $painel = new PainelVertical("pit", $table);
        $painel->addColunaWithDBName("nm_abtest");
        $painel->addColunaWithDBName("url_abtest");
        $painel->addColunaWithDBName("desc_abtest");
        $painel->addColunaWithDBName("id_tipo_abtest");

        $painel->adicionaLinkImportante(getHomeDir() . "abtest", translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

}

?>