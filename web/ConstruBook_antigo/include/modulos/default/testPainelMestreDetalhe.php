<?php

class TestPainelMestreDetalhe extends TestBase {

    function testeWidgets() {
        $painel = new PainelMestreDetalhe();
// -- ao mudar o campo mestre, novos campos filhos serão mostrados

        $painel->defineMaster("widget_section", "id_widget_section", "seq_widget_section");
        $painel->defineDetail("widget_coluna", "seq_widget_coluna");

        $painel->addColunaMestre("id_widget_section");
        $painel->addColunaMestre("seq_widget_section");
        $painel->addColunaMestre("nm_widget_section");
        $painel->addColunaDetalhe("id_widget_coluna");
        $painel->addColunaDetalhe("seq_widget_coluna");
        $painel->addColunaDetalhe("id_coluna");

        $painel->load("id_widget", WIDGET_TESTE_SECTION);
        $painel->generate();
    }

}

?>
