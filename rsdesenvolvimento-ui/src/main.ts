import { HttpClient, provideHttpClient } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { ROUTES } from 'src/app/app.routes';
import { httpLoaderFactory } from 'src/app/core/translate/translate-loader-factory';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, {
    providers: [
        provideRouter(ROUTES),
        provideHttpClient(),
        importProvidersFrom(
            BrowserModule,
            BrowserAnimationsModule,
            TranslateModule.forRoot({
                loader: {
                    provide: TranslateModule,
                    useFactory: httpLoaderFactory,
                    deps: [HttpClient],
                },
            })
        ),
    ],
}).catch(err => console.error(err));
