import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-contato',
    template: `
        <section class="contact" id="contato">
            <div class="container">
                <app-section-title title="Entre em Contato"></app-section-title>
                <div class="contact-container">
                    <form class="contact-form">
                        <div class="form-group">
                            <label for="nome">Nome</label>
                            <input id="nome" type="text" required />
                        </div>
                        <div class="form-group">
                            <label for="email">E-mail</label>
                            <input id="email" type="email" required />
                        </div>
                        <div class="form-group">
                            <label for="mensagem">Mensagem</label>
                            <textarea id="mensagem" required></textarea>
                        </div>
                        <button class="btn btn-primary" style="width: 100%;">Enviar Mensagem</button>
                    </form>

                    <div class="contact-info">
                        <h3>Informações</h3>
                        <!-- <div class="info-item" *ngFor="let info of infos">
                            <div class="info-icon"><i [class]="info.icon"></i></div>
                            <div class="info-content">
                                <h4>{{ info.title }}</h4>
                                <p>{{ info.details }}</p>
                            </div>
                        </div> -->
                    </div>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor],
})
export class ContatoComponent {}
