package unit.services;

import unit.assertions.AssertableResponse;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

public class FileService {
    public AssertableResponse downloadBaseImage(){
        return new AssertableResponse(given()
                .get("/unit/files/download")
                .then());

    }
    public AssertableResponse downloadLastUploaded(){
        return  new AssertableResponse(given()
                .get("/unit/files/downloadLastUploaded")
                .then());
    }
    @SneakyThrows
    public AssertableResponse uploadFile(File file){
        return new AssertableResponse(given().contentType(ContentType.MULTIPART)
                .multiPart("file", "myFile", Files.readAllBytes(file.toPath()))//загружаем файл
                .post("/unit/files/upload")
                .then());
    }

}
