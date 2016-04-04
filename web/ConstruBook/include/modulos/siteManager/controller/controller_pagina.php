<?

class PaginaController extends BaseController {

    function loadPaginasComArtigos() {
        $this->loadRegistros("and id_pagina in (select id_pagina from elemento e,artigo a where e.id_elemento=a.id_elemento)");
    }

    function insereRegistro($arr) {//coberto
        validaAdmin();
        $id = executaSQL("insert into pagina (id_tipo_pagina,nminterno_pagina) values (:id_tipo_pagina,:nminterno_pagina)", $arr);
        return $id;
    }

    function excluirRegistro($id) {//coberto
        validaEscrita("pagina", $id);
        getControllerForTable("elemento")->excluirElementoPagina($id);
        executaSQL("delete from pagina where id_pagina=?", array($id));
    }

    function getActualPage() {
        $id = getIDPaginaAtual();
        //echo "id: $id" . $this->getFieldFromModel($id, "nminterno_pagina");
        return $this->getFieldFromModel($id, "nminterno_pagina");
    }

}

?>