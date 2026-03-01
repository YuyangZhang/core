import { Injectable, signal, computed } from '@angular/core';
import { Router } from '@angular/router';

export interface User {
  id: string;
  username: string;
  email: string;
  roles: string[];
  avatar?: string;
}

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  token: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _currentUser = signal<User | null>(null);
  private _token = signal<string | null>(null);

  readonly currentUser = this._currentUser.asReadonly();
  readonly isAuthenticated = computed(() => !!this._currentUser());
  readonly userRoles = computed(() => this._currentUser()?.roles ?? []);
  readonly token = this._token.asReadonly();

  constructor(private router: Router) {
    this.initializeAuth();
  }

  private initializeAuth(): void {
    // Check for stored auth data on init
    const storedToken = localStorage.getItem('auth_token');
    const storedUser = localStorage.getItem('auth_user');
    
    if (storedToken && storedUser) {
      try {
        const user = JSON.parse(storedUser);
        this._token.set(storedToken);
        this._currentUser.set(user);
      } catch {
        this.logout();
      }
    }
  }

  login(username: string, password: string): boolean {
    // Mock login - replace with actual API call
    // This is just for demonstration
    const mockUsers: Record<string, User> = {
      'admin': {
        id: '1',
        username: 'admin',
        email: 'admin@example.com',
        roles: ['admin', 'user'],
      },
      'manager': {
        id: '2',
        username: 'manager',
        email: 'manager@example.com',
        roles: ['manager', 'user'],
      },
      'user': {
        id: '3',
        username: 'user',
        email: 'user@example.com',
        roles: ['user'],
      },
    };

    const user = mockUsers[username];
    if (user && password === 'password') {
      const token = btoa(JSON.stringify({ userId: user.id, exp: Date.now() + 3600000 }));
      this._currentUser.set(user);
      this._token.set(token);
      localStorage.setItem('auth_token', token);
      localStorage.setItem('auth_user', JSON.stringify(user));
      return true;
    }
    return false;
  }

  logout(): void {
    this._currentUser.set(null);
    this._token.set(null);
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
    this.router.navigate(['/login']);
  }

  hasRole(role: string): boolean {
    return this.userRoles().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }
}
