import {
  ChangeDetectionStrategy,
  Component,
  CUSTOM_ELEMENTS_SCHEMA,
} from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  template: ` <router-outlet></router-outlet>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true,
  imports: [RouterOutlet],
})
export class AppComponent {
  title = 'rsdesenvolvimento-ui';
}
