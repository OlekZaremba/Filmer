import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { DrawService } from '../../services/draw.service';
import { interval, Subscription } from 'rxjs';
import { NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PopUpComponent } from '../popup/popup.component';

@Component({
  selector: 'app-results',
  standalone: true,
  imports: [NgIf, RouterLink, RouterLinkActive, RouterModule, PopUpComponent],
  templateUrl: './results.component.html',
  styleUrl: './results.component.css',
})
export class ResultsComponent implements OnInit {
  lobbyCode: string | null = null;
  userId: number | null = null;
  votingCompleted = false;
  pollingSubscription: Subscription | null = null;

  // Stan pop-upu
  isPopupVisible = false;
  popupTitle = '';
  popupItems: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private drawService: DrawService,
    private router: Router
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

  showPopupForPlace(place: string): void {
    this.popupTitle = `Filmy z miejsca ${place}`;
    this.popupItems = [
      'Film 1 - Opis...',
      'Film 2 - Opis...',
      'Film 3 - Opis...',
    ]; // Testowe dane

    console.log('Popup items:', this.popupItems); // Sprawdzenie zawartości
    this.isPopupVisible = true;
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
          }
        },
        error: (err) => console.error('Błąd podczas sprawdzania statusu głosowania:', err),
      });
    } else {
      console.error('Nie ustawiono userId lub lobbyCode.');
    }
  }
}
