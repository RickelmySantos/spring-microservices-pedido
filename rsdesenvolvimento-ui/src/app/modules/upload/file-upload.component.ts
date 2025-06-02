import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FileUploadModule } from 'primeng/fileupload';
import { EstoqueService } from 'src/app/services/estoque.service';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-file-upload',
    template: `
        <form [formGroup]="form" (ngSubmit)="enviarProduto()" class="w-full max-w-2xl bg-white rounded-xl shadow-lg overflow-hidden mt-8 ">
            <div class="ml-6 mt-6">
                <div class="flex items-center justify-between">
                    <div>
                        <h1 class="text-2xl font-bold">Upload Your Images</h1>
                        <p class="text-xl font-bold">Share your favorite moments with us</p>
                    </div>
                    <div class="bg-white bg-opacity-20 p-3 rounded-full">
                        <i class="fas fa-cloud-upload-alt text-xl"></i>
                    </div>
                </div>
            </div>

            <div class="p-6">
                <div class="form-file">
                    <input type="file" id="fileInput" class="file-input" accept="image/*" (change)="onFileSelected($event)" />
                    <!-- <p-fileUpload mode="basic" chooseLabel="upload" chooseIcon="pi pi-upload" accept="image/*"></p-fileUpload> -->
                    <label for="fileInput" class="cursor-pointer">
                        <div class="mx-auto w-16 h-16 bg-blue-100 border-round flex items-center justify-center mb-4">
                            <i class="fas fa-cloud-upload-alt text-blue-500 text-2xl"></i>
                        </div>
                        <h3 class="text-lg font-medium text-gray-700 mb-1">Drag & drop files here</h3>
                        <p class="text-gray-500 mb-4">or click to browse</p>
                        <div class="text-sm text-gray-400">Suporta: JPG, PNG</div>
                    </label>
                    <div *ngIf="form.get('imagem')?.invalid && form.get('imagem')?.touched" class="text-red-600 text-sm mt-2">Imagem é obrigatória</div>
                </div>

                <div class="mb-6">
                    <h3 class="text-lg font-medium text-gray-700 mb-4">Formulário</h3>
                    <div class="grid">
                        <div class="col-6">
                            <label for="nome" class="text-lg font-bold ">Nome</label>
                            <input type="text" id="nome" pInputText formControlName="nome" required="true" class="input" />
                            <div *ngIf="form.get('nome')?.invalid && form.get('nome')?.touched" class="text-red-600 text-sm">Nome é obrigatório</div>
                        </div>
                        <div class="col-6">
                            <label for="descricao" class="text-lg font-bold">Descrição</label>
                            <textarea id="descricao" rows="3" formControlName="descricao" class="input"></textarea>
                            <div *ngIf="form.get('descricao')?.invalid && form.get('descricao')?.touched" class="text-red-600 text-sm">Descrição é obrigatória</div>
                        </div>
                        <div class="col-6">
                            <label for="preco" class="text-lg font-bold">Preço</label>
                            <input type="text" id="preco" formControlName="preco" class="input" />
                            <div *ngIf="form.get('preco')?.invalid && form.get('preco')?.touched" class="text-red-600 text-sm">Preço é obrigatório</div>
                        </div>
                        <div class="col-6">
                            <label for="categoria" class="text-lg font-bold">Categoria</label>
                            <input type="text" id="categoria" formControlName="categoria" class="input" />
                            <div *ngIf="form.get('categoria')?.invalid && form.get('categoria')?.touched" class="text-red-600 text-sm">Categoria é obrigatória</div>
                        </div>
                        <div class="col-6">
                            <label for="estoque" class="text-lg font-bold">Estoque</label>
                            <input type="text" id="estoque" formControlName="estoque" class="input" />
                            <div *ngIf="form.get('estoque')?.invalid && form.get('estoque')?.touched" class="text-red-600 text-sm">Estoque é obrigatório</div>
                        </div>
                    </div>
                </div>

                <div class="flex flex-col sm:flex-row justify-end gap-3">
                    <button type="button" class="px-6 py-2 border border-gray-300 border-round text-gray-700  hover:bg-gray-50 transition cursor-pointer " (click)="form.reset()">Cancelar</button>
                    <button type="submit" [disabled]="form.invalid" id="uploadBtn" class="btn">
                        <i class="fas fa-upload"></i>
                        Upload Images
                    </button>
                </div>
            </div>

            <div *ngIf="uploadSuccess" class="bg-green-50 border-l-4 border-green-500 p-4 mb-6 mx-6">
                <div class="flex items-center">
                    <div class="flex-shrink-0">
                        <i class="fas fa-check-circle text-green-500 text-xl"></i>
                    </div>
                    <div class="ml-3">
                        <p class="text-sm text-green-700">
                            <span class="font-medium">Sucesso!</span>
                            Upload concluído com sucesso
                        </p>
                    </div>
                </div>
            </div>
        </form>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, ReactiveFormsModule, NgIf, FileUploadModule],
    styles: `
        .file-input {
            opacity: 0;
            position: absolute;
            z-index: -1;
        }
        .preview-container {
            transition: all 0.3s ease;
        }
        .preview-container:hover {
            transform: scale(1.02);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }
        .upload-btn {
            transition: all 0.3s ease;
        }
        .upload-btn:hover {
            transform: translateY(-2px);
        }
        .progress-bar {
            transition: width 0.5s ease;
        }
        @keyframes pulse {
            0%, 100% {
                opacity: 1;
            }
            50% {
                opacity: 0.5;
            }
        }
        .pulse {
            animation: pulse 2s infinite;
        }`,
})
export class FileUploadComponent implements OnInit {
    form: FormGroup;
    selectedFile: File | null = null;
    uploadSuccess = false;

    constructor(private fb: FormBuilder, private estoqueService: EstoqueService) {
        this.form = this.fb.group({
            nome: ['', Validators.required],
            descricao: ['', Validators.required],
            preco: ['', Validators.required],
            categoria: ['', Validators.required],
            estoque: ['', Validators.required],
            imagem: [null, Validators.required],
        });
    }

    ngOnInit(): void {}

    onFileSelected(event: any) {
        const file: File = event.target.files[0];
        if (file) {
            this.selectedFile = file;
            this.form.patchValue({ imagem: file });
            this.form.get('imagem')?.updateValueAndValidity();
        }
    }

    enviarProduto() {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        const { imagem, ...dadosProduto } = this.form.value;

        if (!imagem) {
            alert('Selecione uma imagem');
            return;
        }

        this.estoqueService.salvarProduto(dadosProduto, imagem).subscribe({
            next: () => {
                this.uploadSuccess = true;
                this.form.reset();
                this.selectedFile = null;
            },
            error: () => {
                alert('Erro ao cadastrar produto');
            },
        });
    }
}
