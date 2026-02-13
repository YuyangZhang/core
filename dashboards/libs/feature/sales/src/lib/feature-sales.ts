import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, ChartData, DashboardData } from '@dashboard-workspace/data-access';

@Component({
  selector: 'lib-feature-sales',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feature-sales.html',
  styleUrl: './feature-sales.scss',
})
export class FeatureSales implements OnInit {
  private apiService = inject(ApiService);

  summaryData: DashboardData[] = [];
  chartData: ChartData | null = null;
  isLoading = true;

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(): void {
    this.apiService.getDashboardSummary().subscribe(data => {
      this.summaryData = data;
    });

    this.apiService.getChartData('sales').subscribe(data => {
      this.chartData = data;
      this.isLoading = false;
    });
  }

  getTrendClass(trend: number): string {
    return trend >= 0 ? 'trend-positive' : 'trend-negative';
  }

  getTrendIcon(trend: number): string {
    return trend >= 0 ? '↑' : '↓';
  }
}
