package com.rsdesenvolvimento.estoque.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.rsdesenvolvimento.estoque.core.exception.ImageUploadException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("CloudinaryService - Testes Unitários")
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    private byte[] fileBytes;
    private Map<String, Object> uploadResult;

    @BeforeEach
    void setUp() {
        // Arrange
        this.fileBytes = "fake image content".getBytes();
        this.uploadResult = new HashMap<>();
        this.uploadResult.put("secure_url", "https://cloudinary.com/image/test123.jpg");
        this.uploadResult.put("public_id", "test123");
        this.uploadResult.put("version", "1234567890");
    }

    @Nested
    @DisplayName("Upload de Imagem")
    class UploadImagemTests {

        @Test
        @DisplayName("Deve fazer upload da imagem com sucesso")
        void deveFazerUploadDaImagemComSucesso() throws Exception {
            // Arrange
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            String resultado = CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Assertions.assertThat(resultado).isEqualTo("https://cloudinary.com/image/test123.jpg");

            Mockito.verify(CloudinaryServiceTest.this.multipartFile).getBytes();
            Mockito.verify(CloudinaryServiceTest.this.cloudinary).uploader();
            Mockito.verify(CloudinaryServiceTest.this.uploader)
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap());
        }

        @Test
        @DisplayName("Deve retornar URL segura quando upload é bem-sucedido")
        void deveRetornarUrlSeguraQuandoUploadBemSucedido() throws Exception {
            // Arrange
            String expectedUrl =
                    "https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg";
            CloudinaryServiceTest.this.uploadResult.put("secure_url", expectedUrl);

            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            String resultado = CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Assertions.assertThat(resultado).isEqualTo(expectedUrl);
        }

        @Test
        @DisplayName("Deve chamar Cloudinary com parâmetros corretos")
        void deveChamarCloudinaryComParametrosCorretos() throws Exception {
            // Arrange
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Mockito.verify(CloudinaryServiceTest.this.uploader)
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap());
        }
    }

    @Nested
    @DisplayName("Tratamento de Erros")
    class TratamentoErrosTests {

        @Test
        @DisplayName("Deve lançar ImageUploadException quando IOException ocorre ao ler arquivo")
        void deveLancarImageUploadExceptionQuandoIoExceptionOcorreAoLerArquivo() throws Exception {
            // Arrange
            IOException ioException = new IOException("Erro ao ler arquivo");
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenThrow(ioException);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> CloudinaryServiceTest.this.cloudinaryService
                            .uploadImage(CloudinaryServiceTest.this.multipartFile))
                    .isInstanceOf(ImageUploadException.class)
                    .hasMessage("Erro ao ler o arquivo de imagem.").hasCause(ioException);

            Mockito.verify(CloudinaryServiceTest.this.multipartFile).getBytes();
            // Não deve chamar uploader quando IOException ocorre na leitura do arquivo
        }

        @Test
        @DisplayName("Deve lançar ImageUploadException quando falha no upload para Cloudinary")
        void deveLancarImageUploadExceptionQuandoFalhaNoUploadParaCloudinary() throws Exception {
            // Arrange
            RuntimeException cloudinaryException = new RuntimeException("Cloudinary API Error");
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenThrow(cloudinaryException);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> CloudinaryServiceTest.this.cloudinaryService
                            .uploadImage(CloudinaryServiceTest.this.multipartFile))
                    .isInstanceOf(ImageUploadException.class)
                    .hasMessage("Falha ao enviar imagem para o Cloudinary.")
                    .hasCause(cloudinaryException);

            Mockito.verify(CloudinaryServiceTest.this.multipartFile).getBytes();
            Mockito.verify(CloudinaryServiceTest.this.cloudinary).uploader();
        }

        @Test
        @DisplayName("Deve lançar ImageUploadException quando Exception genérica ocorre")
        void deveLancarImageUploadExceptionQuandoExceptionGenericaOcorre() throws Exception {
            // Arrange
            RuntimeException runtimeException = new RuntimeException("Generic error");
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenThrow(runtimeException);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> CloudinaryServiceTest.this.cloudinaryService
                            .uploadImage(CloudinaryServiceTest.this.multipartFile))
                    .isInstanceOf(ImageUploadException.class)
                    .hasMessage("Falha ao enviar imagem para o Cloudinary.")
                    .hasCause(runtimeException);
        }

        @Test
        @DisplayName("Deve lançar ImageUploadException quando secure_url é null")
        void deveLancarImageUploadExceptionQuandoSecureUrlNull() throws Exception {
            // Arrange
            CloudinaryServiceTest.this.uploadResult.put("secure_url", null);
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act & Assert
            Assertions
                    .assertThatThrownBy(() -> CloudinaryServiceTest.this.cloudinaryService
                            .uploadImage(CloudinaryServiceTest.this.multipartFile))
                    .isInstanceOf(ImageUploadException.class)
                    .hasMessage("Falha ao enviar imagem para o Cloudinary.")
                    .hasCauseInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Validação de Dados")
    class ValidacaoDadosTests {

        @Test
        @DisplayName("Deve processar arquivo vazio sem erro")
        void deveProcessarArquivoVazioSemErro() throws Exception {
            // Arrange
            byte[] emptyBytes = new byte[0];
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(emptyBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(
                    CloudinaryServiceTest.this.uploader.upload(emptyBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            String resultado = CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Mockito.verify(CloudinaryServiceTest.this.uploader).upload(emptyBytes,
                    ObjectUtils.emptyMap());
        }

        @Test
        @DisplayName("Deve processar arquivo grande sem erro")
        void deveProcessarArquivoGrandeSemErro() throws Exception {
            // Arrange
            byte[] largeBytes = new byte[1024 * 1024]; // 1MB
            java.util.Arrays.fill(largeBytes, (byte) 1);

            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(largeBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(
                    CloudinaryServiceTest.this.uploader.upload(largeBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            String resultado = CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Assertions.assertThat(resultado).isNotNull();
            Mockito.verify(CloudinaryServiceTest.this.uploader).upload(largeBytes,
                    ObjectUtils.emptyMap());
        }

        @Test
        @DisplayName("Deve usar ObjectUtils.emptyMap() como opções padrão")
        void deveUsarObjectUtilsEmptyMapComoOpcoesPadrao() throws Exception {
            // Arrange
            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Mockito.verify(CloudinaryServiceTest.this.uploader)
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap());
        }
    }

    @Nested
    @DisplayName("Testes de Integração de Comportamento")
    class TestesIntegracaoComportamento {

        @Test
        @DisplayName("Deve executar fluxo completo de upload com sucesso")
        void deveExecutarFluxoCompletoDeUploadComSucesso() throws Exception {
            // Arrange
            String expectedUrl = "https://cloudinary.com/uploaded/image.jpg";
            CloudinaryServiceTest.this.uploadResult.put("secure_url", expectedUrl);

            Mockito.when(CloudinaryServiceTest.this.multipartFile.getBytes())
                    .thenReturn(CloudinaryServiceTest.this.fileBytes);
            Mockito.when(CloudinaryServiceTest.this.cloudinary.uploader())
                    .thenReturn(CloudinaryServiceTest.this.uploader);
            Mockito.when(CloudinaryServiceTest.this.uploader
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap()))
                    .thenReturn(CloudinaryServiceTest.this.uploadResult);

            // Act
            String resultado = CloudinaryServiceTest.this.cloudinaryService
                    .uploadImage(CloudinaryServiceTest.this.multipartFile);

            // Assert
            Assertions.assertThat(resultado).isEqualTo(expectedUrl);

            // Verificar se todos os métodos foram chamados
            Mockito.verify(CloudinaryServiceTest.this.multipartFile).getBytes();
            Mockito.verify(CloudinaryServiceTest.this.cloudinary).uploader();
            Mockito.verify(CloudinaryServiceTest.this.uploader)
                    .upload(CloudinaryServiceTest.this.fileBytes, ObjectUtils.emptyMap());
        }

        @Test
        @DisplayName("Deve garantir que dependências são injetadas corretamente")
        void deveGarantirQueDependenciasSaoInjetadasCorretamente() {
            // Act & Assert
            Assertions.assertThat(CloudinaryServiceTest.this.cloudinaryService).isNotNull();
            Assertions.assertThat(CloudinaryServiceTest.this.cloudinary).isNotNull();
        }
    }
}
