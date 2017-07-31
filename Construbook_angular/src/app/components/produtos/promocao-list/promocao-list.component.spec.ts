import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PromocaoListComponent } from './promocao-list.component';

describe('PromocaoListComponent', () => {
  let component: PromocaoListComponent;
  let fixture: ComponentFixture<PromocaoListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PromocaoListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PromocaoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
