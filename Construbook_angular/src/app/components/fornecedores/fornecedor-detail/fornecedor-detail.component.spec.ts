import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FornecedorDetailComponent } from './fornecedor-detail.component';

describe('FornecedorDetailComponent', () => {
  let component: FornecedorDetailComponent;
  let fixture: ComponentFixture<FornecedorDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FornecedorDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FornecedorDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
