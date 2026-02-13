import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, delay } from 'rxjs';

export interface DashboardData {
  id: string;
  title: string;
  value: number;
  trend: number;
  unit: string;
}

export interface ChartData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    backgroundColor?: string;
    borderColor?: string;
  }[];
}

export interface TableData {
  columns: string[];
  rows: Record<string, unknown>[];
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = '/api'; // Replace with actual API URL

  constructor(private http: HttpClient) {}

  // Dashboard summary data
  getDashboardSummary(): Observable<DashboardData[]> {
    // Mock data - replace with actual API call
    return of([
      { id: '1', title: 'Total Users', value: 12543, trend: 12.5, unit: '' },
      { id: '2', title: 'Active Sessions', value: 892, trend: -3.2, unit: '' },
      { id: '3', title: 'Revenue', value: 45678, trend: 8.7, unit: '$' },
      { id: '4', title: 'Conversion Rate', value: 3.42, trend: 0.5, unit: '%' },
    ]).pipe(delay(500));
  }

  // Chart data for specific card
  getChartData(cardId: string): Observable<ChartData> {
    // Mock data - replace with actual API call
    const mockData: Record<string, ChartData> = {
      'sales': {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          label: 'Sales',
          data: [65, 59, 80, 81, 56, 55],
          backgroundColor: 'rgba(59, 130, 246, 0.2)',
          borderColor: 'rgba(59, 130, 246, 1)',
        }]
      },
      'users': {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        datasets: [{
          label: 'Active Users',
          data: [150, 230, 180, 290, 210, 180, 120],
          backgroundColor: 'rgba(16, 185, 129, 0.2)',
          borderColor: 'rgba(16, 185, 129, 1)',
        }]
      }
    };

    return of(mockData[cardId] || mockData['sales']).pipe(delay(300));
  }

  // Table data for specific card
  getTableData(cardId: string): Observable<TableData> {
    // Mock data - replace with actual API call
    const mockData: Record<string, TableData> = {
      'orders': {
        columns: ['Order ID', 'Customer', 'Amount', 'Status'],
        rows: [
          { 'Order ID': '#12345', 'Customer': 'John Doe', 'Amount': '$125.00', 'Status': 'Completed' },
          { 'Order ID': '#12346', 'Customer': 'Jane Smith', 'Amount': '$89.50', 'Status': 'Pending' },
          { 'Order ID': '#12347', 'Customer': 'Bob Wilson', 'Amount': '$234.00', 'Status': 'Processing' },
        ]
      }
    };

    return of(mockData[cardId] || mockData['orders']).pipe(delay(300));
  }

  // Generic GET request
  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}/${endpoint}`);
  }

  // Generic POST request
  post<T>(endpoint: string, body: unknown): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}/${endpoint}`, body);
  }

  // Generic PUT request
  put<T>(endpoint: string, body: unknown): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}/${endpoint}`, body);
  }

  // Generic DELETE request
  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(`${this.apiUrl}/${endpoint}`);
  }
}
