import { Injectable } from '@angular/core';

@Injectable()
export abstract class StorageSerivce<T> {
    protected abstract readonly STORAGE_KEY: string;

    public salvar(dados: T): void {
        try {
            localStorage.setItem(this.STORAGE_KEY, JSON.stringify(dados));
        } catch (error) {
            console.warn(`Erro ao salvar dados no localStorage (${this.STORAGE_KEY}):`, error);
        }
    }

    public carregar(): T | null {
        try {
            const dados = localStorage.getItem(this.STORAGE_KEY);
            return dados ? JSON.parse(dados) : null;
        } catch (error) {
            console.warn(`Erro ao carregar dados do localStorage (${this.STORAGE_KEY}):`, error);
            return null;
        }
    }

    public limpar(): void {
        try {
            localStorage.removeItem(this.STORAGE_KEY);
        } catch (error) {
            console.warn(`Erro ao limpar dados do localStorage (${this.STORAGE_KEY}):`, error);
        }
    }
}
