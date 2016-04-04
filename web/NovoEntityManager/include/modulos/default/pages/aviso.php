<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();
definePaginaAtual(PAGINA_ERRO);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();


escreveHeader();

$erro = $_GET['erro'];
if ($erro == ALERT_RESET_EMAIL_SENT) {
    $title = translateKey("txt_reset_email_sent");
    $msg = translateKey("txt_reset_email_sent_descricao");
}
if ($erro == ALERT_PASSWORD_RESETED) {
    $title = translateKey("txt_password_reseted");
    $msg = translateKey("txt_password_reseted_descricao");
}


Out::painel($title, $msg);

escreveFooter();
?>