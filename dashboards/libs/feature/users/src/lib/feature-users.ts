import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, ChartData } from '@dashboard-workspace/data-access';

@Component({
  selector: 'lib-feature-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feature-users.html',
  styleUrl: './feature-users.scss',
})
export class FeatureUsers implements OnInit {
  private apiService = inject(ApiService);

  chartData: ChartData | null = null;
  isLoading = true;

  // Mock user data
  users = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin', status: 'Active' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'Manager', status: 'Active' },
    { id: 3, name: 'Bob Wilson', email: 'bob@example.com', role: 'User', status: 'Inactive' },
    { id: 4, name: 'Alice Brown', email: 'alice@example.com', role: 'User', status: 'Active' },
  ];

  ngOnInit(): void {
    this.loadChartData();
  }

  private loadChartData(): void {
    this.apiService.getChartData('users').subscribe(data => {
      this.chartData = data;
      this.isLoading = false;
    });
  }

  getStatusClass(status: string): string {
    return status.toLowerCase() === 'active' ? 'status-active' : 'status-inactive';
  }
}
