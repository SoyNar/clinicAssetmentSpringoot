package com.example.clinic.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@OpenAPIDefinition(
        info = @Info(
                title = "Api rest Clinic Citas ",
                description =  "api para clinica de citas medicas " +
                        "Agendamiento de citas medicas",
                termsOfService = "www.apiRestCitas.co",
                version = "1.0.0",
                contact = @Contact(
                        name = "Narciris Mena ",
                        email = "narciris.tech@gmail.com"

                ),
                license = @License(
                        name = "Standard software api rest Clinic citas",
                        url = "www.apiRestClinic.co"
                )

        )
//        , security = @SecurityRequirement(
//                name = "security Token"
//)
)
//@SecurityScheme(
//        name = "security Token",
//        description = "Acces token for my Api",
//        type = SecuritySchemeType.HTTP, //cuando trabajamos con tokens
//        in = SecuritySchemeIn.HEADER,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
public class SwaggerConfig {


}
