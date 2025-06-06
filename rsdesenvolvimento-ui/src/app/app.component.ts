import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
    selector: 'app-root',
    template: `
        <router-outlet></router-outlet>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    standalone: true,
    imports: [RouterOutlet],
})
export class AppComponent implements OnInit {
    title = 'rsdesenvolvimento-ui';
    isLoggedIn = false;

    constructor(private oauthService: AuthService) {}

    ngOnInit(): void {
        this.isLoggedIn = this.oauthService.isLoggedIn();
        if (!this.isLoggedIn) {
            this.oauthService.login();
        }
    }

    login() {
        this.oauthService.login();
    }
}
