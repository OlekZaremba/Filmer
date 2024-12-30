import {ChangeDetectorRef, Component, OnInit, Renderer2} from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FriendsService } from '../../services/friends.service';
import {MenuComponent} from '../menu/menu.component';
import { FormsModule } from '@angular/forms';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, RouterModule, MenuComponent],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  userName: string = '';
  email: string = '';
  friends: any[] = [];
  searchTerm: string = '';
  searchResults: any[] = [];
  selectedFile: File | null = null;
  currentTheme: 'light' | 'dark' = 'dark';
  currentLanguage: 'polish' | 'english' = 'english';

  constructor(private friendsService: FriendsService, private renderer: Renderer2, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();

    const email = localStorage.getItem('email');
    const nick = localStorage.getItem('nick');
    const userId = localStorage.getItem('userId');

    if (email && nick) {
      this.userName = nick;
      this.email = email;

      if (userId) {
        this.loadFriends(+userId);
        this.loadProfilePicture(+userId);
      } else {
        console.error('ID użytkownika nie jest dostępne w localStorage.');
      }
    } else {
      console.error('E-mail lub nick użytkownika nie są dostępne w localStorage.');
    }
    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  ngOnDestroy(): void {
    // Usuwanie nasłuchiwania po zniszczeniu komponentu
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  private loadLanguage() {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges(); // Wymuszenie odświeżenia widoku
  }

  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  private applyTheme(theme: 'light' | 'dark') {
    const container = document.querySelector('.section-user') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  loadFriends(userId: number): void {
    this.friendsService.getFriends(userId).subscribe({
      next: (friends) => {
        this.friends = friends;

        // Dla każdego znajomego pobierz jego zdjęcie profilowe
        this.friends.forEach(friend => {
          this.loadFriendPicture(friend);
        });
      },
      error: (err) => {
        console.error('Nie udało się pobrać listy znajomych:', err);
      },
    });
  }

  loadFriendPicture(friend: any): void {
    this.friendsService.getProfilePicture(friend.id).subscribe({
      next: (blob) => {
        const objectURL = URL.createObjectURL(blob);
        friend.avatar = objectURL;
      },
      error: (err) => {
        console.error(`Nie udało się pobrać zdjęcia dla użytkownika ${friend.id}:`, err);
        friend.avatar = 'assets/images/user.png';
      },
    });
  }



  loadProfilePicture(userId: number): void {
    this.friendsService.getProfilePicture(userId).subscribe({
      next: (blob) => {
        const objectURL = URL.createObjectURL(blob);
        const imgElement = document.querySelector('.profile-picture') as HTMLImageElement;
        if (imgElement) {
          imgElement.src = objectURL;
        }
      },
      error: (err) => {
        console.error('Błąd podczas ładowania zdjęcia profilowego:', err);
      },
    });
  }



  searchUsers(): void {
    if (this.searchTerm.trim()) {
      this.friendsService.searchUsers(this.searchTerm).subscribe({
        next: (results) => {
          this.searchResults = results;

          this.searchResults.forEach(user => {
            this.friendsService.getProfilePicture(user.id).subscribe({
              next: (blob) => {
                const objectURL = URL.createObjectURL(blob);
                user.avatar = objectURL;
              },
              error: () => {
                user.avatar = 'assets/images/user.png';
              }
            });
          });
        },
        error: (err) => {
          console.error('Błąd podczas wyszukiwania użytkowników:', err);
        }
      });
    } else {
      this.searchResults = [];
    }
  }


  addFriend(friendId: number): void {
    const userId = localStorage.getItem('userId');

    if (userId) {
      this.friendsService.addFriend(+userId, friendId).subscribe({
        next: () => {
          alert('Użytkownik został dodany do znajomych.');
          this.loadFriends(+userId);
          this.searchResults = this.searchResults.filter(user => user.id !== friendId);
        },
        error: (err) => {
          console.error('Błąd podczas dodawania znajomego:', err);
        },
      });
    } else {
      console.error('Brak userId w localStorage.');
    }
  }

  triggerFileInput(): void {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      this.uploadProfilePicture();
    }
  }

  uploadProfilePicture(): void {
    if (!this.selectedFile) {
      alert('Nie wybrano zdjęcia.');
      return;
    }

    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('Nie znaleziono ID użytkownika w localStorage.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.friendsService.uploadProfilePicture(+userId, formData).subscribe({
      next: () => {
        alert('Zdjęcie zostało zapisane.');
        this.loadProfilePicture(+userId);
      },
      error: (err) => {
        console.error('Błąd podczas przesyłania zdjęcia:', err);
        alert('Nie udało się przesłać zdjęcia.');
      },
    });
  }
}
