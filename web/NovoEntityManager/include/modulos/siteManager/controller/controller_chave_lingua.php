<?php

class ChaveLinguaController extends BaseController {

    function insereRegistro($arr) {
        // printArray($arr);
        if (!existsQuery("select * from chave_lingua where nm_chave_lingua=?", array($arr['nm_chave_lingua'])))
            executaSQL("insert into chave_lingua (nm_chave_lingua) values (?)", array($arr['nm_chave_lingua']));

        if (!existsQuery("select * from valor_lingua where nm_chave_lingua=? and id_lingua=1", array($arr['nm_chave_lingua'])))
            executaSQL("insert into valor_lingua (nm_chave_lingua,id_lingua,nm_valor_lingua) values (?,1,?)", array($arr['nm_chave_lingua'], $arr['valor_en_chave_lingua_novo']));
        if (!existsQuery("select * from valor_lingua where nm_chave_lingua=? and id_lingua=2", array($arr['nm_chave_lingua'])))
            executaSQL("insert into valor_lingua (nm_chave_lingua,id_lingua,nm_valor_lingua) values (?,2,?)", array($arr['nm_chave_lingua'], $arr['valor_pt_chave_lingua_novo']));
    }

    function atualizaValor($idreg, $campo, $novoValor) {
        if ($campo == "valor_en_chave_lingua") {
            executaQuery("update valor_lingua set nm_valor_lingua=? where nm_chave_lingua=? and id_lingua=1", array($novoValor, $idreg));
            return;
        }
        if ($campo == "valor_pt_chave_lingua") {
            executaQuery("update valor_lingua set nm_valor_lingua=? where nm_chave_lingua=? and id_lingua=2", array($novoValor, $idreg));
            return;
        }
        return parent::atualizaValor($idreg, $campo, $novoValor);
    }

    function excluirRegistro($chave) {
        $chave = validaTexto($chave);
        executaSQL("delete from valor_lingua where nm_chave_lingua=?", array($chave));
        executaSQL("delete from chave_lingua where nm_chave_lingua=?", array($chave));
    }

}

?>
