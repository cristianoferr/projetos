import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MeusFornecedoresComponent } from './meus-fornecedores.component';

describe('MeusFornecedoresComponent', () => {
  let component: MeusFornecedoresComponent;
  let fixture: ComponentFixture<MeusFornecedoresComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MeusFornecedoresComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeusFornecedoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
