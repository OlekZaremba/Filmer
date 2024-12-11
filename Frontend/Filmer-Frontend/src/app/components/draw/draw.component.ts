import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-draw',
  standalone: true,
  imports: [],
  templateUrl: './draw.component.html',
  styleUrl: './draw.component.css'
})
export class DrawComponent implements OnInit {
  lobbyCode: string | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.lobbyCode = this.route.snapshot.paramMap.get('lobbyCode');
  }
}
