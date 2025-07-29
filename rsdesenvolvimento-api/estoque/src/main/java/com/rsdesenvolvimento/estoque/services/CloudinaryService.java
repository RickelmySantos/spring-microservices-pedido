package com.rsdesenvolvimento.estoque.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rsdesenvolvimento.estoque.core.exception.ImageUploadException;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {

            Map<?, ?> uploadResult =
                    this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new ImageUploadException("Erro ao ler o arquivo de imagem.", e);
        } catch (Exception e) {
            throw new ImageUploadException("Falha ao enviar imagem para o Cloudinary.", e);
        }
    }
}
