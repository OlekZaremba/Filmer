import { Component, OnInit } from '@angular/core';
import { FriendsService } from '../../services/friends.service';
import { Friend } from '../models/friend.model';

@Component({
  selector: 'app-friends-list',
  standalone: true,
  imports: [],
  templateUrl: './friends-list.component.html',
  styleUrl: './friends-list.component.css'
})
export class FriendsListComponent implements OnInit{
  friends: Friend[] = [];

  constructor(private friendsService: FriendsService) {}

  ngOnInit() {
    const userId = 2;
    this.friendsService.getFriendsList(userId).subscribe({
      next: (data) => {
        this.friends = data;
      },
      error: (error) => {
        console.error('Nie udało się pobrać listy znajomych', error);
      }
    })
  }
}
