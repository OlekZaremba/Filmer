import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DrawService } from '../../services/draw.service';
import { interval, Subscription } from 'rxjs';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-results',
  standalone: true,
  imports: [NgIf],
  templateUrl: './results.component.html',
  styleUrl: './results.component.css',
})
export class ResultsComponent implements OnInit {
  lobbyCode: string | null = null;
  userId: number | null = null;
  votingCompleted = false;
  pollingSubscription: Subscription | null = null;

  constructor(private route: ActivatedRoute, private drawService: DrawService, private router: Router) {}

  ngOnInit(): void {
    this.lobbyCode = this.route.snapshot.paramMap.get('lobbyCode');
    console.log('Lobby Code w ResultsComponent:', this.lobbyCode);
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

  startPolling(): void {
    this.pollingSubscription = interval(5000).subscribe(() => {
      console.log('Wywołanie pollingu: sprawdzanie statusu głosowania...');
      this.checkVotingStatus();
    });
  }

  checkVotingStatus(): void {
    if (this.lobbyCode && this.userId !== null) {
      this.drawService.checkVotingStatus(this.lobbyCode, this.userId).subscribe({
        next: (isCompleted: boolean) => {
          console.log('Status głosowania z backendu:', isCompleted);
          this.votingCompleted = isCompleted;
          if (this.votingCompleted) {
            console.log('Głosowanie zakończone, przerywanie polling.');
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
