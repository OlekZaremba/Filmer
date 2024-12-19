import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DrawService } from '../../services/draw.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-results',
  standalone: true,
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
    this.pollingSubscription = interval(2000).subscribe(() => {
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
            this.router.navigate([`/final-results`, this.lobbyCode]);
          }
        },
        error: (err) => console.error('Błąd podczas sprawdzania statusu głosowania:', err),
      });
    }
  }
}
