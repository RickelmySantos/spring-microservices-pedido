import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SharedModule } from 'src/app/shared/shared.module';

interface ContactDetail {
    icon: string;
    text: string;
    link?: string;
}

interface OperatingHours {
    days: string;
    hours: string;
}

interface SocialLink {
    icon: string;
    url: string;
    label: string;
}

@Component({
    selector: 'app-contato',
    template: `
        <!-- Contact Section -->
        <section id="contact" class="contato-page">
            <div class="container">
                <div class="contato-page__title">
                    <h2>Fale Conosco</h2>
                    <p>Adoraríamos ouvir você! Entre em contato para reservas, dúvidas ou apenas para dizer um "oi".</p>
                </div>

                <div class="contato-page__container">
                    <aside class="contato-page__info">
                        <div class="info-block">
                            <h3>Nosso Endereço</h3>
                            <address>
                                <p *ngFor="let detail of contactDetails">
                                    <a [href]="detail.link" *ngIf="detail.link; else noLink">
                                        <i [class]="detail.icon"></i>
                                        {{ detail.text }}
                                    </a>
                                    <ng-template #noLink>
                                        <i [class]="detail.icon"></i>
                                        {{ detail.text }}
                                    </ng-template>
                                </p>
                            </address>
                        </div>

                        <div class="info-block">
                            <h3>Horário de Funcionamento</h3>
                            <p *ngFor="let item of operatingHours">
                                <strong>{{ item.days }}:</strong>
                                {{ item.hours }}
                            </p>
                        </div>

                        <div class="info-block">
                            <h3>Siga-nos</h3>
                            <div class="social-links">
                                <a *ngFor="let link of socialLinks" [href]="link.url" [attr.aria-label]="link.label" target="_blank">
                                    <i [class]="link.icon"></i>
                                </a>
                            </div>
                        </div>
                    </aside>

                    <div class="contato-page__form-wrapper">
                        <h3>Envie uma Mensagem</h3>
                        <form [formGroup]="contatoForm" (ngSubmit)="submitForm()" novalidate>
                            <div class="form-group">
                                <label for="name">Nome Completo</label>
                                <input type="text" id="name" formControlName="name" />
                                <small *ngIf="contatoForm.get('name')?.touched && contatoForm.get('name')?.errors?.['required']" class="error-message">O nome é obrigatório.</small>
                            </div>

                            <div class="form-group">
                                <label for="email">E-mail</label>
                                <input type="email" id="email" formControlName="email" />
                                <small *ngIf="contatoForm.get('email')?.touched && contatoForm.get('email')?.errors?.['email']" class="error-message">Por favor, insira um e-mail válido.</small>
                            </div>

                            <div class="form-group">
                                <label for="phone">Telefone (Opcional)</label>
                                <input type="tel" id="phone" formControlName="phone" />
                            </div>

                            <div class="form-group">
                                <label for="subject">Assunto</label>
                                <select id="subject" formControlName="subject">
                                    <option value="reservation">Reserva</option>
                                    <option value="question">Dúvida</option>
                                    <option value="feedback">Feedback</option>
                                    <option value="other">Outro</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="message">Mensagem</label>
                                <textarea id="message" formControlName="message" rows="5"></textarea>
                                <small *ngIf="contatoForm.get('message')?.touched && contatoForm.get('message')?.errors?.['minlength']" class="error-message">
                                    A mensagem precisa ter pelo menos 10 caracteres.
                                </small>
                            </div>

                            <button type="submit" class="btn btn--primary" [disabled]="contatoForm.invalid">Enviar Mensagem</button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, ReactiveFormsModule, NgFor, NgIf],
})
export class ContatoComponent implements OnInit {
    protected fb: FormBuilder = inject(FormBuilder);

    contatoForm: FormGroup;

    ngOnInit(): void {
        this.contatoForm = this.fb.group({
            name: [{ value: '' }, Validators.required],
            email: [{ value: '' }, Validators.required],
            phone: [{ value: '' }, Validators.required],
            subject: ['reservation'],
            message: [{ value: '' }, Validators.required],
        });
    }

    contactDetails: ContactDetail[] = [
        { icon: 'fas fa-map-marker-alt', text: '123 Avenida Brasil, Ananindeua, PA' },
        { icon: 'fas fa-phone', text: '(91) 99999-0187', link: 'tel:+5591999990187' },
        { icon: 'fas fa-envelope', text: 'contato@saborbrasileiro.com', link: 'mailto:contato@saborbrasileiro.com' },
    ];

    operatingHours: OperatingHours[] = [
        { days: 'Segunda - Quinta', hours: '11:30 - 22:00' },
        { days: 'Sexta - Sábado', hours: '11:30 - 23:00' },
        { days: 'Domingo', hours: '12:00 - 21:00' },
    ];

    socialLinks: SocialLink[] = [
        { icon: 'fab fa-instagram', url: '#', label: 'Instagram' },
        { icon: 'fab fa-facebook-f', url: '#', label: 'Facebook' },
        { icon: 'fab fa-whatsapp', url: '#', label: 'WhatsApp' },
    ];

    submitForm(): void {
        if (this.contatoForm.valid) {
            console.log('Formulário enviado!', this.contatoForm.value);

            this.contatoForm.reset({ subject: 'reservation' });
        } else {
            console.error('Formulário inválido. Por favor, verifique os campos.');
            this.contatoForm.markAllAsTouched();
        }
    }
}
