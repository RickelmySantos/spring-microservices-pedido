import { JsonPipe } from '@angular/common';
import { NgModule } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    imports: [TranslateModule, FontAwesomeModule, JsonPipe],
    exports: [TranslateModule, FontAwesomeModule, JsonPipe],
})
export class SharedModule {}
