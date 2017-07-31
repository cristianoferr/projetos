import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './components/header/header.component';
import { ProdutoListComponent } from './components/produtos/produto-list/produto-list.component';
import { ProdutoDetailComponent } from './components/produtos/produto-detail/produto-detail.component';
import { PromocaoListComponent } from './components/produtos/promocao-list/promocao-list.component';
import { FornecedorDetailComponent } from './components/fornecedores/fornecedor-detail/fornecedor-detail.component';
import { MeusFornecedoresComponent } from './components/fornecedores/meus-fornecedores/meus-fornecedores.component';
import { DropdownDirective } from './directives/dropdown.directive';
import { AreaClienteComponent } from './components/clientes/area-cliente/area-cliente.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ProdutoListComponent,
    ProdutoDetailComponent,
    PromocaoListComponent,
    FornecedorDetailComponent,
    MeusFornecedoresComponent,
    DropdownDirective,
    AreaClienteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
