import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionOneComponent} from './section-one.component';

describe('MenuComponent', () => {
  let component: SectionOneComponent;
  let fixture: ComponentFixture<SectionOneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SectionOneComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SectionOneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
