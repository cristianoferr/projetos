<?

define("PAINEL_LISTA_ARTIGOS", 'P_LIST_ARTIGOS');
define("PAINEL_EDIT_ARTIGO", 'P_EDIT_ARTIGO');
define("PAINEL_NOVO_ARTIGO", 'P_novo_artigo');

class PainelManagerArtigo {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_LISTA_ARTIGOS) {
            return $this->getPainelListaArtigos();
        }
        if ($nome == PAINEL_EDIT_ARTIGO) {
            return $this->getPainelEditArtigo();
        }
        if ($nome == PAINEL_NOVO_ARTIGO) {
            return $this->getFormInclusaoArtigo();
        }
    }

    function getFormInclusaoArtigo() {
        $table = getTableManager()->getTabelaComNome("artigo");
        $painel = new FormPainel("fia", $table);
        $painel->addColunaWithDBName("id_lingua");
        $painel->addColunaWithDBName("title_artigo");
        $painel->addColunaWithDBName("texto_artigo");

        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelListaArtigos() {//coberto
        $table = getTableManager()->getTabelaComNome("artigo");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelArtigos("psa", $table);
        $painel->addColunaWithDBName("id_artigo");
        $painel->addColunaWithDBName("id_elemento");
        $painel->addColunaWithDBName("title_artigo");
        $painel->addColunaWithDBName("id_lingua");
        //$painel->adicionaAcaoSeguir(getHomeDir()."deliverable/".projetoAtual()."/");
        return $painel;
    }

    function getPainelEditArtigo() {
        $table = getTableManager()->getTabelaComNome("artigo");
        $painel = new FormPainel("pea", $table);
        $painel->addColunaWithDBName("id_artigo");
        $painel->addColunaWithDBName("id_elemento");
        $painel->addColunaWithDBName("id_lingua");
        $painel->addColunaWithDBName("title_artigo");
        $painel->addColunaWithDBName("texto_artigo");
        $painel->modoEdicao();
        $painel->ativaAjax();
        return $painel;
    }

}

?>