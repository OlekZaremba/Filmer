import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { DrawService } from '../../services/draw.service';
import { interval, Subscription } from 'rxjs';
import {CommonModule, NgIf} from '@angular/common';
import { RouterModule } from '@angular/router';
import { PopUpComponent } from '../popup/popup.component';
import {ResultsService} from '../../services/results.service';

@Component({
  selector: 'app-results',
  standalone: true,
  imports: [NgIf, RouterLink, RouterLinkActive, RouterModule, PopUpComponent, CommonModule],
  templateUrl: './results.component.html',
  styleUrl: './results.component.css',
})
export class ResultsComponent implements OnInit {
  lobbyCode: string | null = null;
  userId: number | null = null;
  votingCompleted = false;
  pollingSubscription: Subscription | null = null;
  results: { [key: number]: string[] } = {};

  // Stan pop-upu
  isPopupVisible = false;
  popupTitle = '';
  popupItems: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private drawService: DrawService,
    private router: Router,
    private resultsService: ResultsService,
  ) {}

  ngOnInit(): void {
    this.lobbyCode = this.route.snapshot.paramMap.get('lobbyCode');
    const storedUserId = localStorage.getItem('userId');

    if (storedUserId) {
      this.userId = Number(storedUserId);
    } else {
      console.error('Brak userId w localStorage.');
      return;
    }

    if (this.lobbyCode) {
      this.startPolling();
    } else {
      console.error('Brak lobbyCode w URL.');
    }
  }

  showPopupForPlace(place: string, placeKey: number): void {
    this.popupTitle = `Filmy z miejsca ${place}`;
    this.popupItems = (this.results[placeKey] || []).map(
      (film: any) => `${film.filmName} - ${film.filmDesc}`
    );
    this.isPopupVisible = true;
  }

  loadResults(): void {
    if (this.lobbyCode) {
      this.resultsService.getResults(this.lobbyCode).subscribe({
        next: (results) => {
          console.log('Wyniki pobrane z backendu:', results);
          this.results = results;
        },
        error: (err) => console.error('Błąd podczas pobierania wyników:', err),
      });
    }
  }

  closePopup(): void {
    this.isPopupVisible = false;
  }

  startPolling(): void {
    this.pollingSubscription = interval(5000).subscribe(() => {
      this.checkVotingStatus();
    });
  }

  checkVotingStatus(): void {
    if (this.lobbyCode && this.userId !== null) {
      this.drawService.checkVotingStatus(this.lobbyCode, this.userId).subscribe({
        next: (isCompleted: boolean) => {
          this.votingCompleted = isCompleted;
          if (this.votingCompleted) {
            this.pollingSubscription?.unsubscribe();
            this.loadResults();
          }
        },
        error: (err) => console.error('Błąd podczas sprawdzania statusu głosowania:', err),
      });
    } else {
      console.error('Nie ustawiono userId lub lobbyCode.');
    }
  }
}
