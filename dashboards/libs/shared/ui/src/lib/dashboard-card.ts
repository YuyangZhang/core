import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

export interface CardConfig {
  id: string;
  title: string;
  description: string;
  icon: string;
  route: string;
  color: string;
  roles: string[];
}

@Component({
  selector: 'lib-dashboard-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard-card.html',
  styleUrl: './dashboard-card.scss',
})
export class DashboardCard {
  @Input() config!: CardConfig;
  @Input() userRoles: string[] = [];

  get isAccessible(): boolean {
    if (!this.config?.roles?.length) return true;
    return this.config.roles.some(role => this.userRoles.includes(role));
  }
}
