import { Route } from '@angular/router';
import { authGuard } from '@dashboard-workspace/auth';

export const appRoutes: Route[] = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
    canActivate: [authGuard],
  },
  {
    path: 'sales',
    loadComponent: () => import('@dashboard-workspace/feature-sales').then(m => m.FeatureSales),
    canActivate: [authGuard],
  },
  {
    path: 'users',
    loadComponent: () => import('@dashboard-workspace/feature-users').then(m => m.FeatureUsers),
    canActivate: [authGuard],
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'unauthorized',
    loadComponent: () => import('./pages/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
