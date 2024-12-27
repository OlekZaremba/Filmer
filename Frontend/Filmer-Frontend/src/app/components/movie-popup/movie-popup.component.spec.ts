import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoviePopupComponent } from './movie-popup.component';

describe('MoviePopupComponent', () => {
  let component: MoviePopupComponent;
  let fixture: ComponentFixture<MoviePopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoviePopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoviePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
