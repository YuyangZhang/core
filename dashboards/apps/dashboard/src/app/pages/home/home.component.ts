import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardCard, CardConfig } from '@dashboard-workspace/shared-ui';
import { AuthService } from '@dashboard-workspace/auth';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, DashboardCard],
  template: `
    <div class="home-container">
      <header class="home-header">
        <div class="user-info">
          <span class="welcome">Welcome, {{ authService.currentUser()?.username }}</span>
          <button class="logout-btn" (click)="logout()">Logout</button>
        </div>
      </header>

      <main class="home-main">
        <h1 class="page-title">Dashboard</h1>
        <p class="page-subtitle">Select a card to view detailed information</p>

        <div class="cards-grid">
          @for (card of visibleCards; track card.id) {
            <lib-dashboard-card 
              [config]="card" 
              [userRoles]="authService.userRoles()">
            </lib-dashboard-card>
          }
        </div>
      </main>
    </div>
  `,
  styles: [`
    .home-container {
      min-height: 100vh;
      background: #f3f4f6;
    }

    .home-header {
      background: white;
      padding: 1rem 2rem;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
      display: flex;
      justify-content: flex-end;

      .user-info {
        display: flex;
        align-items: center;
        gap: 1rem;

        .welcome {
          color: #374151;
          font-weight: 500;
        }

        .logout-btn {
          padding: 0.5rem 1rem;
          background: #ef4444;
          color: white;
          border: none;
          border-radius: 6px;
          cursor: pointer;
          font-weight: 500;
          transition: background 0.2s;

          &:hover {
            background: #dc2626;
          }
        }
      }
    }

    .home-main {
      padding: 2rem;
      max-width: 1200px;
      margin: 0 auto;

      .page-title {
        margin: 0;
        font-size: 2rem;
        color: #1f2937;
      }

      .page-subtitle {
        margin: 0.5rem 0 2rem 0;
        color: #6b7280;
      }

      .cards-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 1.5rem;
      }
    }
  `],
})
export class HomeComponent {
  protected authService = inject(AuthService);

  cards: CardConfig[] = [
    {
      id: 'sales',
      title: 'Sales Analytics',
      description: 'View sales performance and trends',
      icon: '📊',
      route: '/sales',
      color: '#3b82f6',
      roles: ['admin', 'manager', 'user'],
    },
    {
      id: 'users',
      title: 'User Management',
      description: 'Manage user accounts and permissions',
      icon: '👥',
      route: '/users',
      color: '#10b981',
      roles: ['admin', 'manager'],
    },
    {
      id: 'reports',
      title: 'Reports',
      description: 'Generate and view detailed reports',
      icon: '📈',
      route: '/reports',
      color: '#8b5cf6',
      roles: ['admin', 'manager'],
    },
    {
      id: 'settings',
      title: 'Settings',
      description: 'Configure system settings',
      icon: '⚙️',
      route: '/settings',
      color: '#f59e0b',
      roles: ['admin'],
    },
  ];

  get visibleCards(): CardConfig[] {
    const userRoles = this.authService.userRoles();
    return this.cards.filter(card => 
      card.roles.some(role => userRoles.includes(role))
    );
  }

  logout(): void {
    this.authService.logout();
  }
}
