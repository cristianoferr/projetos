<?

define("LIMITE_RADIO_SELECT", 7);

define("LINGUA_PT", "1");
define("LINGUA_EN", "2");
define("LINGUA_IPSUM", 3);

define("GUEST_ID", "2");


define("ITENS_RESUMO", "5"); //quantidade máxima de itens que mostra quando é painel resumo

define("FIRST_OPTION", "FIRST_OPTION");
define("ANCORA_INICIO", "top");

define("ACAO_NOVO", "novo");
define("ACAO_INSERIR", "inserir");
define("ACAO_EXCLUIR", "excluir");
define("ACAO_AVANCAR", "avancar");
define("ACAO_LISTAR", "lista");
define("ACAO_EDITAR", "editar");

define("CONDITION_EQUALS", "E");
define("CONDITION_GREATER", "G");
define("CONDITION_EQUAL_GREATER", "EG");
define("CONDITION_LOWER", "L");
define("CONDITION_EQUAL_LOWER", "EL");
define("CONDITION_DIFFERENT", "D");

define("SUFIXO_DESC", "_sufdesc");

define("COND_FIELD_OPTGROUP", "c_optgroup");


//Recursos
//define("ICON_EDIT", "pencil.png");
define("ICON_EDIT", "writing.png");
define("ICON_DELETE", "delete2.png");
define("ICON_CANCEL", "cancel.png");
define("ICON_SEGUIR", "go-next-yellow.png");
define("ICON_AVANCAR", "arrow_step_over.png");
define("ICON_EMAIL", "e_mail.png");
define("ICON_IDEA", "ktip.png");
define("ICON_BUG", "bug.png");
define("ICON_INFORMATION", "information.png");
define("ICON_ADD", "plus_orange.png");
define("ICON_NEW", "new.png");

define("TIPO_INPUT_INTEIRO", 1);
define("TIPO_INPUT_TEXTO_CURTO", 2);
define("TIPO_INPUT_TEXTAREA", 3);
define("TIPO_INPUT_SELECT_FK", 4);
define("TIPO_INPUT_BOOLEAN", 5);
define("TIPO_INPUT_SELECT_FIXO", 6);
define("TIPO_INPUT_DATA", 7);
define("TIPO_INPUT_COR", 8);
define("TIPO_INPUT_EMAIL", 9);
define("TIPO_INPUT_SEQUENCE", 10);
define("TIPO_INPUT_2_SELECT_OR", 11);
define("TIPO_INPUT_TEXTAREA_GRANDE", 12);
define("TIPO_INPUT_BUSCA_USUARIO", 13);
define("TIPO_INPUT_RADIO", 14);
define("TIPO_INPUT_RADIO_SELECT", 15);
define("TIPO_INPUT_UNIQUE_ID", 16);
define("TIPO_INPUT_UNIQUE_EMAIL", 17);
define("TIPO_INPUT_UNIQUE_ID_PROJETO", 18);
define("TIPO_INPUT_FLOAT", 19);
define("TIPO_INPUT_CURRENCY", 20);
define("TIPO_INPUT_HTML", 21);
define("TIPO_INPUT_SENHA", 22);
define("TIPO_INPUT_DATATYPE", 23);
define("TIPO_INPUT_EMAIL_HIDDEN", 24);


define("ABA_PRINCIPAL", 1);
define("ABA_COLUNA_GERAL", 2);
define("ABA_COLUNA_TABLE", 3);
define("ABA_COLUNA_CLASS", 4);
define("ABA_COLUNA_REDIR", 5);
define("ABA_COLUNA_DOC", 6);
define("ABA_COLUNA_CUSTOS", 7);
define("ABA_COLUNA_TECNICO", 8);
define("ABA_COLUNA_INFO", 9);
define("ABA_COLUNA_RETORNO", 10);

define("ERROR_COD_PROJETO_EXISTE", 1);
define("ERROR_EMAIL_EXISTS", 2);
define("ERROR_INVALID_PASSWORD", 3);
define("ERROR_INVALID_TICKET", 4);

//retorna true se o campo tiver descricao... geralmente chave estrangeira
function hasDescription($tipoDado) {
    if ($tipoDado == TIPO_INPUT_SELECT_FK) {
        return true;
    }
    if ($tipoDado == TIPO_INPUT_RADIO_SELECT) {
        return true;
    }
    if ($tipoDado == TIPO_INPUT_RADIO) {
        return true;
    }
    return false;
}

?>