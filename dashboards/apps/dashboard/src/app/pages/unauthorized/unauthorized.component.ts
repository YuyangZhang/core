import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="unauthorized-container">
      <div class="unauthorized-card">
        <div class="icon">🔒</div>
        <h1>Unauthorized</h1>
        <p>You don't have permission to access this page.</p>
        <p class="sub-text">Please contact your administrator if you believe this is an error.</p>
        <button (click)="goHome()" class="home-btn">Go to Home</button>
      </div>
    </div>
  `,
  styles: [`
    .unauthorized-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }
    .unauthorized-card {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      text-align: center;
      max-width: 400px;
    }
    .icon {
      font-size: 4rem;
      margin-bottom: 1rem;
    }
    h1 {
      color: #e74c3c;
      margin-bottom: 0.5rem;
    }
    p {
      color: #555;
      margin-bottom: 0.5rem;
    }
    .sub-text {
      font-size: 0.875rem;
      color: #888;
      margin-bottom: 1.5rem;
    }
    .home-btn {
      padding: 0.75rem 2rem;
      background: #3498db;
      color: white;
      border: none;
      border-radius: 4px;
      font-size: 1rem;
      cursor: pointer;
      transition: background 0.3s;
    }
    .home-btn:hover {
      background: #2980b9;
    }
  `]
})
export class UnauthorizedComponent {
  constructor(private router: Router) {}

  goHome(): void {
    this.router.navigate(['/']);
  }
}
