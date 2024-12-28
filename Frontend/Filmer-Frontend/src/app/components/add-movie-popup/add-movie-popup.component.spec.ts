import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMoviePopupComponent } from './add-movie-popup.component';

describe('AddMoviePopupComponent', () => {
  let component: AddMoviePopupComponent;
  let fixture: ComponentFixture<AddMoviePopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddMoviePopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddMoviePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
